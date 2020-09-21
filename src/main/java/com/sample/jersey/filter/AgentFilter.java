package com.sample.jersey.filter;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import java.io.IOException;
import java.util.concurrent.Callable;

@PreMatching
public class AgentFilter implements ContainerRequestFilter {

    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> zuper,  @AllArguments Object... args) throws Exception {
        System.out.println("Agent method start");
        try {
//            new AgentFilter().filter(requestContext);
            return zuper.call();
        } finally {
            System.out.println("method end");
        }
    }

    @Override
    public void filter (ContainerRequestContext requestContext) throws IOException {
        System.out.println("AGENT filtering ... ");
        String tc = requestContext.getHeaderString("testcase");
        System.out.println(tc);
    }
}
