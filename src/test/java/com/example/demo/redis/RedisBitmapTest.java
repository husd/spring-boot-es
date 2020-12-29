package com.example.demo.redis;

import com.example.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 这里是这个类的功能描述
 *
 * @author hushengdong
 */
public class RedisBitmapTest extends BaseTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testBitmap() {

        String key = "test:bit:map";
        String key2 = "test:bit:map2";

        stringRedisTemplate.delete(key);
        stringRedisTemplate.delete(key2);

        stringRedisTemplate.opsForValue().setBit(key, 100, true);
        stringRedisTemplate.opsForValue().setBit(key, 101, true);
        stringRedisTemplate.opsForValue().setBit(key, 102, true);
        System.out.println("getBit is :" + stringRedisTemplate.opsForValue().getBit(key, 100));
        System.out.println("bitCount is :" + bitCount(key));


        stringRedisTemplate.opsForValue().setBit(key2, 100, true);
        stringRedisTemplate.opsForValue().setBit(key2, 103, true);
        stringRedisTemplate.opsForValue().setBit(key2, 104, true);

        System.out.println("key1 len is : " + stringRedisTemplate.opsForValue().get(key).length() * 8);
        System.out.println("key2 len is : " + stringRedisTemplate.opsForValue().get(key2).length() * 8);

        System.out.println("bit AND & is " + bitOpResult(RedisStringCommands.BitOperation.AND, "not Exist1", key, key2));
        System.out.println("bit OR | is " + bitOpResult(RedisStringCommands.BitOperation.OR, "not Exist2", key, key2));
        //异或
        System.out.println("bit XOR ^ is " + bitOpResult(RedisStringCommands.BitOperation.XOR, "not Exist3", key, key2));
        //取反
        System.out.println("bit NOT ～ is " + bitOpResult(RedisStringCommands.BitOperation.NOT, "not Exist4", key2));
    }

    public long bitCount(String key) {
        return (long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.bitCount(key.getBytes());
            }
        });
    }

    /**
     * 对一个或多个保存二进制的字符串key进行元操作，并将结果保存到saveKey上。
     * <p>
     * bitop and saveKey key [key...]，对一个或多个key逻辑并，结果保存到saveKey。
     * bitop or saveKey key [key...]，对一个或多个key逻辑或，结果保存到saveKey。
     * bitop xor saveKey key [key...]，对一个或多个key逻辑异或，结果保存到saveKey。
     * bitop xor saveKey key，对一个或多个key逻辑非，结果保存到saveKey。
     * <p>
     *
     * @param op      元操作类型；
     * @param saveKey 元操作后将结果保存到saveKey所在的结构中。
     * @param desKey  需要进行元操作的类型。
     * @return 1：返回元操作值。
     */
    public Long bitOp(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
        byte[][] bytes = new byte[desKey.length][];
        for (int i = 0; i < desKey.length; i++) {
            bytes[i] = desKey[i].getBytes();
        }
        return stringRedisTemplate.execute((RedisCallback<Long>) con -> con.bitOp(op, saveKey.getBytes(), bytes));
    }

    /**
     * 对一个或多个保存二进制的字符串key进行元操作，并将结果保存到saveKey上，并返回统计之后的结果。
     *
     * @param op      元操作类型；
     * @param saveKey 元操作后将结果保存到saveKey所在的结构中。
     * @param desKey  需要进行元操作的类型。
     * @return 返回saveKey结构上value=1的所有数量值。
     */
    public Long bitOpResult(RedisStringCommands.BitOperation op, String saveKey, String... desKey) {
        bitOp(op, saveKey, desKey);
        return bitCount(saveKey);
    }

}
