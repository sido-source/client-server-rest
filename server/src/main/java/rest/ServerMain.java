package rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerMain {

    public static void main(String[] args) {
        System.out.println("Server is running");
        SpringApplication.run(ServerMain.class);
    }
}
