package com.yf.pingche.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yf.pingche.entity.Fav;
import com.yf.pingche.entity.Info;
import com.yf.pingche.model.ApiResult;
import com.yf.pingche.service.IFavService;
import com.yf.pingche.service.IInfoService;
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
 *  收藏控制器
 * </p>
 *
 * @author yangfei
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/fav")
public class FavController {
    @Autowired
    private IFavService iFavService;

    @Autowired
    private IInfoService iInfoService;
    /**
     * 是否收藏
     * @param id
     * @return
     */
    @PostMapping("/isfav")
    public Object isFan(Long uid,Long iid) {
        Fav check = iFavService.getOne(Wrappers.<Fav>lambdaQuery().eq(Fav::getUid, uid).eq(Fav::getIid, iid));
        return ApiResult.ok(check != null ? true : false);
    }

    /**
     * 添加收藏
     * @param uid
     * @param iid
     * @return
     */
    @PostMapping("/addFav")
    public Object addFav(Long uid, Long iid) {
        Fav fav = new Fav();
        fav.setUid(uid);
        fav.setIid(iid);
        fav.setTime(new Date());
        iFavService.save(fav);
        return ApiResult.ok(fav.getId());
    }

    /**
     * 取消收藏
     * @param uid
     * @param iid
     * @return
     */
    @PostMapping("/delFav")
    public Object delFav(Long uid, Long iid) {
        boolean ret = iFavService.remove(Wrappers.<Fav>lambdaQuery().eq(Fav::getUid, uid).eq(Fav::getIid, iid));
        return ApiResult.ok(ret);
    }

    /**
     * 我的收藏
     * @param uid
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/myFav")
    public Object myFav(Long uid, Integer current, Integer size) {
        IPage<Info> pageInfo = new Page<>(current, size);
        IPage<Fav> pageFavs = iFavService.page(new Page<>(current, size), Wrappers.<Fav>lambdaQuery().eq(Fav::getUid, uid));
        List<Info> infos = new ArrayList<>();
        pageFavs.getRecords().forEach(f -> {
            Info info = iInfoService.getById(f.getIid());
            infos.add(info);
        });
        BeanUtils.copyProperties(pageFavs, pageInfo);
        pageInfo.setRecords(infos);
        return ApiResult.ok(pageInfo);
    }
}

