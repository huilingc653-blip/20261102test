package com.bytter.actuator;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class BytterActuatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BytterActuatorApplication.class, args);
	}

}
