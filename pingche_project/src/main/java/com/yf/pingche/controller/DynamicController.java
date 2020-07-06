package com.yf.pingche.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.pingche.constant.BaseConstant;
import com.yf.pingche.entity.Dynamic;
import com.yf.pingche.entity.User;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.model.po.DynamicPo;
import com.yf.pingche.service.IDynamicService;
import com.yf.pingche.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangfei
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/dynamic")
public class DynamicController {
    @Autowired
    private IDynamicService iDynamicService;

    @Autowired
    private IUserService iUserService;
    @PostMapping("/getlist")
    public Object getlist(Long uid, Integer current) {
        IPage<Dynamic> page = iDynamicService.page(new Page<>(current, BaseConstant.SIZE),
                Wrappers.<Dynamic>lambdaQuery().eq(Dynamic::getUid, uid)
                        .orderByDesc(Dynamic::getTime));
        List<DynamicPo> records = new ArrayList<>();
        page.getRecords().forEach(d -> {
            User user = iUserService.getById(d.getUid());
            DynamicPo po = new DynamicPo();
            BeanUtils.copyProperties(d, po);
            po.setAvatarUrl(user.getAvatarUrl());
            po.setNickName(user.getNickName());
            records.add(po);
        });
        return ApiResult.ok(records);
    }

    @PostMapping("/add")
    public Object add(Dynamic entity) {
        entity.setTime(new Date());
        boolean ret = iDynamicService.save(entity);
        return ApiResult.ok(ret);
    }
}

