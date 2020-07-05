package com.yf.pingche.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

@ControllerAdvice
@Slf4j
public class ControllerHandler {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //字符串转时间
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(new DateConverter().convert(text));
            }
        });
        /*//字符串转Integer
        binder.registerCustomEditor(Integer.class,new PropertyEditorSupport(){

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(new TimeConverter().convert(text));
            }
        });*/

    }
}
