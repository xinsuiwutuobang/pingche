package com.yf.pingche.constant;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
public class BaseConstant {

    public static String USER_NAME_CACHE = "user:%d";

    public static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) {
        int localVar = 2;
        while (!atomicInteger.compareAndSet(localVar, localVar + 1)) {
            int ret = atomicInteger.get();
            System.out.println(ret);
        }
    }
}
