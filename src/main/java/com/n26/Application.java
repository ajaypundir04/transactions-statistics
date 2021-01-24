package com.n26;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.TimeZone;

@SpringBootApplication
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main( String... args ) throws UnknownHostException {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        Environment env = run.getEnvironment();
        String serverPort = env.getProperty("server.port");

        LOGGER.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "Swagger: \thttp://localhost:{}/swagger-ui.html\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                serverPort,
                serverPort,
                InetAddress.getLocalHost().getHostAddress(), serverPort);

        LOGGER.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));

    }

    @PostConstruct
    public void initTimeZone() {
        TimeZone.setDefault( TimeZone.getTimeZone( "UTC" ) );
    }
}
