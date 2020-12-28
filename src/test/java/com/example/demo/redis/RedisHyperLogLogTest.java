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
public class RedisHyperLogLogTest extends BaseTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testSet() {

        //测试HyperLogLog 统计网页www.epoooll.com被访问的次数
        //这里我们主要是统计UV，计算集合中访客的次数
        //注意： PV就直接来个KV结构，每次自增1就行了，完全不用HyperLogLog
        //UV不能这么搞，是因为要记录用户的ID的话，如果用户量特别大，就没办法记录下所有的用户ID，太占用内存了。

        //HyperLogLog有2个命令：
        // PFADD
        // PFCOUNT
        String key = "www.epoooll.com";
        stringRedisTemplate.opsForHyperLogLog().delete(key);
        for (long i = 0; i <= 100000; i++) {
            stringRedisTemplate.opsForHyperLogLog().add(key, String.valueOf(i));
            if (i == 100L || i == 1000 || i == 100000
                    || i == 100000 || i == 200000 || i == 300000
                    || i == 500000 || i == 800000 || i == 2000000
                    || i == 5000000 || i == 10000000) {
                System.out.println("i =" + i + " count=" + stringRedisTemplate.opsForHyperLogLog().size(key));
            }
        }
    }
}
