package com.yf.pingche.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.pingche.entity.Comment;
import com.yf.pingche.entity.Zan;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.service.ICommentService;
import com.yf.pingche.service.IZanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 评论
 * 赞
 * </p>
 *
 * @author yangfei
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private ICommentService iCommentService;

    @Autowired
    private IZanService iZanService;

    @PostMapping("/get_count")
    public Object getCount(Long iid) {
        int count = iCommentService.count(Wrappers.<Comment>lambdaQuery().eq(Comment::getIid, iid));
        return ApiResult.ok(count);

    }

    @PostMapping("/get")
    public Object get(Long id, String type, Integer current) {
        IPage<Comment> ret = iCommentService.page(new Page<>(current, 10), Wrappers.<Comment>lambdaQuery().eq(Comment::getIid, id).eq(Comment::getType, type).orderByDesc(Comment::getTime));
        return ApiResult.ok(ret.getRecords());
    }

    @PostMapping("/zan")
    public Object zan(Long uid, Long cid) {
        int count = iZanService.count(Wrappers.<Zan>lambdaQuery().eq(Zan::getUid, uid).eq(Zan::getCid, cid));
        if (count == 0) {
            Zan zan = new Zan();
            zan.setCid(cid).setUid(uid).setTime(new Date());
            iZanService.save(zan);
            //TODO 发送消息

            return ApiResult.ok(count, "点赞成功");
        } else {
            return ApiResult.fail("你已经赞过了");
        }
    }
}

