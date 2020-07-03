package com.yf.pingche.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yf.pingche.entity.User;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.model.WXSessionModel;
import com.yf.pingche.service.IUserService;
import com.yf.pingche.utils.HttpClientUtil;
import com.yf.pingche.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@RequestMapping("/user")
@RestController
public class LoginController {
    @Value("${wx.appId}")
    private String wxAppId;

    @Value("${wx.secret}")
    private String wxSecret;

    @Autowired
    private IUserService iUserService;

    @PostMapping("/login")
    public Object login(String code,User user) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<String, String>();
        param.put("appid", wxAppId);
        param.put("secret", wxSecret);
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        String wxResult = HttpClientUtil.doGet(url, param);
        WXSessionModel wxSessionModel = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
        User check = iUserService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getOpenId, wxSessionModel.getOpenid()));
        user.setOpenId(wxSessionModel.getOpenid());
        if (check == null) {
            //新用户
            user.setName(user.getNickName());
            boolean save = iUserService.save(user);
        } else {
            //老用户
            check.setOpenId(wxSessionModel.getOpenid());
            boolean b = iUserService.updateById(check);

        }
        String openid = wxSessionModel.getOpenid();
        return ApiResult.ok(check == null ? user : check);
    }
}
