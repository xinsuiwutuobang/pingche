package com.yf.pingche.service.impl;

import com.yf.pingche.entity.Msg;
import com.yf.pingche.mapper.MsgMapper;
import com.yf.pingche.service.IMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangfei
 * @since 2020-07-03
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements IMsgService {

}
