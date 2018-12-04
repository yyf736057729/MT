package org.spring.springboot.service;

import org.spring.springboot.domain.*;

import java.util.List;

/**
 * @author yuyunfeng
 * @create_time 2018/12/1
 * @describe ${class}
 */
public interface MongoService {

    boolean saveMessages(Messages messages);

    boolean savemobile(Mobile mobile);

    boolean savePhone(Phone phone);

    boolean saveStatistics(Statistic statistic);

    boolean saveUserRisk(UserRisk userRisk);
}