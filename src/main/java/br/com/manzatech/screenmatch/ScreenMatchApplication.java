package br.com.manzatech.screenmatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import br.com.manzatech.screenmatch.principal.Principal;

@SpringBootApplication
public class ScreenMatchApplication  {

	public static void main(String[] args) {
	 	SpringApplication.run(ScreenMatchApplication.class, args);
	}

}
