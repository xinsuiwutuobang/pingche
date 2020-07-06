package com.yf.pingche.controller;


import com.yf.pingche.entity.User;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @RequestMapping("/editUser")
    public Object editUser(User user) {
        boolean saveOrUpdate = iUserService.saveOrUpdate(user);
        User ret = iUserService.getById(user.getId());
        return ApiResult.ok(ret);
    }
}

