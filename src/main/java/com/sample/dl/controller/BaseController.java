package com.sample.dl.controller;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Path("/")
public class BaseController {

    @GET
    @Produces("application/json")
    @Path("/ok")
    @RuntimeType
    public Response appOk(@HeaderParam("testcase") String testCase) {
        String greet = "App is OK";
        System.out.println(greet);
        String output = "{'greet': '" + greet + "'}";
        return Response.status(200).entity(output).build();
    }
}