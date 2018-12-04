package org.spring.springboot.dao;

import org.spring.springboot.domain.Phone;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author yuyunfeng
 * @create_time 2018/11/30
 * @describe ${class}
 */
public interface PhoneMongoDao extends MongoRepository<Phone, Long> {
}
