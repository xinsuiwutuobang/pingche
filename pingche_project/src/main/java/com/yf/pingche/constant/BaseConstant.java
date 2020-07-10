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

    public static Integer SIZE = 10;

    public static Integer YES_ONE = 1;

    public static Integer NO_ZERO = 0;

    //赞
    public static String ZAN_COMMON_URL = "/pages/info/index?id=";
    //信息评论，评论回复
    public static String COMMENT_INFO_URL = "/pages/info/index?id=";
    //动态回复
    public static String COMMENT_DYNAMIC_URL = "/pages/dynamic/index?id=";
    //预约同意
    public static String NOTICE_APPOINTMENT = "/pages/appointment/index?id=";
    //预约拒绝
    public static String NOTICE_INFO = "/pages/info/index?id=";

    public static void main(String[] args) {
        int localVar = 2;
        while (!atomicInteger.compareAndSet(localVar, localVar + 1)) {
            int ret = atomicInteger.get();
            System.out.println(ret);
        }
    }
}
