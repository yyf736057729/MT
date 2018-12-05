package org.spring.springboot.task;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.spring.springboot.domain.Messages;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 报表
 */
@Component
public class ExcelTask {


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
    //    @Scheduled(cron = "0 * 2 * * ?")
    @Scheduled(cron = "0 30 23 * * ?")
    public void excel(){
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
        update.set("alreadyReceived",messages.getAlreadyReceived());
        update.set("risk",messages.getRisk());
        update.set("no_prize",messages.getNo_prize());
        update.set("systemFailure",messages.getSystemFailure());
        update.set("frequently",messages.getFrequently());
        update.set("errorPhone",messages.getErrorPhone());
        update.set("success",messages.getSuccess());
        update.set("newPeople",messages.getNewPeople());
        mongoTemplate.upsert(query, update, Messages.class);
        List<Messages> all = mongoTemplate.findAll(Messages.class);
        JSONArray JS = JSONArray.fromObject(all);
//        JSONObject o  = (JSONObject)((Object) all);
        redisTemplate.opsForValue().set("newMessagesList", JS.toString(),1728000, TimeUnit.SECONDS);

    }
//    @Scheduled(cron = "0/10 * * * * ?")
    public void excelTaskMongodb(){

    }

}
