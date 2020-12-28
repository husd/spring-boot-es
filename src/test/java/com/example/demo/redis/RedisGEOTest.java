package com.example.demo.redis;

import com.example.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 这里是这个类的功能描述
 *
 * @author hushengdong
 */
public class RedisGEOTest extends BaseTest {

    private final static String cityGeoKey = "geo:key";

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testAdd() {
        Long addedNum = redisTemplate.opsForGeo().add(cityGeoKey, new Point(116.405285, 39.904989), "北京");
        redisTemplate.opsForGeo().add(cityGeoKey, new Point(116.405285, 39.904989), "上海");
        redisTemplate.opsForGeo().add(cityGeoKey, new Point(116.405285, 39.904989), "深圳");
        System.out.println(addedNum);
    }

    @Test
    public void testGeoGet() {
        List<Point> points = redisTemplate.opsForGeo().position(cityGeoKey, "北京", "上海", "深圳");
        System.out.println(points);
    }

    @Test
    public void testDist() {
        Distance distance = redisTemplate.opsForGeo()
                .distance(cityGeoKey, "北京", "上海", RedisGeoCommands.DistanceUnit.KILOMETERS);
        System.out.println(distance);
    }

    @Test
    public void testNearByXY() {
        //longitude,latitude
        Circle circle = new Circle(116.405285, 39.904989, Metrics.KILOMETERS.getMultiplier());
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(5);
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo()
                .radius(cityGeoKey, circle, args);
        System.out.println(results);
    }

    @Test
    public void testNearByPlace() {
        Distance distance = new Distance(5, Metrics.KILOMETERS);
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(5);
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo()
                .radius(cityGeoKey, "北京", distance, args);
        System.out.println(results);
    }

    @Test
    public void testGeoHash() {
        List<String> results = redisTemplate.opsForGeo()
                .hash(cityGeoKey, "北京", "上海", "深圳");
        System.out.println(results);
    }
}
