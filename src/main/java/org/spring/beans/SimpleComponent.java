package org.spring.beans;

import org.spring.App;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class SimpleComponent {
    private String name;

    public void doStuff() {
        App.logger.info("Doing something for client.");
    }

    @Bean
    public E46BMW e46BMW() {
        final String vin = UUID.randomUUID().toString();
        App.logger.info("Simple component vin: " + vin);
        return new E46BMW(vin);
    }

    @PostConstruct
    public void postConstr() {
        App.logger.info("Simple Component post construct");
    }
}
