package org.spring.springboot.task;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.spring.springboot.config.SunCallablePhone;
import org.spring.springboot.controller.MtContrller;
import org.spring.springboot.domain.*;
import org.spring.springboot.mq.MtSenderService;
import org.spring.springboot.service.MongodbMtOldService;
import org.spring.springboot.service.MongodbPhoneService;
import org.spring.springboot.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class MtTask {



    @Autowired
    MongodbPhoneService mongodbPhoneService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    MtSenderService mtSenderService;

    @Autowired
    MongoTemplate mongoTemplate;


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
    private MongodbMtOldService mongodbMtOldService;
    private static final Logger LOG = Logger.getLogger(Mt.class);

    public static int count = 0;

//    @Scheduled(cron = "0 0/10 7-23 * * ?")
    @Scheduled(cron = "0 0/1 7-22 * * ?")
//    @Scheduled(cron = "0/20 * * * * ?")
    public void goMT(){
        List<Phone> list = mongodbPhoneService.mongodbFindList();
        Query query = new Query();
        if(null != list && list.size()>0){
            //美团接口处理
            try {
                doReadList(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //批量修改mongodb
            mongodbPhoneService.updateList(list);
        }
    }

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private void doReadList(List list) throws InterruptedException, ExecutionException, ExecutionException {
//        ExecutorService threadPoolTaskExecutor = Executors.newFixedThreadPool(24);
        /**集合总条数**/
        int size = list.size();
        /**将集合切分的段数**/
        int sunSum = 24;
        int listStart,listEnd;
        /***当总条数不足10条时 用总条数 当做线程切分值**/
        if(sunSum > size){
            sunSum = size;
        }
        /**定义子线程**/
        SunCallablePhone sunCallable ;
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
            sunCallable = new SunCallablePhone(i,sunList, mongodbPhoneService, mongodbMtOldService,redisTemplate,mtSenderService,mongoTemplate);
            /***多线程执行***/
            threadPoolTaskExecutor.submit(sunCallable);
        }
    }

//    @Scheduled(cron = "0 * 1 * * ?")
    public void taskCleanPhone() {
        count=0;
        Common.count=0;
        Common.COUNTUPLOAD=0;
        Common.MQSEND=0;
    }






}