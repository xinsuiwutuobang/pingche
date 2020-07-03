package com.yf.pingche.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangfei
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @RequestMapping("/submit")
    public Object submit(Integer id,String sk,Integer type,String form_id) {

        return null;
    }
}

