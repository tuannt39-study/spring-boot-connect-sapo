package vn.sapo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import vn.sapo.config.SapoProperties;

@SpringBootApplication
@EnableConfigurationProperties({SapoProperties.class})
public class SpringBootConnectSapoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConnectSapoApplication.class, args);
    }

}
