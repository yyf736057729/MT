package org.spring.springboot.mqlistner;

import org.apache.log4j.Logger;
import org.spring.springboot.config.SunCallable;
import org.spring.springboot.dao.StatisticsMongoDao;
import org.spring.springboot.domain.Phone;
import org.spring.springboot.mq.PhoneCallable;
import org.spring.springboot.service.MongodbMtOldService;
import org.spring.springboot.service.MongodbPhoneService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class PhoneListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongodbMtOldService mongodbMtOldService;

    @Autowired
    private StatisticsMongoDao statisticsMongoDao;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private long count;

    private static final Logger LOG = Logger.getLogger(PhoneListener.class);


    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }




    @Autowired
    private MongodbPhoneService mongodbPhoneService;
    @RabbitListener(queues="MT-phone-one")    //监听器监听指定的Queue
    public void processC(List<Phone> phones) {
        try {
//            //消费手机号码信息，
//            LOG.info("进入MT-phone");
            doReadList(phones);

        }catch (Exception e){

        }
    }


    private void doReadList(List list) throws InterruptedException, ExecutionException, ExecutionException {
//        ExecutorService threadPoolTaskExecutor = Executors.newFixedThreadPool(16);
        /**接收集合各段的 执行的返回结果**/
        /**集合总条数**/
        int size = list.size();
        /**将集合切分的段数**/
        int sunSum = 16;
        int listStart,listEnd;
        /***当总条数不足10条时 用总条数 当做线程切分值**/
        if(sunSum > size){
            sunSum = size;
        }
        /**定义子线程**/
        PhoneCallable phoneCallable;
        /**将list 切分10份 多线程执行**/
        for (int i = 0; i < sunSum; i++) {
            /***计算切割  开始和结束**/
            listStart = size / sunSum * i ;
            listEnd = size / sunSum * ( i + 1 );
            /**最后一段线程会 出现与其他线程不等的情况**/
            if(i == sunSum - 1){
                listEnd = size;
            }
            /**线程切断**/
            List<Phone> sunList = list.subList(listStart,listEnd);
            /**子线程初始化**/
            phoneCallable = new PhoneCallable(i,sunList, mongodbPhoneService, mongodbMtOldService,redisTemplate,statisticsMongoDao,mongoTemplate);
            /***多线程执行***/
            threadPoolTaskExecutor.execute(phoneCallable);
        }

    }

}