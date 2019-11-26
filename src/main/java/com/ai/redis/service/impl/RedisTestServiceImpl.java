package com.ai.redis.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import com.ai.redis.model.LocalDateDemoVO;
import com.ai.redis.service.RedisTestService;

/**
 * @author liuhb
 * @date 2019-10-30 11:27
 */
@Service("redisTestService")
public class RedisTestServiceImpl implements RedisTestService {

    private static final Logger logger = LoggerFactory.getLogger(RedisTestService.class);
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Value("${name}")
    private String name;

    @Override
    public List<String> testRedisClusterKeys() {
        List<String> result = Collections.emptyList();
        Set<String> keys = redisTemplate.keys("11_*");
        if (CollectionUtils.isNotEmpty(keys)) {
            result = new ArrayList<>(keys.size());
            for (String key : keys) {
                result.add(redisTemplate.opsForValue().get(key));
            }
        }
        return result;
    }

    @Override
    public String test(String serviceName) {
        redisTemplate.opsForValue().set("aa", "china");
        String aa = redisTemplate.opsForValue().get("aa");
        if (logger.isInfoEnabled()) {
            logger.info("aa value is {} .", aa);
        }
        redisTemplate.delete("aa");
        return serviceName.concat(" IS RUNNING! ")
                .concat(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public Long runLua() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("test.lua")));

        // 在集群环境下使用lua设置key，必须使用{}，redis 使用key进行hash时，只使用{}中的值
        List<String> keys = new ArrayList<>(2);
        keys.add("{1111}:name");
        keys.add("{1111}:age");

        return redisTemplate.execute(redisScript, keys, "liuhongbo", "98");
    }

    @Override
    public String reloadProperties() {
        return name;
    }

    @Override
    public LocalDateDemoVO returnHello(LocalDateDemoVO req) {
        LocalDateDemoVO vo = new LocalDateDemoVO();
        vo.setNow(LocalDateTime.now());
        vo.setDate(new Date());
        vo.setDay(LocalDate.now());
        vo.setInputDate(req.getInputDate());
        vo.setInputDays(req.getInputDays());
        vo.setInputLocalDate(req.getInputLocalDate());
        return vo;
    }
}
