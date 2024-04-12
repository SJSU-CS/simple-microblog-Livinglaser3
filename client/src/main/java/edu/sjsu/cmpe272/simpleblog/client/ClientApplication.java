package edu.sjsu.cmpe272.simpleblog.client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ClientApplication {


    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ClientApplication.class, args)));
    }


}
