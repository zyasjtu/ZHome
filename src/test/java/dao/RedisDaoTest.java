package dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by z673413 on 2016/11/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RedisDaoTest {
    @Autowired
    private RedisDao redisDao;

    @Test
    public void get() throws Exception {
        redisDao.get("redisKey");
    }

    @Test
    public void set() throws Exception {
        redisDao.set("redisKey", "redisValue");
    }

    @Test
    public void setex() throws Exception {
        redisDao.setex("tmp", "30", 30);
    }

    @Test
    public void del() throws Exception {
        redisDao.del("redisKey");
    }

    @Test
    public void hmset() throws Exception {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("hashMapKey1", "1");
        hashMap.put("hashMapKey2", "2");
        redisDao.hmset("redisKey", hashMap);
    }

    @Test
    public void hgetall() throws Exception {
        System.out.println(redisDao.hgetall("redisKey"));
    }

    @Test
    public void hset() throws Exception {
        redisDao.hset("redisKey", "hashMapKey1", "0");
    }

    @Test
    public void hget() throws Exception {
        System.out.println(redisDao.hget("redisKey", "hashMapKey1"));
    }

    @Test
    public void hincrBy() throws Exception {
        redisDao.hincrBy("redisKey", "hashMapKey2", 1L);
    }

    @Test
    public void hdel() throws Exception {
        redisDao.hdel("redisKey", "key1");
    }

    @Test
    public void expire() throws Exception {
        redisDao.expire("redisKey", 10);
    }
}