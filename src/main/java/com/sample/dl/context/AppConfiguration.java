package com.sample.dl.context;


import com.sample.dl.controller.BaseController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfiguration extends ResourceConfig {
    //	public AppConfiguration(@HeaderParam("content-type") String contentType) {
    public AppConfiguration() {
        register(BaseController.class);
    }


}
