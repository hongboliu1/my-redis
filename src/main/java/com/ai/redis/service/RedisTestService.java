package com.ai.redis.service;

import java.util.List;

import com.ai.redis.model.LocalDateDemoVO;

public interface RedisTestService {

    String test(String serviceName);

    Long runLua();

    List<String> testRedisClusterKeys();

    String reloadProperties();

    LocalDateDemoVO returnHello(LocalDateDemoVO req);
}

