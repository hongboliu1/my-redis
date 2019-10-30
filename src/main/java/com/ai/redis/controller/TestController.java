package com.ai.redis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.redis.service.RedisTestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/redis")
@Api(value = "/api/redis", tags = "redis-test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired private RedisTestService redisTestService;

    @GetMapping("/test")
    @ApiOperation(value = "redis服务测试")
    public String testRedisCluster() {
        return redisTestService.test("my-redis");
    }
}
