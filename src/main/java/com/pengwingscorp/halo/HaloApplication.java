package com.pengwingscorp.halo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class HaloApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaloApplication.class, args);
		System.out.println("Salve");
	}

}
