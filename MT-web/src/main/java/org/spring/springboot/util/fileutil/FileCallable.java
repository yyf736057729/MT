package org.spring.springboot.util.fileutil;

import org.spring.springboot.mq.MtSenderService;
import org.spring.springboot.domain.Phone;
import org.spring.springboot.task.Common;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import static org.spring.springboot.controller.MtContrller.replaceBlank;


public class FileCallable implements Callable<Boolean> {

    private MongoTemplate mongoTemplate;

    RedisTemplate redisTemplate;

    /**当前是属于第几段线程**/
    private int pageIndex;
    private MtSenderService mtSenderService;
    private List<String> list;
    public FileCallable(int pageIndex, List<String> list, MongoTemplate mongoTemplate, RedisTemplate redisTemplate,MtSenderService mtSenderService){
        this.pageIndex = pageIndex;
        this.list = list;
        this.mongoTemplate = mongoTemplate;
        this.redisTemplate = redisTemplate;
        this.mtSenderService = mtSenderService;
    }



    @Override
    public Boolean call() throws Exception {
        Boolean result = Boolean.TRUE;
        List<Phone> phones1  = new ArrayList<>();
        int i=0;
        if(null != list && list.size() >0) {
            for (String str : list) {
                Phone phone = new Phone();
                try {
                    System.out.println("{已上传数:"+Common.COUNTUPLOAD++ +"}");
                    String s = replaceBlank(str);
                    phone.setPhone(s);
                    phone.setCreate(new Date());
                    phone.setStatus("0");
                    phones1.add(phone);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        mongoTemplate.insertAll(phones1);
        return result;
    }
}
