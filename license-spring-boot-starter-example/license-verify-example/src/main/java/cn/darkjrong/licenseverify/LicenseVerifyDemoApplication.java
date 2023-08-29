package cn.darkjrong.licenseverify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LicenseVerifyDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenseVerifyDemoApplication.class, args);
    }

}
