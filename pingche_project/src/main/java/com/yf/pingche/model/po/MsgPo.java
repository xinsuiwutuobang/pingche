package com.yf.pingche.model.po;

import com.yf.pingche.entity.Msg;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@Data
public class MsgPo extends Msg {
    private String avatarUrl;
    private String nickName;
}
