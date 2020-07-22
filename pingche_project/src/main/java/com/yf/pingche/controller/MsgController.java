package com.yf.pingche.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.pingche.constant.BaseConstant;
import com.yf.pingche.entity.Msg;
import com.yf.pingche.entity.User;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.model.po.MsgPo;
import com.yf.pingche.service.IMsgService;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yangfei
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/msg")
public class MsgController {
    @Autowired
    private IMsgService iMsgService;
    @Autowired
    private IUserService iUserService;

    /**
     * 获取某类型的消息
     * @param uid
     * @param type
     * @param current
     * @return
     */
    @RequestMapping("/get")
    public Object get(Long uid,String type, Integer current) {
        IPage<Msg> page = iMsgService.page(new Page<>(current, BaseConstant.SIZE),
                Wrappers.<Msg>lambdaQuery().eq(Msg::getUid, uid).eq(Msg::getType,type).orderByDesc(Msg::getTime));
        List<MsgPo> ret = new ArrayList<>();
        page.getRecords().forEach(m -> {
            MsgPo po = new MsgPo();
            BeanUtils.copyProperties(m, po);
            User user = iUserService.getById(m.getUid());
            po.setAvatarUrl(user.getAvatarUrl());
            po.setNickName(user.getNickName());
            ret.add(po);
            m.setSee(BaseConstant.YES_ONE);
            m.setTime(new Date());
            iMsgService.updateById(m);
        });
        return ApiResult.ok(ret);
    }

    @PostMapping("/getall")
    public Object getAll(Long uid) {
        List<Msg> list = iMsgService
                .list(Wrappers.<Msg>lambdaQuery().eq(Msg::getUid, uid).eq(Msg::getSee,BaseConstant.NO_ZERO).orderByDesc(Msg::getTime));
        Map<String, List<Msg>> ret = list.stream().collect(Collectors.groupingBy(Msg::getType));
        return ApiResult.ok(ret);
    }
}

