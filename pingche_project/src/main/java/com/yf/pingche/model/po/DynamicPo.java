package com.yf.pingche.model.po;

import com.yf.pingche.entity.Comment;
import com.yf.pingche.entity.Dynamic;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@Data
public class DynamicPo extends Dynamic {
    private String avatarUrl;
    private String nickName;
    private List<CommentPo> comment;
}
