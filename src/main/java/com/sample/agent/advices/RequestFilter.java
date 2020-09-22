package com.sample.agent.advices;

import com.sample.agent.utils.DataCache;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.Callable;


@PreMatching
public class RequestFilter implements ContainerRequestFilter {

    @RuntimeType
    public static Object interceptFilter(@SuperCall Callable<?> zuper, @AllArguments Object... args) throws Exception {
        System.out.println("Agent filtering ...");

        String tc = ((ContainerRequestContext) args[0]).getHeaderString("testcase");
        String pathInfo = ((ContainerRequestContext) args[0]).getUriInfo().getPath().toString();

        System.out.println("Agent uri-path: " + pathInfo);
        System.out.println("Agent TC: " + tc);

        // Here is to handle for any prior request

        return zuper.call();
    }


    /**
     * Override to use ContainerRequestContext
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    }
}
