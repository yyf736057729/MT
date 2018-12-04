package org.spring.springboot.service.impl;


import org.spring.springboot.dao.*;
import org.spring.springboot.domain.*;
import org.spring.springboot.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuyunfeng
 * @create_time 2018/11/30
 * @describe ${class}
 */
@Service
public class MongoImpl implements MongoService {

    @Autowired
    private  MessagesMongoDao messagesMongoDao;

    @Autowired
    private  MobileMongoDao mobileMongoDao;

    @Autowired
    private PhoneMongoDao phoneMongoDao;

    @Autowired
    private StatisticsMongoDao statisticsMongoDao;

    @Autowired
    private UserRiskMongoDao userRiskMongoDao;

    public boolean  saveMessages(Messages messages) {
        messagesMongoDao.save(messages);
        return true;

    }

    public boolean  savemobile(Mobile mobile){
        mobileMongoDao.save(mobile);
        return true;

    }

    public boolean  savePhone(Phone phone){
        phoneMongoDao.save(phone);
        return true;

    }


    public boolean  saveStatistics(Statistic statistic){
        statisticsMongoDao.save(statistic);
        return true;

    }


    public boolean  saveUserRisk(UserRisk userRisk){
        userRiskMongoDao.save(userRisk);
        return true;

    }


}
