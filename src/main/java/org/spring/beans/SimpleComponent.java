package org.spring.beans;

import org.spring.App;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class SimpleComponent implements BeanNameAware, ApplicationContextAware {
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

    @Override
    public void setBeanName(String name) {
        App.logger.info("this bean called " + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        App.logger.info("this bean is handled by context: " + applicationContext.getApplicationName());
    }
}
