package com.forezp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.springboot.Application;
import org.spring.springboot.dao.StatisticsMongoDao;
import org.spring.springboot.domain.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

@RunWith(SpringRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
public class mongodbTests {


    @Autowired
    StatisticsMongoDao statisticsMongoDao;



    @Test
    public void mongodbIdTestcc(){
//        Statistic statistic=new Statistic(new Date(),"123123213","213213","123");
        Statistic statistic=new Statistic();
        statistic.setCoupon_display_name("123");
        statistic.setCreate(new Date());
        statistic.setMessage("222222222");
        statistic.setPhone("888888888888");
        statistic=statisticsMongoDao.save(statistic);
//        logger.info("mongodbId:"+customer.getId());
        System.out.println("mongodbId:"+statistic.getId());
    }


    @Test
    public void contextLoads() {
    }

}