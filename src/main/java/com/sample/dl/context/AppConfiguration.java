package com.sample.dl.context;


import org.glassfish.jersey.server.ResourceConfig;
import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;


@Configuration
public class AppConfiguration extends ResourceConfig {
    //	public AppConfiguration(@HeaderParam("content-type") String contentType) {
    public AppConfiguration() {
        scan("com.sample.dl.controller");
    }

    // Also lay on both methods
    @PostConstruct
    public void setUp() {
    }

    public void scan(String... packages) {
        for (String pack : packages) {
            Reflections reflections = new Reflections(pack);
            reflections.getTypesAnnotatedWith(Path.class, true)
                       .parallelStream()
                       .forEach((clazz) -> {
                           register(clazz);
                       });
        }
    }
}
