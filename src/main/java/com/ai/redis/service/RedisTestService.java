package com.ai.redis.service;

public interface RedisTestService {
    String test(String serviceName);

    Long runLua();
}

