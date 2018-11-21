package org.spring;

import org.spring.beans.SimpleComponent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Spring Core!
 */
public class App {
    public static Logger logger = Logger.getLogger("MAIN");

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("Start ...");
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        applicationContext.getBean(SimpleComponent.class).doStuff();
        logger.info("Application Context created.");
    }
}
