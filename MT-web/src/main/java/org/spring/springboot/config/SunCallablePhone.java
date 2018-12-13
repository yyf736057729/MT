package org.spring.springboot.config;

import org.apache.log4j.Logger;
import org.spring.springboot.mq.MtSenderService;
import org.spring.springboot.domain.Mt;
import org.spring.springboot.domain.Phone;
import org.spring.springboot.domain.Statistic;
import org.spring.springboot.service.MongodbMtOldService;
import org.spring.springboot.service.MongodbPhoneService;
import org.spring.springboot.task.MtTask;
import org.spring.springboot.util.MtApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SunCallablePhone implements Runnable {

	private static final Logger LOG = Logger.getLogger(SunCallablePhone.class);
	/**当前是属于第几段线程**/
	private int pageIndex;

	private List<Phone> list;
    private MongodbPhoneService mongodbPhoneService;
	MongodbMtOldService mongodbMtOldService;
	RedisTemplate redisTemplate;
	MtSenderService mtSenderService;
	MongoTemplate mongoTemplate;
	public SunCallablePhone(int pageIndex, List<Phone> list, MongodbPhoneService mongodbPhoneService, MongodbMtOldService mongodbMtOldService,
							RedisTemplate redisTemplate, MtSenderService mtSenderService, MongoTemplate mongoTemplate){
		this.pageIndex = pageIndex;
		this.list = list;
		this.mongodbPhoneService = mongodbPhoneService;
		this.mongodbMtOldService = mongodbMtOldService;
		this.redisTemplate  = redisTemplate;
		this.mtSenderService  = mtSenderService;
		this.mongoTemplate  = mongoTemplate;
	}
	public static int count = 0;


	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		System.out.println(count+++"name:"+name);
		System.err.println(String.format("pageIndex:%s size:%s",pageIndex,list.size()));
		//将消息放入消息队列中
		try{
			mtSenderService.sendPhoneMassage(list);
		}catch (Exception E){
		}

	}
}