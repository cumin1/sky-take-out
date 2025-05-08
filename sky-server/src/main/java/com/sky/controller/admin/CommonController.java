package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private AliOssUtil ossUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传: {}",file);
        try {
            String originalFilename = file.getOriginalFilename(); // 原始文件名
            // 截取原始文件名的后缀 jpg / png
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + suffix;  // 新文件名称
            String path = ossUtil.upload(file.getBytes(), objectName);
            return Result.success(path);
        }catch (Exception e){
            log.info("文件上传失败：{}",e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
