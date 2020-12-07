package com.example.demo.redis;

import com.example.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;

/**
 * @author hushengdong
 */
public class RedisLuaInvTest extends BaseTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //这里是把lua脚本保存在文本中
    @Test
    public void initLock() {

        GenericToStringSerializer serializer = new GenericToStringSerializer(Object.class);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //通过lua脚本，设置分布式锁
        String redisKey = "p:101:100010001";
        String invNum = "10";
        String uniqueId = redisKey + ":uid";
        int n = 10; //一次扣2个库存
        redisTemplate.opsForValue().set(redisKey, invNum);
        String v = (String) redisTemplate.opsForValue().get(redisKey);
        System.out.println("inv is :" + v);
        // 执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/minus_inv.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(redisKey), n, uniqueId);
        System.out.println("minus inv resu:" + result);
        v = (String) redisTemplate.opsForValue().get(redisKey);
        System.out.println("v is :" + v);
        v = (String) redisTemplate.opsForValue().get(uniqueId);
        System.out.println("unique key is :" + v);
    }

}
