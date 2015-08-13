/**
 * 
 */
package com.ai.redis.web.controller;

import redis.clients.jedis.JedisPoolConfig;

import com.ai.redis.util.RedisUtils;
import com.ai.redis.util.ShardedJedisPipelineUtils;

/**
 * Class Name		: TestRedisUtil<br>
 * 
 * Description		: 这里记述class说明<br>
 * 
 * @author liuhb
 * @version $Revision$
 * @see
 *
 */
public class TestRedisUtil {

    public static void main(String[] args) {
        String uri = "127.0.0.1:6379";
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        RedisUtils redisUtils = new RedisUtils(uri, jedisPoolConfig);
        //redisUtils.set("key", "liu");
        ShardedJedisPipelineUtils pipelineUtils = redisUtils.buildPipelineUtils();
        for(int i = 1; i < 100; i ++){
            pipelineUtils.del("INDEX_"+i);
        }
        pipelineUtils.sync();
    }
}
