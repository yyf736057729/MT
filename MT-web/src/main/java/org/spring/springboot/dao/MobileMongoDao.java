package org.spring.springboot.dao;

import org.spring.springboot.domain.Mobile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yuyunfeng
 * @create_time 2018/11/30
 * @describe ${class}
 */
@Repository
public interface MobileMongoDao extends MongoRepository<Mobile, Long> {

}
