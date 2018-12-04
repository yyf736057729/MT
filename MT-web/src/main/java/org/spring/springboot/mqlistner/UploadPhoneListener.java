package org.spring.springboot.mqlistner;

import org.apache.log4j.Logger;
import org.spring.springboot.domain.Phone;
import org.spring.springboot.mq.SunCallable;
import org.spring.springboot.service.MongodbPhoneService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class UploadPhoneListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private long count;

    private static final Logger LOG = Logger.getLogger(UploadPhoneListener.class);

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
    @RabbitListener(queues="MT-phone-upload")    //监听器监听指定的Queue
//    @RabbitListener(queues="test1")
    public void processC(List<Phone> phones) {
        try {
            LOG.info("进入MT1监听");
            doReadList(phones);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void doReadList(List list) throws InterruptedException, ExecutionException, ExecutionException {
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        ExecutorService threadPoolTaskExecutor = Executors.newFixedThreadPool(10);
//        /**初始化集合**/
//        List<String> list = new ArrayList<String>();


        /**接收集合各段的 执行的返回结果**/
        List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();

        /**集合总条数**/
        int size = list.size();
        /**将集合切分的段数**/
        int sunSum = 10;
        int listStart,listEnd;
        /***当总条数不足10条时 用总条数 当做线程切分值**/
        if(sunSum > size){
            sunSum = size;
        }
        /**定义子线程**/
        SunCallable sunCallable ;
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
            List<String> sunList = list.subList(listStart,listEnd);
            /**子线程初始化**/
            sunCallable = new SunCallable(i,sunList, mongoTemplate,redisTemplate);
            /***多线程执行***/
            futureList.add(threadPoolTaskExecutor.submit(sunCallable));
        }
        /**对各个线程段结果进行解析**/
        for(Future<Boolean> future : futureList){
            if(null != future && future.get()){
                System.err.println("成功");
            }else{
                System.err.println("失败");
            }
        }
    }


}