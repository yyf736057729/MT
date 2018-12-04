package org.spring.springboot.mq;

import org.spring.springboot.domain.Phone;
import org.spring.springboot.service.MongodbPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class SunCallable implements Callable<Boolean> {

	private MongoTemplate mongoTemplate;

    RedisTemplate redisTemplate;

	/**当前是属于第几段线程**/
	private int pageIndex;

	@Autowired(required = false)
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		RedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(stringSerializer);
		this.redisTemplate = redisTemplate;
	}

	private List<String> list;
    private MongodbPhoneService mongodbPhoneService;
	public SunCallable(int pageIndex, List<String> list, MongoTemplate mongoTemplate, RedisTemplate redisTemplate){
		this.pageIndex = pageIndex;
		this.list = list;
		this.mongoTemplate = mongoTemplate;
		this.redisTemplate = redisTemplate;
	}



	List<Phone> phones = null;
	@Override
	public Boolean call() throws Exception {
		System.err.println(String.format("pageIndex:%s size:%s",pageIndex,list.size()));
		Boolean result = Boolean.TRUE;
        mongoTemplate.insertAll(list);
		return result;
	}
}