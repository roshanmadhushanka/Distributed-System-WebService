package home;

import home.system.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DSClientApp {
    public static void main(String[] args) {

        // Set up system configuration
        Configuration.setSystemIPAddress("127.0.0.1");
        Configuration.setSystemPort(12345);
        Configuration.setSystemName("Client 1");
        Configuration.setBsIpAddress("localhost");
        Configuration.setBsPort(55555);

        SpringApplication.run(DSClientApp.class, args);
    }
}
