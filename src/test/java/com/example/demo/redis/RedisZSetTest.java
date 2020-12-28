package com.example.demo.redis;

import com.example.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 这里是这个类的功能描述
 *
 * @author hushengdong
 */
public class RedisZSetTest extends BaseTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testSet() {

        //测试ZSET 模拟微博相互关注假设有1000W
        //ID 是2 ～ 10000000的人 ，都去关注 ID 是1的人
        String followers = "followers:1";
        for (int i = 2; i < 500000; i++) {

            stringRedisTemplate.opsForSet().add(followers, String.valueOf(i));
            stringRedisTemplate.opsForSet().add("following:" + i, "1");
        }
    }
}
