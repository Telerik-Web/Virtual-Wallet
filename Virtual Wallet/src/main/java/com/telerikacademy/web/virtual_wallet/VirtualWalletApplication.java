package com.telerikacademy.web.virtual_wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.telerikacademy.web.virtual_wallet.models")
public class VirtualWalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(VirtualWalletApplication.class, args);
    }
    //deposits in transactions
    //акт и компания в тестовете
    //add the proper message for a duplicate card - try to add a card with already existing number
}
