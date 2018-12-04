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
import org.springframework.scheduling.annotation.Scheduled;
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
//        if(count>=4500000){
//            return ;
//        }
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

    private void doReadList(List list) throws InterruptedException, ExecutionException, ExecutionException {
        ExecutorService threadPoolTaskExecutor = Executors.newFixedThreadPool(16);

        /**接收集合各段的 执行的返回结果**/
        List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();

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
            futureList.add(threadPoolTaskExecutor.submit(sunCallable));
        }
    }

    @Scheduled(cron = "0 * 1 * * ?")
    public void taskCleanPhone() {
        count=0;
        Common.count=0;
        Common.COUNTUPLOAD=0;
        Common.MQSEND=0;
    }



//    @Scheduled(cron = "0 * 2 * * ?")
   @Scheduled(cron = "0 0/20 * * * ?")
    public void excel() throws ParseException {
       List<Messages> messagesList = new ArrayList<>();
       Messages messages = new Messages();
       String date = DateUtil.getDateByDay_yyyy_hh_ss(0);
       messages.setCreate(date);
//            System.out.println(date);
       //查询某一天的某个状态的量
       String status = "3"; //已经领取过了，不能重复领取
       long alreadyReceived = mongodbMtOldService.mongodbFindListStatistic(0, status,null);
       status = "4";//风控的
       long risk = mongodbMtOldService.mongodbFindListStatistic(0, status,null);
       status = "2";//用户未中奖
       long no_prize = mongodbMtOldService.mongodbFindListStatistic(0, status,null);
       status = "9";//系统故障
       long systemFailure = mongodbMtOldService.mongodbFindListStatistic(0, status,null);
       status = "8";//访问频繁
       long frequently = mongodbMtOldService.mongodbFindListStatistic(0, status,null);
       status = "5";//错误的手机号
       long errorPhone = mongodbMtOldService.mongodbFindListStatistic(0, status,null);
       status = "1";//领取成功(新用户和老用户)
       long success = mongodbMtOldService.mongodbFindListStatistic(0, status,null);
       String couponDisplayName = "1";//新用户(首单)
       long newPeople = mongodbMtOldService.mongodbFindListStatistic(0, status,couponDisplayName);
       messages.setAlreadyReceived(alreadyReceived+"");
       messages.setRisk(risk+"");
       messages.setNo_prize(no_prize+"");
       messages.setSystemFailure(systemFailure+"");
       messages.setFrequently(frequently+"");
       messages.setErrorPhone(errorPhone+"");
       messages.setSuccess(success+"");
       messages.setNewPeople(newPeople+"");
       messagesList.add(messages);
       Query query = new Query();
       query.addCriteria(where("create").is(date));
       boolean exists = mongoTemplate.exists(query, Messages.class);
       Update update = new Update();
       update.set("*",messages);
       mongoTemplate.upsert(query, update, Messages.class);
       List<Messages> all = mongoTemplate.findAll(Messages.class);
       JSONArray JS = JSONArray.fromObject(all);
       JSONObject o  = (JSONObject)((Object) messagesList);
       redisTemplate.opsForValue().set("newMessagesList", JS.toString(),1728000, TimeUnit.SECONDS);


    }






}