package org.spring.springboot.task;

import org.spring.springboot.domain.CountAll;
import org.spring.springboot.domain.CountByRedis;
import org.spring.springboot.domain.Statistic;
import org.spring.springboot.mq.MtSenderService;
import org.spring.springboot.service.MongodbMtOldService;
import org.spring.springboot.service.MongodbPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 首页数字
 */
@Component
public class IndexTask {



    @Autowired
    MongodbPhoneService mongodbPhoneService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    MtSenderService mtSenderService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private MongodbMtOldService mongodbMtOldService;
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

//    @Scheduled(cron = "0/15 * * * * ?")
    public void taskPhoneCeche(){
        long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数

        Timestamp timestamp = new Timestamp(zero);
//        int countByDay = mongodbMtOldService.mongodbFindCount(timestamp); //今日已执行

        int notCount = mongodbPhoneService.mongodbNotCount();   //未执行

        //今日已执行
        long countByDay = mongoTemplate.count(new Query(), "statistics_"+Common.TODAY);//(query,返回类型.class,collectionName);  /


//        CountAll countAlls = new CountAll();
//        countAlls.setCountByDay((int)countByDay);

//        List<Statistic> findstatistics = mongodbMtOldService.mongodbFindstatistics(null);

//        if (null != findstatistics) {
//            countAlls.setList(findstatistics);
//        }
//        countByRedis.setFindstatistics(findstatistics);
        CountByRedis countByRedis = new CountByRedis();
        countByRedis.setCountByDay((int)countByDay);
//
        countByRedis.setNotCount(notCount);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(countByRedis);
        redisTemplate.opsForValue().set("newFindCounts", jsonObject.toString(),129600, TimeUnit.SECONDS);


    }
}
