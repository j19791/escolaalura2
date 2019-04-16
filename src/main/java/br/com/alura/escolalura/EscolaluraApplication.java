package br.com.alura.escolalura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EscolaluraApplication {

	public static void main(String[] args) {
		SpringApplication.run(EscolaluraApplication.class, args);
	}

	//O SpringBoot não precisa de um servidor para ser executado. O mesmo já vem preparado com um contêiner pré-configurado para executar o projeto sem maiores problemas.
	
}
