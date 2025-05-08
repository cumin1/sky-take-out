package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类：用于创建AliOssUtil对象
 */
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties prop){
        log.info("开始创建AliOss工具类对象: {}",prop);
        AliOssUtil aliOssUtil = new AliOssUtil(prop.getEndpoint(),
                                prop.getAccessKeyId(),
                                prop.getAccessKeySecret(),
                                prop.getBucketName());
        return aliOssUtil;
    }
}
