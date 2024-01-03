package br.com.marianadmoreira.bibliotecaob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BibliotecaobApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaobApplication.class, args);
	}

}
