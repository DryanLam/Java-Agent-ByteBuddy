package com.sample.agent.advices;

import com.sample.agent.utils.DataCache;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;;
import java.io.IOException;
import java.util.concurrent.Callable;

@PreMatching
public class RequestFilter implements ContainerRequestFilter {

    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> zuper, @AllArguments Object... args) throws Exception {
        try {
            System.out.println("Agent filtering ...");

            String tc = ((ContainerRequestContext) args[0]).getHeaderString("testcase");
            String pathInfo = ((ContainerRequestContext) args[0]).getUriInfo().getPath().toString();

            System.out.println("Agent uri-path: " + pathInfo);
            System.out.println("Agent TC: " + tc);

            // Start data caching
            DataCache cache = DataCache.getInstance();
            cache.updateStatus("Starting");

            if (pathInfo.contains("register")) {
                System.out.println("Registering test case");

                // Get variable to store tc-ID: @ID-ABC
                cache.updateStatus("Caching");
                cache.addData("tc", tc);

                String output = "{'tc': '" + tc + "', 'status': 'start'}";
                return Response.status(200).entity(output).build();

            } else if (pathInfo.contains("logout")) {

                // Generate data end insert to DB;
                cache.reset();

                String output = "{'tc': '" + tc + "', 'status': 'end'}";
                return Response.status(200).entity(output).build();
            } else {
                return zuper.call();
            }
        } finally {
            System.out.println("method end");
        }
    }


    /**
     * Override to use ContainerRequestContext
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    }
}
