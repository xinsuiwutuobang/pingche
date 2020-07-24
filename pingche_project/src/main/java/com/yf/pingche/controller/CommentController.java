package com.yf.pingche.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.pingche.constant.BaseConstant;
import com.yf.pingche.entity.*;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.model.po.CommentPo;
import com.yf.pingche.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@Slf4j
public class CommentController {
    @Autowired
    private ICommentService iCommentService;

    @Autowired
    private IZanService iZanService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IInfoService iInfoService;

    @Autowired
    private IMsgService iMsgService;
    @PostMapping("/get_count")
    public Object getCount(Long iid) {
        int count = iCommentService.count(Wrappers.<Comment>lambdaQuery().eq(Comment::getIid, iid));
        return ApiResult.ok(count);

    }

    @PostMapping("/get")
    public Object get(Long id, String type, Integer current) {
        IPage<Comment> ret = iCommentService.page(new Page<>(current, 10), Wrappers.<Comment>lambdaQuery().eq(Comment::getIid, id).eq(Comment::getType, type).orderByDesc(Comment::getTime));
        List<CommentPo> comments = new ArrayList<>();
        ret.getRecords().forEach(c -> {
            CommentPo po = new CommentPo();
            BeanUtils.copyProperties(c,po);
            User user = iUserService.getById(c.getUid());
            po.setAvatarUrl(user.getAvatarUrl());
            po.setNickName(user.getNickName());
            comments.add(po);
        });
        return ApiResult.ok(comments);
    }

    @PostMapping("/zan")
    @Transactional
    public Object zan(Long uid, Long cid) {
        int count = iZanService.count(Wrappers.<Zan>lambdaQuery().eq(Zan::getUid, uid).eq(Zan::getCid, cid));
        if (count == 0) {
            //赞
            Zan zan = new Zan();
            zan.setCid(cid).setUid(uid).setTime(new Date());
            iZanService.save(zan);
            //评论
            Comment comment = iCommentService.getById(zan.getCid());
            comment.setZan(comment.getZan() + 1).setTime(new Date());
            boolean ret = iCommentService.updateById(comment);
            //赞msg
            User user = iUserService.getById(uid);
            Msg msg = new Msg().setSee(BaseConstant.NO_ZERO).setTime(new Date())
                    .setContent(user.getNickName() + "赞了您的评论:" + comment.getContent())
                    .setFid(uid).setUid(comment.getUid()).setType("zan").setUid(uid)
                    .setUrl(BaseConstant.ZAN_COMMON_URL + comment.getIid());
            iMsgService.save(msg);
            //TODO 发送消息

            return ApiResult.ok(ret, "点赞成功");
        } else {
            return ApiResult.fail("你已经赞过了");
        }
    }

    @PostMapping("/add")
    public Object add(Comment comment) {
        comment.setZan(0).setTime(new Date());
        boolean ret = iCommentService.save(comment);
        //发布评论消息
        Info info = iInfoService.getById(comment.getIid());
        User user = iUserService.getById(comment.getUid());
        log.info("{}",comment.getIid());
        log.info(BaseConstant.COMMENT_INFO_URL + comment.getIid());
       /* new Msg(null, comment.getUid(), user.getNickName() + "评论了您的信息:" + comment.getContent(),
                new Date(), BaseConstant.NO_ZERO, "comment",
                BaseConstant.COMMENT_INFO_URL + comment.getIid(), null);*/
        Msg msg = new Msg().setSee(BaseConstant.NO_ZERO).setTime(new Date())
                .setContent(user.getNickName()+ "评论了您的信息:" + comment.getContent())
                .setType("comment").setUid(info.getUid()).setFid(comment.getId())
                .setUrl(BaseConstant.COMMENT_INFO_URL + comment.getIid());
        if (comment.getType().equals("dynamic")) {
            msg.setUrl(BaseConstant.COMMENT_DYNAMIC_URL + comment.getIid());
            msg.setContent(user.getNickName()+ "评论了您的动态:" + comment.getContent());
        }
        iMsgService.save(msg);
        return ApiResult.ok(ret);
    }
}

