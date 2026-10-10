package com.example.nexo;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NexoApplication {

	public static void main(String[] args) {
		// F35–F37 · la estación opera en hora de Perú: todas las fechas y horas
		// que genera el servidor (asistencia, ventas y compras) se calculan en
		// America/Lima aunque la máquina de despliegue use otra zona.
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
		SpringApplication.run(NexoApplication.class, args);
	}

}
