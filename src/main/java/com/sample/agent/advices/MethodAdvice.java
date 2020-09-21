package com.sample.agent.advices;

import com.sample.agent.utils.DataCache;
import net.bytebuddy.asm.Advice;

public class MethodAdvice {

    @Advice.OnMethodEnter
    static void enterMethods(@Advice.Origin String method) throws Exception {
    }

    @Advice.OnMethodExit
    static void exitMethods(@Advice.Origin String method) throws Exception {
        String st = DataCache.getInstance().getStatus();
        String mName = nameParser(method);
        System.out.println("Exit method: " + mName);
        System.out.println(st);
        if ("Caching".equalsIgnoreCase(st)) {
            DataCache.getInstance().addData("method", mName);
        }
    }

    public static String nameParser(String methodCaught){
        String mf = methodCaught.split("\\(")[0];
        String[] lstName = mf.split("\\.");
        String m = lstName[lstName.length - 2] + "." + lstName[lstName.length - 1];
        return m;
    }
}