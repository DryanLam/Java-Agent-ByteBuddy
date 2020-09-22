package com.sample.agent;

import com.sample.agent.advices.MethodAddition;
import com.sample.agent.advices.MethodAdvice;
import com.sample.agent.advices.RequestFilter;
import com.sample.agent.advices.TcAdvice;
import com.sample.dl.controller.BaseController;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
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
        testAddMethodWS(agentArgs, instrumentation);
    }


    /**
     * If the agent is attached to an already running JVM,
     * this method is invoked.
     *
     * @param agentArgs       Agent command line arguments.
     * @param instrumentation An object to access the JVM instrumentation mechanism.
     */
    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("Start Main Agent to instrument a running JVM!");
        testAddMethodWS(agentArgs, instrumentation);
    }


    private static void myIntercept(String arguments, Instrumentation instrumentation) {
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


    private static void oldIntercept(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type((ElementMatchers.nameStartsWith(INCLUDE_PACKAGE_INSTRUMENT))
                              .and(ElementMatchers.not(ElementMatchers.nameStartsWith(EXCLUDE_PACKAGE_INSTRUMENT))))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.any())
                        .intercept(Advice.to(MethodAdvice.class))
                        .defineMethod("methodAdding", void.class, Visibility.PUBLIC)
                        .intercept(MethodDelegation.to(MethodAddition.class))
                        .method(ElementMatchers.nameContains("method2"))
                        .intercept(SuperMethodCall.INSTANCE
                                           .andThen(MethodCall.invoke(ElementMatchers.nameContains("methodAdding"))))
                ).installOn(instrumentation);
    }


    private static void testAddMethodWS(String arguments, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type((ElementMatchers.nameStartsWith(INCLUDE_PACKAGE_INSTRUMENT))
                              .and(ElementMatchers.not(ElementMatchers.nameStartsWith(EXCLUDE_PACKAGE_INSTRUMENT))))
                .transform((builder, typeDescription, classLoader, module) -> builder
                        .method(ElementMatchers.any())
                        .intercept(Advice.to(MethodAdvice.class))
                        .visit(Advice.to(BaseController.class).on(ElementMatchers.nameStartsWith("BaseController")))
                ).installOn(instrumentation);
    }
}
