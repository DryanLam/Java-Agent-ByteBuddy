package com.sample.agent.advices;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;
import java.util.concurrent.Callable;

@PreMatching
public class RequestFilter implements ContainerRequestFilter {

    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> zuper,  @AllArguments Object... args) throws Exception {
        try {
            String tc =  ((ContainerRequestContext)args[0]).getHeaderString("testcase");
            String pathInfo =  ((ContainerRequestContext)args[0]).getUriInfo().getPath();
            System.out.println("Agent uri-path: " + pathInfo);

            if("register" == pathInfo){
                // Get variable to store tc-ID: @ID-ABC
            }

            if("tc-revoke" == pathInfo){
                //
                // Generate data end insert to DB;
            }

            System.out.println("Agent TC: " + tc);
            return zuper.call();
        } finally {
            System.out.println("method end");
        }
    }

    /**
     * Override to use ContainerRequestContext
     */
    @Override
    public void filter (ContainerRequestContext requestContext) throws IOException {
    }
}
