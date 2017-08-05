package com.ai.redis.util;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liuhb on 2017/8/5.
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {

    private JedisPoolConfig jedisPoolConfig;
    private JedisCluster jedisCluster;
    private int connectionTimeout = 2000;
    private int soTimeout = 3000;
    private int maxRedirections;
    private Set<String> jedisClusterNodes;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(jedisClusterNodes)) {
            throw new NullPointerException("jedisClusterNodes is null.");
        }
        Set<HostAndPort> haps = new HashSet<>();
        jedisClusterNodes.forEach(node -> {
            String[] arr = node.split("[:]");
            haps.add(new HostAndPort(arr[0], Integer.valueOf(arr[1])));
        });
        jedisCluster = new JedisCluster(haps, connectionTimeout, maxRedirections, jedisPoolConfig);
        // jedisCluster = new JedisCluster(haps, connectionTimeout, soTimeout, maxRedirections, genericObjectPoolConfig);
    }

    @Override
    public JedisCluster getObject() throws Exception {
        return jedisCluster;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public void setJedisClusterNodes(Set<String> jedisClusterNodes) {
        this.jedisClusterNodes = jedisClusterNodes;
    }
}
