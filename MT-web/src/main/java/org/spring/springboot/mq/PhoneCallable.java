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
public class PhoneCallable implements Callable<Boolean> {

    private static final Logger LOG = Logger.getLogger(PhoneCallable.class);
    /**当前是属于第几段线程**/
    private int pageIndex;

    private List<Phone> list;
    private MongodbPhoneService mongodbPhoneService;
    MongodbMtOldService mongodbMtOldService;
    RedisTemplate redisTemplate;
    StatisticsMongoDao statisticsMongoDao;
    MongoTemplate mongoTemplate;
    public PhoneCallable(int pageIndex, List<Phone> list, MongodbPhoneService mongodbPhoneService,
                         MongodbMtOldService mongodbMtOldService, RedisTemplate redisTemplate, StatisticsMongoDao statisticsMongoDao, MongoTemplate mongoTemplate){
        this.pageIndex = pageIndex;
        this.list = list;
        this.mongodbPhoneService = mongodbPhoneService;
        this.mongodbMtOldService = mongodbMtOldService;
        this.redisTemplate  = redisTemplate;
        this.statisticsMongoDao = statisticsMongoDao;
        this.mongoTemplate= mongoTemplate;
    }

    @Override
    public Boolean call() throws Exception {
        System.err.println(String.format("pageIndex:%s size:%s",pageIndex,list.size()));
        Boolean result = Boolean.TRUE;
        List<Statistic> statistics = new ArrayList<>();
        List<Phone>phones = new ArrayList<>();
        Statistic statistic = new Statistic();
        Phone phone = new Phone();
        boolean good = true;
        if(null != list && list.size() >0){
            for(Phone phone1: list) {
                if(!good){
                    phones.add(phone);
                    continue;
                }
                String s = phone1.getPhone();
                System.out.println(s);
                try {

                    boolean hasKey = redisTemplate.hasKey(s);
                        if (hasKey) { // 从缓存中取
                            continue;
                    } else {

                      Mt  mt = MtApi.mtNumber(s);

                        if("领取成功".equals(mt.getMessage())){
                            mt.setMessage("1");
                            if("美团外卖新人首单红包".equals(mt.getCoupon_display_name())){
                                mt.setCoupon_display_name("1");
                            }else {
                                mt.setCoupon_display_name("10");
                            }
                        }else if("手机号被风控，30 天内无法重复领取".equals(mt.getMessage())){
                            mt.setMessage("4");
                        }else if("已经领取过了，不能重复领取".equals(mt.getMessage())){
                            mt.setMessage("3");
                        }else if("访问频繁".equals(mt.getMessage())){
                            phones.add(phone);
                            mt.setMessage("8");
                            good=false;
                            break;
                        }else if("用户未中奖".equals(mt.getMessage())){
                            mt.setMessage("2");
                        }else if("错误的手机号".equals(mt.getMessage())){
                            mt.setMessage("5");
                        }else if("不在活动地域列表中".equals(mt.getMessage())){
                            mt.setMessage("6");
                        }else if("非法时间段调用".equals(mt.getMessage())){
                            mt.setMessage("7");
                            phones.add(phone);
                        }else if("系统故障".equals(mt.getMessage())){
                            mt.setMessage("9");
                        }else{
                            mt.setMessage("10");
                        }
                        System.out.println("调用次数:"+ Common.count++);
                        statistic.setPhone(s);
                        statistic.setMessage(mt.getMessage());
                        statistic.setCoupon_display_name(mt.getCoupon_display_name());
                        statistic.setCreate(new Date());
                        statistics.add(statistic);
                        phone.setId(phone1.getId());

//                        mongodbMtOldService.mongodbInsertStatistic(statistic);
                        LOG.info("入参phone=" + s + "\n" + "出参:" + mt);
                        System.out.println("入参phone=" + s + "\n" + "出参:" + mt);
                        mt.setPhone(s);

                        if(!"访问频繁".equals(mt.getMessage())){
//                            mongodbPhoneService.mongodbEditCount(phone);
                            redisTemplate.opsForValue().set(s, mt,86400*37, TimeUnit.SECONDS);
                            continue;
                        }

                    }
                } catch (Exception e) {
                    result = Boolean.FALSE;
                }
            }
        }
        System.out.println("插入----------");
        statisticsMongoDao.insert(statistics);
        System.out.println("插入结束-----------");

        if(phones.size()>0){
            System.out.println("访问频繁回归!");
            BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Phone.class);
            for(Phone s:phones){
                Update u =  Update.update("status","0");
                ops.updateOne(query(where("id").is(s.getId())),u);
            }
            ops.execute();
        }
        return result;
    }

}
