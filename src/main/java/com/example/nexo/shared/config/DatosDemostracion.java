package com.example.nexo.shared.config;

import java.nio.charset.StandardCharsets;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.example.nexo.usuario.service.UsuarioServiceImpl;

/** Semilla ficticia optativa, una sola vez. Nunca reemplaza datos al reiniciar. */
@Component @Profile("demo")
public class DatosDemostracion implements ApplicationRunner {
    private final JdbcTemplate jdbc; private final PasswordEncoder encoder;
    public DatosDemostracion(JdbcTemplate jdbc,PasswordEncoder encoder) {this.jdbc=jdbc;this.encoder=encoder;}
    @Override @Transactional
    public void run(ApplicationArguments argumentos) throws Exception {
        if(jdbc.queryForObject("select count(*) from usuario",Long.class)>0)return;
        for(String tabla:java.util.List.of("categoria","producto","proveedor","empleado","compra","venta","caja_diaria","asistencia")) {
            if(jdbc.queryForObject("select count(*) from "+tabla,Long.class)>0)
                throw new IllegalStateException("El perfil demo requiere una base nueva; no se reemplazan datos existentes.");
        }
        String password=UsuarioServiceImpl.CONTRASENA_DEMO;
        if(password==null || password.length()<8 || password.length()>40)
            throw new IllegalStateException("Define NEXO_DEMO_PASSWORD (8 a 40 caracteres) para inicializar la demostración.");
        String sql=new ClassPathResource("db/demo.sql").getContentAsString(StandardCharsets.UTF_8)
                .replace("${passwordHash}",encoder.encode(password));
        jdbc.execute((ConnectionCallback<Void>) connection->{
            ScriptUtils.executeSqlScript(connection,new EncodedResource(new ByteArrayResource(sql.getBytes(StandardCharsets.UTF_8)),StandardCharsets.UTF_8));return null;
        });
    }
}
