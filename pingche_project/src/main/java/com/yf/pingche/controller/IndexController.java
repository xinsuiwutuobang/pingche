package com.yf.pingche.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.pingche.entity.Info;
import com.yf.pingche.entity.User;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.model.po.InfoPo;
import com.yf.pingche.service.IInfoService;
import com.yf.pingche.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@RestController
@RequestMapping("/info")
public class IndexController {
    @Autowired
    private IInfoService infoService;
    @Autowired
    private IUserService iUserService;
    @PostMapping("/lists")
    public Object lists(String start, String over, String date,Integer current, Integer size ) {
        LambdaQueryWrapper<Info> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(start)) {
            wrapper.like(Info::getDeparture, start);
        }
        if (StringUtils.isNotBlank(over)) {
            wrapper.like(Info::getDestination, over);
        }
        if (StringUtils.isNotBlank(date)) {
            wrapper.eq(Info::getDate, date);
        }
        IPage<Info> page = infoService.page(new Page<>(current == null ? 1 : current, size == null ? 20 : size), wrapper);
        List<InfoPo> infoPos = new ArrayList<>();
        page.getRecords().forEach(i -> {
            InfoPo infoPo = new InfoPo();
            BeanUtils.copyProperties(i, infoPo);
            User user = iUserService.getById(i.getUid());
            infoPo.setAvatarUrl(user.getAvatarUrl());
            infoPos.add(infoPo);
        });
        IPage<InfoPo> ret = new Page<>();
        BeanUtils.copyProperties(page, ret);
        ret.setRecords(infoPos);
        return ApiResult.ok(ret);
    }
}
