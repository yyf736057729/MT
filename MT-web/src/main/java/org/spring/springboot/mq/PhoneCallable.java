package org.spring.springboot.mq;

import org.apache.log4j.Logger;
import org.spring.springboot.dao.StatisticsMongoDao;
import org.spring.springboot.domain.Mt;
import org.spring.springboot.domain.Phone;
import org.spring.springboot.domain.Statistic;
import org.spring.springboot.service.MongodbMtOldService;
import org.spring.springboot.service.MongodbPhoneService;
//import org.spring.springboot.task.MtTask;
import org.spring.springboot.task.Common;
import org.spring.springboot.util.MtApi;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author yuyunfeng
 * @create_time 2018/12/3
 * @describe ${class}
 * 手机号码提交到美团
 */
public class PhoneCallable implements Runnable {

    private static final Logger LOG = Logger.getLogger(PhoneCallable.class);
    /**
     * 当前是属于第几段线程
     **/
    private int pageIndex;

    private List<Phone> list;
    private MongodbPhoneService mongodbPhoneService;
    MongodbMtOldService mongodbMtOldService;
    RedisTemplate redisTemplate;
    StatisticsMongoDao statisticsMongoDao;
    MongoTemplate mongoTemplate;

    public PhoneCallable(int pageIndex, List<Phone> list, MongodbPhoneService mongodbPhoneService,
                         MongodbMtOldService mongodbMtOldService, RedisTemplate redisTemplate, StatisticsMongoDao statisticsMongoDao, MongoTemplate mongoTemplate) {
        this.pageIndex = pageIndex;
        this.list = list;
        this.mongodbPhoneService = mongodbPhoneService;
        this.mongodbMtOldService = mongodbMtOldService;
        this.redisTemplate = redisTemplate;
        this.statisticsMongoDao = statisticsMongoDao;
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.err.println(String.format("pageIndex:%s size:%s", pageIndex, list.size()));
        Boolean result = Boolean.TRUE;
        List<Statistic> statistics = new ArrayList<>();
        List<Phone> phones = new ArrayList<>();
        boolean good = true;
        if (null != list && list.size() > 0) {
            for (Phone phone1 : list) {
                String s = phone1.getPhone();
                System.out.println(s);
                try {
                    boolean hasKey = redisTemplate.hasKey(s);
                    if (hasKey) { // 从缓存中取
                        continue;
                    } else {
                        Phone phone = new Phone();
                        Mt mt = MtApi.mtNumber(s);
                        System.out.println("调用次数:" + Common.count++);
                        if ("领取成功".equals(mt.getMessage())) {
                            mt.setMessage("1");
                            if ("美团外卖新人首单红包".equals(mt.getCoupon_display_name())) {
                                mt.setCoupon_display_name("1");
                            } else {
                                mt.setCoupon_display_name("10");
                            }
                        } else if ("手机号被风控，30 天内无法重复领取".equals(mt.getMessage())) {
                            mt.setMessage("4");
                        } else if ("已经领取过了，不能重复领取".equals(mt.getMessage())) {
                            mt.setMessage("3");
                        } else if ("访问频繁".equals(mt.getMessage())) {
                            phones.add(phone);
                            mt.setMessage("8");
                        } else if ("用户未中奖".equals(mt.getMessage())) {
                            mt.setMessage("2");
                        } else if ("错误的手机号".equals(mt.getMessage())) {
                            mt.setMessage("5");
                        } else if ("不在活动地域列表中".equals(mt.getMessage())) {
                            mt.setMessage("6");
                        } else if ("非法时间段调用".equals(mt.getMessage())) {
                            mt.setMessage("7");
                            phones.add(phone);
                        } else if ("系统故障".equals(mt.getMessage())) {
                            mt.setMessage("9");
                        } else {
                            mt.setMessage("10");
                        }
                        Statistic statistic = new Statistic();
                        statistic.setPhone(s);
                        statistic.setMessage(mt.getMessage());
                        statistic.setCoupon_display_name(mt.getCoupon_display_name());
                        statistic.setCreate(new Date());
                        statistics.add(statistic);
                        phone.setId(phone1.getId());
                        LOG.info("入参phone=" + s + "\n" + "出参:" + mt);
                        System.out.println("入参phone=" + s + "\n" + "出参:" + mt);
                        mt.setPhone(s);
                        System.out.println("当前线程名字:"+name);
                        if (!"访问频繁".equals(mt.getMessage())) {
                            if ("领取成功".equals(mt.getMessage()) && "美团外卖新人首单红包".equals(mt.getCoupon_display_name())) {
                                redisTemplate.opsForValue().set(s, mt, 86400 * 37, TimeUnit.SECONDS);//新用户 37天
                            } else {
                                redisTemplate.opsForValue().set(s, mt, 0, TimeUnit.SECONDS);//老用户 存在永久
                            }
                            continue;
                        }
                    }
                } catch (Exception e) {
                    result = Boolean.FALSE;
                }
            }
        }
        System.out.println("插入----------");
        mongoTemplate.insert(statistics,"statistics_"+Common.TODAY);
        System.out.println("插入结束-----------");
        if (phones.size() > 0) {
            System.out.println("访问频繁回归!");
            BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Phone.class);
            for (Phone s : phones) {
                Update u = Update.update("status", "0");
                ops.updateOne(query(where("id").is(s.getId())), u);
            }
            ops.execute();
        }
    }
}
