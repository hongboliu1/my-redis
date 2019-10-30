package com.ai.redis.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ai.redis.service.RedisTestService;

/**
 * @author liuhb
 * @date 2019-10-30 11:27
 */
@Service("redisTestService")
public class RedisTestServiceImpl implements RedisTestService {
    private static final Logger logger = LoggerFactory.getLogger(RedisTestService.class);
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String test(String serviceName) {
        redisTemplate.opsForValue().set("aa","china");
        String aa = redisTemplate.opsForValue().get("aa");
        if (logger.isInfoEnabled()) {
            logger.info("aa value is {} .",aa);
        }
        return serviceName.concat(" IS RUNNING! ")
                .concat(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
