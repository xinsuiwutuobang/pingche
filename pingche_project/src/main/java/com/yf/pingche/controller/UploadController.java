package com.yf.pingche.controller;

import com.yf.pingche.model.ApiResult;
import com.yf.pingche.utils.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@RestController
@Slf4j
@RequestMapping("/up")
public class UploadController {
    @Value("${upload-path}")
    private String uploadPath;
    @Value("${resource-access-path}")
    private String resourceAccessPath;
    @RequestMapping("/upload")
    public Object upload(MultipartFile file,String user) throws Exception {
        // 上传文件，返回保存的文件名称
        String saveFileName = UploadUtil
                .upload(uploadPath, file, originalFilename -> {
            // 文件后缀
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            // 这里可自定义文件名称，比如按照业务类型/文件格式/日期
            String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssS")) + RandomStringUtils
                    .randomNumeric(6);
            String fileName = dateString + "." + fileExtension;
            return fileName;
        });

        // 上传成功之后，返回访问路径，请根据实际情况设置
        String fileAccessPath = resourceAccessPath + saveFileName;
        log.info("fileAccessPath:{}", fileAccessPath);
        return ApiResult.ok(fileAccessPath);
    }
}
