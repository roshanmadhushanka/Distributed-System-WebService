package home;

import home.gui.Main;
import home.system.Configuration;
import home.table.FileTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.net.InetAddress;

@SpringBootApplication
public class DSClientApp {
    public static void main(String[] args) throws IOException {

        // Identify host that connected
        String host = InetAddress.getLocalHost().getHostAddress();

        // Set up system configuration
        Configuration.loadConfigurations();
        // Configuration.setSystemIPAddress(host);

        // Display configurations
        Configuration.display();

        // Display file table
        FileTable.display();

//        // Simulate
//        RandomQueryGenerator randomQueryGenerator = new RandomQueryGenerator();
//        randomQueryGenerator.start();


        Main.getForm().setVisible(true);
        SpringApplication.run(DSClientApp.class, args);
    }
}
