package com.yf.pingche.model.po;

import com.yf.pingche.entity.Comment;
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
public class CommentPo extends Comment {
    private String avatarUrl;
    private String nickName;
}
