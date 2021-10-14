package de.dhbwheidenheim.informatik.chatServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.dhbwheidenheim.informatik.chatServer.controller.ChatServerController;

@SpringBootApplication
public class ChatServerApplication {

	public static void main(String[] args) {
		ChatServerController.init();
		SpringApplication.run(ChatServerApplication.class, args);
	}

}
