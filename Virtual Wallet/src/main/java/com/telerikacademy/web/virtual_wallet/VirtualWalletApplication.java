package com.telerikacademy.web.virtual_wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.telerikacademy.web.virtual_wallet.models")
public class VirtualWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualWalletApplication.class, args);
    }

}
