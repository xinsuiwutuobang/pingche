package com.yf.pingche.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@Controller
//@RequestMapping("/api/resource")
@Slf4j
public class ResourceController {
    @Value("${resource-access-path}")
    private String resourceAccessPath;
    @GetMapping("/image/{imageFileName}")
    public void getImage(@PathVariable(required = true) String imageFileName, HttpServletResponse response) throws Exception {
        log.info("imageFileName:{}", imageFileName);
        // 重定向到图片访问路径
        response.sendRedirect(resourceAccessPath + imageFileName);
    }
}
