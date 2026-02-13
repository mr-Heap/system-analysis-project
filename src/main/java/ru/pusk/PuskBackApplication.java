package ru.pusk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.pusk")
@EnableScheduling
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class PuskBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(PuskBackApplication.class, args);
    }

}
