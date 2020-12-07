package com.example.demo.redis;

import com.example.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 抽奖项目模拟100W条记录占用多大内存
 *
 * @author hushengdong
 */
public class RedisLuaTest extends BaseTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Test
    public void test() throws IOException {

        String key = "p:inv:10001101";
        int val = 10;
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set(key, val);
        Object k1 = ops.get(key);
        System.out.println(k1);
    }

    //这里是把lua脚本保存在文本中
    @Test
    public void initLock() {

        //通过lua脚本，设置分布式锁
        String lockKey = "123";
        String UUID = "123456";
        boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, UUID, 3, TimeUnit.MINUTES);
        if (!success) {
            System.out.println("锁已存在");
        }
        String v = (String) redisTemplate.opsForValue().get(lockKey);
        System.out.println("v is :" + v);
        // 执行 lua 脚本
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/lua_delete_key.lua")));
        // 指定返回类型
        redisScript.setResultType(Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(lockKey), UUID);
        System.out.println(result);
        v = (String) redisTemplate.opsForValue().get(lockKey);
        System.out.println("v is :" + v);
    }

    //直接使用拼接好的字符串的lua脚本
    @Test
    public void testLua() {

        //通过lua脚本，设置分布式锁
        String lockKey = "123";
        String UUID = "123456";
        boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, UUID, 3, TimeUnit.MINUTES);
        if (!success) {
            System.out.println("锁已存在");
        }
        String v = (String) redisTemplate.opsForValue().get(lockKey);
        System.out.println("v is :" + v);
        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT, Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(lockKey), UUID);
        System.out.println(result);
        v = (String) redisTemplate.opsForValue().get(lockKey);
        System.out.println("v is :" + v);
    }

}
