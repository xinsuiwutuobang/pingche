package com.yf.pingche.service.impl;

import com.yf.pingche.entity.Comment;
import com.yf.pingche.mapper.CommentMapper;
import com.yf.pingche.service.ICommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
