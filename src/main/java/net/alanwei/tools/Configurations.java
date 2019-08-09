package net.alanwei.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class Configurations {
    private Configurations() {
    }

    public static ApplicationContext getContext() {
        return new AnnotationConfigApplicationContext(Configurations.class);
    }

}
