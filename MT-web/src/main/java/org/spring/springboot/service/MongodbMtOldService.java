package org.spring.springboot.service;

import org.spring.springboot.domain.Statistic;

import java.util.Date;
import java.util.List;

public interface MongodbMtOldService {
    int mongodbInsertStatistic(Statistic statistic);

    int mongodbFindCount(Date date);

    List<Statistic>  mongodbFindstatistics(Date date);

    List mongodbFindByCreate();

    int mongodbFindCountCreate(String date);

    long mongodbFindListStatistic(int i,String message,String couponDisplayName);

}
