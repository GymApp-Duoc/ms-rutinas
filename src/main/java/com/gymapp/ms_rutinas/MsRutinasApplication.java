package com.gymapp.ms_rutinas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsRutinasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRutinasApplication.class, args);
	}
}