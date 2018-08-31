package org.spring.beans;

import org.spring.App;
import org.spring.annotations.PrintVIN;

import javax.annotation.PostConstruct;

public class E46BMW {
    private final String VIN;

    public E46BMW(String vin) {
        this.VIN = vin;
        App.logger.info("BMW constructor works.");
    }

    @PrintVIN
    public String getVIN() {
        return VIN;
    }

    @PostConstruct
    public void postConstruct() {
        App.logger.info("Bean PostConstruct here.");
    }
}
