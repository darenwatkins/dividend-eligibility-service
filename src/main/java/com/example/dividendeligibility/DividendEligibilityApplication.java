package com.example.dividendeligibility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.custody.eligibility")
public class DividendEligibilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(DividendEligibilityApplication.class, args);
    }

}
