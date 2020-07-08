package com.yf.pingche.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yf.pingche.entity.Appointment;
import com.yf.pingche.entity.Info;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.model.po.AppointmentPo;
import com.yf.pingche.service.IAppointmentService;
import com.yf.pingche.service.IInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private IAppointmentService iAppointmentService;
    @Autowired
    private IInfoService infoService;


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
        //TODO 发送模板消息给司机
        return ApiResult.ok("预约成功");
    }

    @PostMapping("/my")
    public Object my(Long uid) {
        List<Appointment> appointments = iAppointmentService.list(Wrappers.<Appointment>lambdaQuery().eq(Appointment::getUid, uid).orderByDesc(Appointment::getTime));
        return ApiResult.ok(appointments);
    }

    @PostMapping("/mycount")
    public Object mycount(Long uid) {
        int count = iAppointmentService.count(Wrappers.<Appointment>lambdaQuery().eq(Appointment::getUid, uid).eq(Appointment::getStatus, 0));
        return ApiResult.ok(count);
    }

    @PostMapping("/getPassenger")
    public Object getPassenger(Long uid) {
        return null;
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
    public Object submit(Long aid,Integer uid,Integer status,String sk,String form_id) {
        Appointment appointment = iAppointmentService.getById(aid);
        appointment.setStatus(status);
        appointment.setTime(new Date());
        boolean ret = iAppointmentService.updateById(appointment);
        return ApiResult.ok(ret);
    }
}

