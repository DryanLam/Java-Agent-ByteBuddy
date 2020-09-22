package com.sample.dl.controller;

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
    @Path("/info")
    public Response appInfo(@HeaderParam("testcase") String testCase) {
        String greet = "Warning! Your app is HACKED!!!";
        System.out.println(greet);
        String output = "{'greet': '" + greet + "', 'status': 'fresh'}";
        return Response.status(200).entity(output).build();
    }


    @GET
    @Produces("application/json")
    @Path("/register")
    public Response appOk(@HeaderParam("testcase") String testCase) {
        String greet = "App is OK";
        System.out.println(greet);
        String output = "{'greet': '" + greet + "', 'status': 'registered'}";
        return Response.status(200).entity(output).build();
    }


    @GET
    @Produces("application/json")
    @Path("/logout")
    public Response logOut(@HeaderParam("testcase") String testCase) {
        String greet = "Good bye!!!";
        System.out.println(greet);
        String output = "{'greet': '" + greet + "', 'status': 'exit'}";
        return Response.status(200).entity(output).build();
    }
}