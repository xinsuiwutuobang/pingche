package com.yf.pingche.config;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.yf.pingche.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeConverter implements Converter<String, Integer> {
    private Logger logger = LoggerFactory.getLogger(TimeConverter.class);

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String dateHHmmFormat = "HH:mm";
    private static final String dateHHmmssFormat = "HH:mm:ss";
    @Override
    public Integer convert(String value) {
        logger.info("转换时间：" + value);
        if(value == null || value.trim().equals("") || value.equalsIgnoreCase("null")) {
           return null;
        }
        value = value.trim();
        try {
            if (value.contains(":")) {
                SimpleDateFormat formatter;
                if (value.length() == 5) {
                    return DateUtil.getHHSSSecond(value);
                }
                return null;
            }
            return Integer.valueOf(value);
        } catch (Exception e) {
            throw new RuntimeException(String.format("parser %s to second time fail", value));
        }
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }


}
