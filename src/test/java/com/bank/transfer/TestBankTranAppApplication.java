package com.bank.transfer;

import org.springframework.boot.SpringApplication;

public class TestBankTranAppApplication {

    public static void main(String[] args) {
        SpringApplication.from(BankTranAppApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
