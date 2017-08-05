package com.ai.redis.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

@RestController
public class LinkController {

    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);
	/*
    @Resource private RedisSentinelUtils      redisSentinelUtils;
    
	@RequestMapping("/re/{path}")
	public String t(@PathVariable String path) {
	    System.out.println(redisSentinelUtils.get("name"));
	    redisSentinelUtils.set("key_1", "chinesebillboy");
	    System.out.println(redisSentinelUtils.get("key_1"));
	    return null;
	}
	*/

    @Resource
    private JedisCluster jedisCluster;

    @GetMapping(value = "re/RedisCluster")
    public String testRedisCluster() {
        String result = StringUtils.EMPTY;
        try {
            jedisCluster.setex("aa", 10, "liuhongb");
            result = jedisCluster.get("aa");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return result;
    }
}
