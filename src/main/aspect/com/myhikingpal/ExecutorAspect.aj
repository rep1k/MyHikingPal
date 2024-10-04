package com.myhikingpal;

public aspect ExecutorAspect {
    pointcut executeRunnable(): execution(* java.lang.Runnable.run());

    before(): executeRunnable() {
        System.out.println("Trying to get this work");
        System.out.println("Before running Runnable.run()");
    }

    after(): executeRunnable() {
        System.out.println("After running Runnable.run()");
    }
}
