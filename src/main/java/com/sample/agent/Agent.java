package com.sample.agent;

import com.sample.agent.advices.MethodAdvice;
import com.sample.agent.advices.RequestFilter;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Agent {
    private static final String INCLUDE_PACKAGE_INSTRUMENT = "com.sample.dl";
    private static final String EXCLUDE_PACKAGE_INSTRUMENT = "com.sample.dl.context";


    /**
     * If the agent is attached to a JVM on the start,
     * this method is invoked before {@code main} method is called.
     *
     * @param agentArgs       Agent command line arguments.
     * @param instrumentation An object to access the JVM instrumentation mechanism.
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {

        System.out.println("Start Premain Agent to instrument a freshly started JVMs");
        ultimateInterceptor(agentArgs, instrumentation);
    }


    private static void ultimateInterceptor(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type((ElementMatchers.nameStartsWith(INCLUDE_PACKAGE_INSTRUMENT))
                              .and(ElementMatchers.not(ElementMatchers.nameStartsWith(EXCLUDE_PACKAGE_INSTRUMENT))))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.any())
                        .intercept(Advice.to(MethodAdvice.class))
                        .method(named("filter"))
                        .intercept(MethodDelegation.to(RequestFilter.class))
                ).installOn(instrumentation);
    }
}
