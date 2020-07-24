package com.yf.pingche.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.pingche.constant.BaseConstant;
import com.yf.pingche.entity.Notice;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangfei
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private INoticeService iNoticeService;

    /**
     * 列表
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/list")
    public Object list(Integer current,Integer size) {
        IPage<Notice> ret = iNoticeService
                .page(new Page<>(current == null ? 1 : current, size == null ? 20 : size),
                        Wrappers.<Notice>lambdaQuery().eq(Notice::getStatus, BaseConstant.YES_ONE)
                                .orderByDesc(Notice::getIsTop, Notice::getUpdateTime));
        return ApiResult.ok(ret.getRecords());
    }

    /**
     * 推荐内容
     * @param entity
     * @return
     */
    @PostMapping("/add")
    public Object add(Notice entity) {
        entity.setStatus(BaseConstant.NO_ZERO).setIsTop(BaseConstant.NO_ZERO).setUrl("")
                .setCreateTime(new Date()).setUpdateTime(new Date());
        iNoticeService.save(entity);
        entity.setUrl(BaseConstant.NOTICE_DETAIL + entity.getId());
        iNoticeService.updateById(entity);
        return ApiResult.ok(entity.getId());
    }

    @PostMapping("/top")
    public Object top(Long id) {
        Notice entity = iNoticeService.getById(id);
        entity.setId(id);
        entity.setIsTop(entity.getIsTop().equals(BaseConstant.NO_ZERO) ? BaseConstant.YES_ONE : BaseConstant.NO_ZERO);
        entity.setUpdateTime(new Date());
        iNoticeService.updateById(entity);
        return ApiResult.ok(entity);
    }

    @PostMapping("/up")
    public Object up(Long id) {
        Notice entity = iNoticeService.getById(id);
        entity.setId(id);
        entity.setStatus(entity.getIsTop().equals(BaseConstant.NO_ZERO) ? BaseConstant.YES_ONE : BaseConstant.NO_ZERO);
        entity.setUpdateTime(new Date());
        iNoticeService.updateById(entity);
        return ApiResult.ok(entity);
    }
    /**
     * 详情
     * @param id
     * @return
     */
    @PostMapping("/detail")
    public Object detail(Long id) {
        Notice entity = iNoticeService.getById(id);
        return ApiResult.ok(entity);
    }
}

