package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * Created by z673413 on 2016/11/4.
 */
@Service
public class RedisDao {
    @Autowired
    JedisPool jedisPool;

    public String get(String redisKey) throws Exception {
        Jedis jedis = null;
        String redisValue = null;

        try {
            jedis = jedisPool.getResource();
            redisValue = jedis.get(redisKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return redisValue;
    }

    public boolean set(String redisKey, String redisValue) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.set(redisKey, redisValue);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return !StringUtils.isEmpty(redisKey);
    }

    public boolean setex(String redisKey, String redisValue, int exSeconds) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.setex(redisKey, exSeconds, redisValue);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return !StringUtils.isEmpty(redisKey);
    }

    public void del(String redisKey) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.del(redisKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean hmset(String redisKey, Map<String, String> redisHashMap) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.hmset(redisKey, redisHashMap);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return !StringUtils.isEmpty(redisKey);
    }

    public Map<String, String> hgetall(String redisKey) {
        Jedis jedis = null;
        Map<String, String> redisHashMap = null;

        try {
            jedis = jedisPool.getResource();
            redisHashMap = jedis.hgetAll(redisKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return redisHashMap;
    }

    public boolean hset(String redisKey, String hashMapKey, String hashMapValue) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.hset(redisKey, hashMapKey, hashMapValue);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return !StringUtils.isEmpty(redisKey);
    }

    public String hget(String redisKey, String hashMapKey) {
        Jedis jedis = null;
        String hashMapValue = null;

        try {
            jedis = jedisPool.getResource();
            hashMapValue = jedis.hget(redisKey, hashMapKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return hashMapValue;
    }

    public void hincrBy(String redisKey, String hashMapKey, Long incrValue) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.hincrBy(redisKey, hashMapKey, incrValue);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void hdel(String redisKey, String hashMapKey) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.hdel(redisKey, hashMapKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void expire(String redisKey, int expireSeconds) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.expire(redisKey, expireSeconds);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
