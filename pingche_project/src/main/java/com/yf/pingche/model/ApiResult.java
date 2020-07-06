package com.yf.pingche.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = -6950326556578536228L;

    private int code;

    private String msg;

    private T data;

    private static int SUCCESS = 200;

    private static int FAIL = 500;

    public static <T> ApiResult<T> ok(T data){
        return result(SUCCESS,data);
    }

    public static <T> ApiResult<T> ok(T data,String message){
        return result(SUCCESS,message,data);
    }
    public static <T> ApiResult<T> result(int apiCode,T data){
        return result(apiCode,null,data);
    }

    public static <T> ApiResult<T> result(int apiCode,String message,T data){
        return (ApiResult<T>) ApiResult.builder()
                .code(apiCode)
                .msg(message)
                .data(data)
                .build();
    }


    public static ApiResult<Boolean> fail(int apiCode){
        return result(apiCode,null);
    }

    public static ApiResult<String> fail(String message){
        return result(FAIL,message,null);

    }

    public static <T> ApiResult<T> fail(int apiCode,T data){
        return result(apiCode,data);

    }
    public static <T>  ApiResult<T> fail(Integer errorCode,String message){
        return (ApiResult<T>) ApiResult.builder()
                .code(errorCode)
                .msg(message)
                .build();
    }
}
