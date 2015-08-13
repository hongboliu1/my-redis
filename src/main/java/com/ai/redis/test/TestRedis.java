/**
 * 
 */
package com.ai.redis.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;


/**
 * Class Name		: TestRedis<br>
 * 
 * Description		: 这里记述class说明<br>
 * 
 * @author liuhb
 * @version $Revision$
 * @see
 *
 */
public class TestRedis {

    public static void main(String[] args) {
        List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
        JedisPoolConfig redisConfig = new JedisPoolConfig();
        redisConfig.setMaxTotal(200);
        redisConfig.setMaxWaitMillis(3000);
        String[] addressArr = {"192.168.99.254:6379"};
        for (String str : addressArr) {
            String[] split = str.split(":");
            System.out.println(split[1]);
            String ip = split[0];
            String port = split[1];
            list.add(new JedisShardInfo(ip, Integer.parseInt(port)));
        }
        ShardedJedisPool pool = new ShardedJedisPool(redisConfig, list);
        Collection<Jedis> allShards = pool.getResource().getAllShards();
        for (Jedis jedis : allShards) {
            jedis.ping();
            System.out.println("redis-pingtest    ip:" + jedis.getClient().getHost()
                    + "  port:" + jedis.getClient().getPort());
        }
    }
}
