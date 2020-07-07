package com.yf.pingche.model.po;

import com.yf.pingche.entity.Appointment;
import lombok.Data;

@Data
public class AppointmentPo extends Appointment {
    private String departure;

    private String destination;
}
