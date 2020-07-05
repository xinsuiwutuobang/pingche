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
import com.yf.pingche.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
@RequestMapping("/info")
public class InfoController {
    @Autowired
    private IInfoService iInfoService;
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
        IPage<Info> page = iInfoService.page(new Page<>(current == null ? 1 : current, size == null ? 20 : size), wrapper);
        List<InfoPo> infoPos = new ArrayList<>();
        page.getRecords().forEach(i -> {
            InfoPo infoPo = new InfoPo();
            BeanUtils.copyProperties(i, infoPo);
            User user = iUserService.getById(i.getUid());
            if (user != null) {
                infoPo.setAvatarUrl(user.getAvatarUrl());
            }
            infoPo.setDateString(DateUtil.getDateStr(infoPo.getDate(), DateUtil.YYYY_MM_DD));
            infoPo.setTimeString(DateUtil.getDateStr(infoPo.getTime(), DateUtil.HHMM));
            infoPos.add(infoPo);
        });
        IPage<InfoPo> ret = new Page<>();
        BeanUtils.copyProperties(page, ret);
        ret.setRecords(infoPos);
        return ApiResult.ok(ret);
    }

    @PostMapping("/index")
    public Object index(Long id) {
        Info ret = iInfoService.getById(id);
        return ApiResult.ok(ret);
    }
    /**
     * 添加、修改
     * 人找车、车找人
     * @param info
     * @return
     */
    @PostMapping("/add")
    public Object add(Info info) {
        info.setAddtime(new Date());
        boolean ret = iInfoService.saveOrUpdate(info);
        return ApiResult.ok(info.getId());
    }

    @GetMapping("/test")
    public Object test(Date date) {
        return date;
    }

    public static void main(String[] args) throws ParseException {
        String s = "15:00";
        Integer hhssSecond = DateUtil.getHHSSSecond(s);
        System.out.println("hhsssecond:" + hhssSecond);
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = simpleDateFormat.parse(s);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR,Integer.parseInt(s.substring(0,2)));
        now.set(Calendar.MINUTE,Integer.valueOf(s.substring(3)));
        now.set(Calendar.SECOND,0);
        long ts = date.getTime();
        res = String.valueOf(ts);
        System.out.println(res);
        System.out.println(now.getTime().getTime());
    }
}

