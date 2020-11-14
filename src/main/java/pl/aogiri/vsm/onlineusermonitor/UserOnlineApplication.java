package pl.aogiri.vsm.onlineusermonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.aogiri.vsm.onlineusermonitor.controller.UserOnlineController;

@SpringBootApplication
public class UserOnlineApplication {

    public static void main(String... args){
        ConfigurableApplicationContext context = SpringApplication.run(UserOnlineApplication.class, args);
        context.getBean(UserOnlineController.class).start();
    }
}
