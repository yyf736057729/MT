package org.spring.springboot.dao;

import org.spring.springboot.domain.Messages;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yuyunfeng
 * @create_time 2018/11/30
 * @describe ${class}
 */
@Repository
public interface MessagesMongoDao extends MongoRepository<Messages, Long> {
}
