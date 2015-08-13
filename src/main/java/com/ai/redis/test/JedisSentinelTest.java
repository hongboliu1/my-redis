/**
 * 
 */
package com.ai.redis.test;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinelTest {

    public static void main(String[] args) {
        Set<String> sentinels = new HashSet<String>();
        //sentinels.add("192.168.99.254:26379");
        // 先关闭防火墙，再启动服务
        sentinels.add(new HostAndPort("192.168.99.254",26379).toString());
        
        /**
         * masterName 分片的名称
         * sentinels Redis Sentinel 服务地址列表
         */
        JedisSentinelPool poolA = new JedisSentinelPool("mymaster", sentinels);
        //JedisSentinelPool poolB = new JedisSentinelPool("shard_b", sentinels);
        //获取Jedis主服务器客户端实例
        Jedis jedisA = poolA.getResource();
        jedisA.set("key", "abc");
        System.out.println("jedisA key:"+jedisA.get("key"));

        poolA.returnResource(jedisA); 
        poolA.destroy();
        
        /*
        Jedis jedisB = poolB.getResource();
        jedisB.set("key", "xyz");
        System.out.println("jedisB key:"+jedisB.get("key"));
        */
        //输出结果
        //jedisA key:abc
        //jedisB key:xyz
    }
}