package com.yf.pingche.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yf.pingche.entity.User;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.model.TemplateData;
import com.yf.pingche.model.WXMessage;
import com.yf.pingche.model.WXSessionModel;
import com.yf.pingche.service.IUserService;
import com.yf.pingche.utils.DateUtil;
import com.yf.pingche.utils.JsonUtils;
import com.yf.pingche.utils.WXMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
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
@Slf4j
@RequestMapping("/user")
@RestController
public class LoginController {
    @Value("${wx.appId}")
    private String wxAppId;

    @Value("${wx.secret}")
    private String wxSecret;

    @Value("${wx.message.reply.templateid}")
    private String templateid;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private RestTemplate restTemplate;


    /**
     * 登录
     * @param code
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Object login(String code,User user) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<String, String>();
        param.put("appid", wxAppId);
        param.put("secret", wxSecret);
        param.put("js_code", code);
        param.put("grant_type", "authorization_code");
        String loginUrl =
                url + "?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}";
        String wxResult = restTemplate.getForObject(loginUrl, String.class, param);
       /* String wxResult = HttpClientUtil.doGet(url, param);*/
        log.info("wxResult:{}", wxResult);
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

    @RequestMapping("/sendMesssage")
    public Object sendMessage(String openid) {
        WXMessage message = WXMessage.builder().touser(openid).template_id(templateid).build();
        Map<String, TemplateData> params = new HashMap<>(3);
        params.put("thing1", new TemplateData("小程序入门课程"));
        params.put("thing2", new TemplateData("杭州浙江大学"));
        params.put("time3", new TemplateData(DateUtil.getDateStr(new Date(),DateUtil.YYYYMMDDHHMM)));
        message.setData(params);
        String push = WXMessageUtil.push(message);
        log.info(push);
        return push;
    }
}
