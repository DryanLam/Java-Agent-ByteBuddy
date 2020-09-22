package com.sample.agent.advices;

import com.sample.agent.utils.DataCache;
import net.bytebuddy.asm.Advice;

public class MethodAdvice {


    @Advice.OnMethodEnter
    static long enterMethods() throws Exception {
        long start = System.currentTimeMillis();
        return start;
    }


    @Advice.OnMethodExit
    static void exitMethods(@Advice.Origin String method, @Advice.Enter long start) throws Exception {
        // Calculate execution time of each method
        long end = System.currentTimeMillis();

        // Method name like Class.method
        String st = DataCache.getInstance().getStatus();
        String mName = nameParser(method);
        System.out.println("Exit method: " + mName);

        System.out.println("This is Method :" + mName + "took " + (end-start) + " milliseconds");

        System.out.println("Caching status: " + st);
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