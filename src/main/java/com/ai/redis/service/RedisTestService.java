package com.ai.redis.service;

import java.util.List;

public interface RedisTestService {

    String test(String serviceName);

    Long runLua();

    List<String> testRedisClusterKeys();

    String reloadProperties();
}

