package com.taotao.rest.dao.impl;

import com.taotao.rest.dao.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 单机版jedis
 */
public class JedisClientSingle implements JedisClient {
    @Autowired
    private JedisPool jedisPool;

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String s = jedis.get(key);
        jedis.close();
        return s;
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String s = jedis.set(key, value);
        jedis.close();
        return s;
    }

    @Override
    public String hget(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        String s = jedis.hget(hkey, key);
        jedis.close();
        return s;
    }

    @Override
    public long hset(String hkey, String key, String value) {
        Jedis jedis = jedisPool.getResource();
        Long s = jedis.hset(hkey, key, value);
        jedis.close();
        return s;
    }

    @Override
    public long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long s = jedis.incr(key);
        jedis.close();
        return s;
    }

    @Override
    public long expire(String key, int second) {
        Jedis jedis = jedisPool.getResource();
        Long s = jedis.expire(key, second);
        jedis.close();
        return s;
    }

    @Override
    public long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long s = jedis.ttl(key);
        jedis.close();
        return s;
    }

    @Override
    public long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long s = jedis.del(key);
        jedis.close();
        return s;
    }

    @Override
    public long hdel(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        Long s = jedis.hdel(hkey, key);
        jedis.close();
        return s;
    }
}
