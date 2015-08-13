package com.ai.redis.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.redis.util.RedisSentinelUtils;

@Controller
public class LinkController {

    @Resource private RedisSentinelUtils      redisSentinelUtils;
    
	@RequestMapping("/re/{path}")
	public String t(@PathVariable String path) {
	    System.out.println(redisSentinelUtils.get("name"));
	    redisSentinelUtils.set("key_1", "chinesebillboy");
	    System.out.println(redisSentinelUtils.get("key_1"));
	    return null;
	}
}
