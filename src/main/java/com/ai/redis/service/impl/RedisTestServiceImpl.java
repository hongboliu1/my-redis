package com.ai.redis.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.ai.redis.service.RedisTestService;

/**
 * @author liuhb
 * @date 2019-10-30 11:27
 */
@Service("redisTestService")
public class RedisTestServiceImpl implements RedisTestService {
    @Override
    public String test(String serviceName) {
        return serviceName.concat(" IS RUNNING! ")
                .concat(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
