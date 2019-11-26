package com.ai.redis.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ai.redis.model.LocalDateDemoVO;
import com.ai.redis.service.RedisTestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/redis")
@Api(value = "/api/redis", tags = "redis-test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private RedisTestService redisTestService;

    @GetMapping("/test")
    @ApiOperation(value = "redis服务测试")
    public String testRedisCluster() {
        return redisTestService.test("my-redis");
    }

    @GetMapping("/testKeys")
    @ApiOperation(value = "redis keys服务测试")
    public List<String> testRedisClusterKeys() {
        return redisTestService.testRedisClusterKeys();
    }

    @PutMapping("/runLua")
    @ApiOperation(value = "redis服务测试")
    public Long testRunLua() {
        return redisTestService.runLua();
    }

    @GetMapping(value = "/reloadProperties")
    @ApiOperation(value = "自动重新获取配置")
    public String reloadProperties() {
        return redisTestService.reloadProperties();
    }

    @PutMapping("/testDate")
    @ApiOperation(value = "时间转换测试")
    public LocalDateDemoVO testDate(@RequestBody(required = false) LocalDateDemoVO vo) {
        return redisTestService.returnHello(vo);
    }
}
