package com.sample.agent.advices;


import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public class TcAdvice {

    @GET
    @Produces("application/json")
    @Path("/ok")
    public Response registerTC(@HeaderParam("testcase") String testCase)  throws Exception {
        System.out.println("Agent welcomes to REGISTER");
        String output = "{'tc': '" + testCase + "'}";
        System.out.println(output);
        return Response.status(200).entity(output).build();
    }
}
