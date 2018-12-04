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

import java.util.Date;
import java.util.List;
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
        //用来构建条件
        query.addCriteria(Criteria.where("create").gte(date));
        long number = mongoTemplate.count(query, "statistics");//(query,返回类型.class,collectionName);
//        Statistic st = new Statistic();
//        BeanUtils.copyProperties(query, st);
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT) //改变默认字符串匹配方式：模糊查询
//                .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
//                .withMatcher("create", ExampleMatcher.GenericPropertyMatchers.contains()) //采用“包含匹配”的方式查询
//              ;  //忽略属性，不参与查询;
//        Example<Statistic> example = Example.of(st, matcher);
//        long count = mongodbMtOldDao.count(example);

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
        /*
        * select
		 DATE_FORMAT(`create`,"%Y-%m-%d")
		from statistics GROUP BY
		DATE_FORMAT(`create`,"%Y-%m-%d")*/
        Query query = new Query();
        query.addCriteria(Criteria.where("create").gte(""));
        List<Statistic> statistics = mongoTemplate.find(query, Statistic.class, "statistics");

        for(Statistic s:statistics){
            String s1 = DateUtil.getyyyy_MM_dd(s.getCreate());//yyyy_MM_dd
//            s.setObj(s1);
        }
//        Map<String, List<Statistic>> collect = statistics.stream().collect(Collectors.groupingBy(Statistic::getObj));
//        List<Statistic> value = null;
//        for (String key : collect.keySet()) {
//             value = collect.get(key);
//
//        }

        return null;
    }

    /**
     * 统计
     * @param date
     * @return
     */
    public int mongodbFindCountCreate(String date) {
//        select count(1) from statistics
//        where `coupon_display_name` LIKE  concat('%','新人','%') and  `create` LIKE  concat(#{date},'%')
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
//        Date dateByDay_yyyy_hh_ss = DateUtil.getDateByDay_yyyy_hh_ss(8);
        Query query = new Query();
//        Date dateByDay_yyyy_hh_ss1= DateUtil.getDateByDay_yyyy_hh_ss(0);
//                                              "2018/11/24"
//        Pattern pattern=Pattern.compile("^.*"+"2018"+".*$", Pattern.CASE_INSENSITIVE);

//        query.addCriteria(Criteria.where("create").regex(pattern));
        Date dateByBeforeDays = DateUtil.getDateByBeforeDays(i);
        Date dateByBeforeDays2 = DateUtil.getDateByBeforeDays(i-1);
        if(null != couponDisplayName){
            query.addCriteria(Criteria.where("create").lt(DateUtil.getDateByBeforeDays(i)).gte(DateUtil.getDateByBeforeDays(i-1)).and("coupon_display_name").
                            is(couponDisplayName).and("message").is(message)
                    );
        }else{
            query.addCriteria(Criteria.where("create").lt(DateUtil.getDateByBeforeDays(i)).gte(DateUtil.getDateByBeforeDays(i-1)).and("message").is(message));
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
