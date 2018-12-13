package org.spring.springboot.service.impl;

import org.spring.springboot.domain.Statistic;
import org.spring.springboot.service.MongodbMtOldService;
import org.spring.springboot.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Component
public class MongodbMtOldServiceImpl implements MongodbMtOldService {
    @Autowired
    private MongoTemplate mongoTemplate;


//    private MongodbMtOldDao mongodbMtOldDao;
//    @Autowired
//    public MongodbMtOldServiceImpl(MongodbMtOldDao mongodbMtOldDao) {
//        this.mongodbMtOldDao = mongodbMtOldDao;
//    }

    /**
     * mongodb 插入
     * @param statistic
     * @return
     */
    public int mongodbInsertStatistic(Statistic statistic) {
        mongoTemplate.save(statistic,"statistics");
        return 0;
    }

    /**
     * mongodb  页面
     * @param date
     * @return
     */
    public int mongodbFindCount(Date date) {
        //用来封装所有条件的对象
        Query query = new Query();
        Date date1 = DateUtil.formatStrDateToUTCStr(date);
        query.addCriteria(Criteria.where("create").gte(date1));
        long number = mongoTemplate.count(query, "statistics");//(query,返回类型.class,collectionName);

        return (int) number;
    }

    /**
     * mongodb 页面
     * @param date
     * @return
     */
    public List<Statistic> mongodbFindstatistics(Date date)
    {
        Query query= new Query();
        Criteria create = Criteria.where("create").gte(date);
        query.addCriteria(create);
        query.with(new Sort(Sort.Direction.DESC,   "create"));
        List<Statistic> statistics = mongoTemplate.find(query.skip(0).limit(50), Statistic.class, "statistics");
        return statistics;
    }

    /**
     * 统计
     * @return
     */
    public List mongodbFindByCreate() {
        Query query = new Query();
        query.addCriteria(Criteria.where("create").gte(""));
        List<Statistic> statistics = mongoTemplate.find(query, Statistic.class, "statistics");

        for(Statistic s:statistics){
            String s1 = DateUtil.getyyyy_MM_dd(s.getCreate());//yyyy_MM_dd
        }

        return null;
    }

    /**
     * 统计
     * @param date
     * @return
     */
    public int mongodbFindCountCreate(String date) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        Pattern pattern1 = Pattern.compile("^.*"+"新人"+".*$", Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile("^.*"+date+".*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(criteria.and("coupon_display_name").regex(pattern1));
        query.addCriteria(criteria.and("create").regex(pattern2));
        long number = mongoTemplate.count(query, Statistic.class, "statistic");
        return (int) number;
    }

    /**
     * 统计
     * @param message
     * @return
     */
    public long mongodbFindListStatistic(int i,String message,String couponDisplayName) {
        long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        Timestamp timestamp = new Timestamp(zero);
        Query query = new Query();
        Date dateByBeforeDays2 = DateUtil.getDateByBeforeDays(-i-1);
        Date date1 = DateUtil.formatStrDateToUTCStr(timestamp);
        Date date2 = DateUtil.formatStrDateToUTCStr(dateByBeforeDays2);
        if(null != couponDisplayName && !"4".equals(message)){
            query.addCriteria(Criteria.where("create").gte(date1).and("coupon_display_name").
                    is(couponDisplayName).and("message").is(message)
            );
        }else if(null != couponDisplayName && "4".equals(message)){
            query.addCriteria(Criteria.where("create").gte(date1)
                    .and("coupon_display_name").
                            is(couponDisplayName).orOperator(Criteria.where("message").is("4"),Criteria.where("message").is("40"))
            );
        }else{
            query.addCriteria(Criteria.where("create").gte(date1).and("message").is(message));
        }
//        Criteria criteria = new Criteria();
        long count = mongoTemplate.count(query, Statistic.class);
        return count;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.getDateByBeforeDays(-1));
        System.out.println(DateUtil.getDateByBeforeDays(0));
    }
}
