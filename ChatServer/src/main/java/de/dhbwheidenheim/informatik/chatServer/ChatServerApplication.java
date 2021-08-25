package de.dhbwheidenheim.informatik.chatServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import de.dhbwheidenheim.informatik.chatServer.controller.ChatServerController;
import de.dhbwheidenheim.informatik.chatServer.model.Person;

@SpringBootApplication
public class ChatServerApplication {

	public static void main(String[] args) {
		ChatServerController.init();
		SpringApplication.run(ChatServerApplication.class, args);
	}


	
}
