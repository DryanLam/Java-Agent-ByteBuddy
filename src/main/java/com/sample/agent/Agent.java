package com.sample.agent;

import com.sample.agent.advices.MethodAddition;
import com.sample.agent.advices.MethodAdvice;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.isMethod;

public class Agent {
    private static final String INCLUDE_PACKAGE_INSTRUMENT = "com.sample.dl";
    private static final String EXCLUDE_PACKAGE_INSTRUMENT = "com.sample.dl.context";

    public static void premain(String arguments, Instrumentation instrumentation) {

        System.out.println("Start Agent to get running methods");


        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
//                .type((ElementMatchers.any()))
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

}
