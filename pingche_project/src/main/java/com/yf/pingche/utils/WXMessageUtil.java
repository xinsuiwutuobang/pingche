package com.yf.pingche.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yf.pingche.model.WXMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 *
 * ?access_token={access_token}&touser={touser}&template_id={template_id}&data={data}
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@Component
@Slf4j
public class WXMessageUtil {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${wx.message.reply.messageurl}")
    private String messageurl;

    @Value("${wx.appId}")
    private String wxAppId;

    @Value("${wx.secret}")
    private String wxSecret;

    @Value(("${wx.getAccessTokenUrl}"))
    private String getAccessTokenUrl;

    private static WXMessageUtil wxMessageUtil;

    @PostConstruct
    public void init() {
        wxMessageUtil = this;
    }

    /**
     * 发送订阅模板消息
     * @param message
     * @return
     */
    public static String push(WXMessage message) {
        ResponseEntity<String> responseEntity = WXMessageUtil.wxMessageUtil.restTemplate
                .postForEntity(WXMessageUtil.wxMessageUtil.messageurl + "?access_token=" + getAccessToken(), message, String.class);
        return responseEntity.getBody();
    }

    public static String getAccessToken() {
        Map<String,String> params = new HashMap<>();
        params.put("APPID", WXMessageUtil.wxMessageUtil.wxAppId);
        params.put("APPSECRET", WXMessageUtil.wxMessageUtil.wxSecret);
        ResponseEntity<String> getAccessToken = WXMessageUtil.wxMessageUtil.restTemplate
                .getForEntity(WXMessageUtil.wxMessageUtil.getAccessTokenUrl, String.class, params);
        String body = getAccessToken.getBody();
        JSONObject object = JSON.parseObject(body);
        String access_token = object.getString("access_token");
        String expires_in = object.getString("expires_in");
        log.info("access_token:{},过期时间：{}", access_token, expires_in);
        return access_token;
    }
}
