package org.spring;

import org.spring.beans.E46BMW;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.UUID;

@Configuration
@ComponentScan("org.spring")
public class SpringConfiguration {

    @Bean
    public E46BMW e46BMW() {
        final String vin = UUID.randomUUID().toString();
        App.logger.info("Configuration vin: " + vin);
        return new E46BMW(vin);
    }

    @EventListener
    public void onAppEvent(ContextRefreshedEvent contextRefreshedEvent) {
        App.logger.info("After context refreshed all beans are ready to use.");
        App.logger.info("Bean e46 has a VIN: " + e46BMW().getVIN());
    }
}
