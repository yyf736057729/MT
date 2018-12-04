package org.spring.springboot.task;

import org.spring.springboot.domain.Phone;
import org.spring.springboot.domain.Statistic;
import org.spring.springboot.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * 处理昨天风控数据
 */
@Component
public class RiskPhoneTask {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Scheduled(cron = "0 * 4 * * ?")
    public void riskPhoneTask(){
        Query query = new Query();
        String dateByDay_yyyy_hh_ss = DateUtil.getDateByDay_yyyy_hh_ss(9);
        Criteria criteria = new Criteria();
        Pattern pattern=Pattern.compile("^.*"+dateByDay_yyyy_hh_ss+".*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("create").lte(DateUtil.getDateByBeforeDays(-1)).gte(DateUtil.getDateByBeforeDays(-2)).and("message").is("4"));
        List<Statistic> list = mongoTemplate.find(query, Statistic.class);
        List<Phone> phones = new ArrayList<>();
        Phone phone = null;
        List<Statistic> statistics = new ArrayList<>();
        if(null != list && list.size()>0){
            for(Statistic s:list){
                phone = new Phone();
                boolean hasKey = redisTemplate.hasKey(s.getPhone());
                if(hasKey == false){
                    phone.setPhone(s.getPhone());
                    phone.setStatus("0");
                    phone.setCreate(new Date());
                    phones.add(phone);
                    statistics.add(s);
                }


            }
        }
        if(null != statistics && statistics.size()>0){
            BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Statistic.class);
            for(Statistic s:statistics){
                Update u =  Update.update("message", "40");//40风控过期
                ops.updateOne(query(where("id").is(s.getId())),u);
            }
            ops.execute();
        }
        mongoTemplate.insertAll(phones);
    }
}
