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

/**
 * ClassName: CommonController
 * Package: com.sky.controller.admin
 * Description:
 *
 * @Author Rainbow
 * @Create 2024/4/3 20:45
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "后台管理-通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public Result<String> upload(MultipartFile file){
        log.info("上传文件，文件名：{}", file.getOriginalFilename());

        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取原始文件名的后缀名（如果 originalFilename 是一个文件名，比如 "example.jpg"，那么 extension 将会是 ".jpg"）
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 生成新的文件名
            String objectName = UUID.randomUUID().toString() + extension;

            // 上传文件到阿里云 OSS，filePath 为上传成功后的文件路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("上传文件失败：{}", e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

}
