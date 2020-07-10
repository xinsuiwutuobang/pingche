package com.yf.pingche.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yf.pingche.constant.BaseConstant;
import com.yf.pingche.entity.Appointment;
import com.yf.pingche.entity.Info;
import com.yf.pingche.entity.Msg;
import com.yf.pingche.entity.User;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.model.po.AppointmentPo;
import com.yf.pingche.service.IAppointmentService;
import com.yf.pingche.service.IInfoService;
import com.yf.pingche.service.IMsgService;
import com.yf.pingche.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
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
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private IAppointmentService iAppointmentService;
    @Autowired
    private IInfoService infoService;

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IMsgService iMsgService;

    @PostMapping("/add")
    public Object add(Long uid, Long iid, String name, String phone, Integer surplus) {
        Appointment check = iAppointmentService.getOne(Wrappers.<Appointment>lambdaQuery().eq(Appointment::getIid, iid).eq(Appointment::getUid, uid));
        if (check != null) {
            return ApiResult.fail("请不要重复预约");
        }
        Appointment entity = new Appointment();
        //TODO 状态
        Appointment appointment = entity.setIid(iid).setUid(uid).setName(name).setPhone(phone).setStatus(0).setSurplus(surplus).setTime(new Date());
        iAppointmentService.save(appointment);
        //msg
        User user = iUserService.getById(uid);
        Info info = infoService.getById(iid);
        Msg msg = new Msg().setSee(BaseConstant.NO_ZERO).setTime(new Date())
                .setContent(user.getNickName() + "预约了您发布的拼车信息,请及时处理")
                .setFid(appointment.getUid()).setUid(info.getUid()).setType("notice").setUid(uid)
                .setUrl(BaseConstant.NOTICE_APPOINTMENT + info.getUid());
        iMsgService.save(msg);
        //TODO 发送模板消息给司机
        return ApiResult.ok("预约成功");
    }

    /**
     * 我的預約
     * @param uid
     * @param type 1.车找人，2:人找车
     * @return
     */
    @PostMapping("/my")
    public Object my(Long uid,Integer type) {
        QueryWrapper<Info> wrapper = new QueryWrapper<>();
        QueryWrapper<Appointment> appointmentQueryWrapper = new QueryWrapper<>();
        //我是车主，我的预约；我是乘客，我的预约。info都是车找人才能预约，别人预约我的车找人，我预约别人的车找人
        wrapper.lambda().eq(Info::getType, 1);
        if (type.equals(1)) {
            wrapper.lambda().eq(Info::getUid,uid);
        } else {
            appointmentQueryWrapper.lambda().eq(Appointment::getUid, uid);
        }
        List<Info> list = infoService.list(wrapper);
        if (list.size() > 0) {
            List<Long> infoIds = list.stream().map(i -> i.getId()).collect(Collectors.toList());
            appointmentQueryWrapper.lambda().in(Appointment::getIid, infoIds).orderByDesc(Appointment::getTime);
            List<Appointment> ret = iAppointmentService
                    .list(appointmentQueryWrapper);
            return ApiResult.ok(ret);
        }
        return ApiResult.ok(null);
    }

    @PostMapping("/mycount")
    public Object mycount(Long uid) {
        int count = iAppointmentService.count(Wrappers.<Appointment>lambdaQuery().eq(Appointment::getUid, uid).eq(Appointment::getStatus, 0));
        return ApiResult.ok(count);
    }

    @PostMapping("/detail")
    public Object detail(Long id) {
        Appointment appointment = iAppointmentService.getById(id);
        Info info = infoService.getById(appointment.getIid());
        AppointmentPo ret = new AppointmentPo();
        BeanUtils.copyProperties(appointment, ret);
        ret.setDeparture(info.getDeparture());
        ret.setDestination(info.getDestination());
        ret.setTime(info.getTime());
        return ApiResult.ok(ret);
    }

    /**
     * 同意、拒绝平车
     * 0:新建，1：同意，2：拒绝
     * @param aid
     * @param uid
     * @param status
     * @param sk
     * @param form_id
     * @return
     */
    @RequestMapping("/submit")
    public Object submit(Long aid,Long uid,Integer status,String sk,String form_id) {
        Appointment appointment = iAppointmentService.getById(aid);
        appointment.setStatus(status);
        appointment.setTime(new Date());
        boolean ret = iAppointmentService.updateById(appointment);
        //msg
        User user = iUserService.getById(uid);
        Info info = infoService.getById(appointment.getIid());
        Msg msg = new Msg().setSee(BaseConstant.NO_ZERO).setTime(new Date())
                .setContent(user.getNickName() + "拒绝了您的拼车请求,原因是")
                .setFid(uid).setUid(info.getUid()).setType("notice").setUid(appointment.getUid())
                .setUrl(BaseConstant.NOTICE_INFO + info.getUid());
        iMsgService.save(msg);
        return ApiResult.ok(ret);
    }
}

