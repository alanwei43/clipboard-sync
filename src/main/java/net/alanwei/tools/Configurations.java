package net.alanwei.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.UUID;

@ComponentScan
public class Configurations {
    public static final String LOCAL_HOST_SOURCE_ID;

    static {
        LOCAL_HOST_SOURCE_ID = String.format("%s/%s/%s", System.getProperty("os.name"), System.getProperty("user.name"), UUID.randomUUID().toString());
    }

    private Configurations() {
    }

    public static ApplicationContext getContext() {
        return new AnnotationConfigApplicationContext(Configurations.class);
    }

    public static String getMqUri() {
        return "amqp://guest:guest@47.52.157.46:32771/%2f";
    }

}
