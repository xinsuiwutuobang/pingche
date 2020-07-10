package com.yf.pingche.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@Data
@Builder
public class WXMessage {
    private String touser;//openid
    private String template_id;//订阅消息模板id
    private String page = "pages/index/index";//点击消息跳转路径，默认跳到首页
    private Map<String,TemplateData> data;//推送文字

}
