package com.forezp;//package com.forezp;
import ch.qos.logback.core.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.springboot.Application;
import org.spring.springboot.dao.StatisticsMongoDao;
import org.spring.springboot.domain.Messages;
import org.spring.springboot.domain.Phone;
import org.spring.springboot.domain.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

@RunWith(SpringRunner.class)// SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@TestPropertySource("classpath:application.properties")
public class mongodbTests {



    @Autowired
    MongoTemplate mongoTemplate;


    @Test
    public void writeToTxt() throws IOException {//设置响应的字符集
        //将根据类型生成的信息追加写入txt
        String writerAddr = "E:\\数据.txt";//文件地址
        File writerFile = new File(writerAddr);
        //判断文件是否存在，如果存在则不创建，不存在则创建文件，保证文件一定存在
        if(!writerFile.exists()) {
            try {
                writerFile.createNewFile();
            } catch (IOException e) {
                System.out.println("文件失败");
            }
        }
        long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数

        Timestamp timestamp = new Timestamp(zero);
        Query query= new Query();
        Criteria create = Criteria.where("create").gte(timestamp);
        query.addCriteria(create);
        List<Statistic> statistics = mongoTemplate.find(query, Statistic.class, "statistics");
        List<Statistic> list = new ArrayList<>();
        // 去重
        statistics.stream().forEach(
                p -> {
                    if (!list.contains(p)) {
                        list.add(p);
                    }
                }
        );
        //加true，表示再原来的基础上追加内容，默认false即每次读入就把之前的内容清空再读入。
        //UTF-8是编码方式
        BufferedWriter br=new BufferedWriter(new OutputStreamWriter(new FileOutputStream   (writerFile,true), "UTF-8"));
        try {
            for (Statistic msg : list) {
                //获取每行的读入内容

                if("1".equals(msg.getMessage())){
                    msg.setMessage("领取成功");
                    if("美团外卖新人首单红包".equals(msg.getCoupon_display_name())){
                        msg.setCoupon_display_name("美团外卖新人首单红包");
                    }else {
                        msg.setCoupon_display_name("其他红包或者未有");
                    }
                }else if("4".equals(msg.getMessage())){
                    msg.setMessage("手机号被风控，30 天内无法重复领取");
                }else if("3".equals(msg.getMessage())){
                    msg.setMessage("已经领取过了，不能重复领取");
                }else if("8".equals(msg.getMessage())){
                    msg.setMessage("访问频繁");
                }else if("2".equals(msg.getMessage())){
                    msg.setMessage("用户未中奖");
                }else if("5".equals(msg.getMessage())){
                    msg.setMessage("错误的手机号");
                }else if("6".equals(msg.getMessage())){
                    msg.setMessage("不在活动地域列表中");
                }else if("7".equals(msg.getMessage())){
                    msg.setMessage("非法时间段调用");
                }else if("9".equals(msg.getMessage())){
                    msg.setMessage("系统故障");
                }else{
                    msg.setMessage("");
                }
                String phone = msg.getPhone();
                String message = msg.getMessage();


                br.write(phone);
                br.write("         ");
                br.write("         ");
                br.write("         ");
                br.write(message);
                br.write("         ");
                br.write("         ");
                br.write("         ");
                String coupon_display_name = msg.getCoupon_display_name();
                if(null != coupon_display_name){
                    if(coupon_display_name.equals("1")){
                        coupon_display_name ="新用户";
                    }
                }
                if(null != coupon_display_name){
                    br.write(coupon_display_name);
                }
                br.newLine();//换行
            }
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            //关流
            br.flush();
            br.close();
        }

    }
    @Test
    public void task() throws InterruptedException {
        long l = System.currentTimeMillis();
        long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        Timestamp timestamp = new Timestamp(zero);
        Query query = new Query();
//        query.with(new Sort(new Sort.Order(Sort.Direction.ASC,"id")));
        query.addCriteria(Criteria.where("create").gte(timestamp));
        long count = mongoTemplate.count(query, Statistic.class);
        long l1 = System.currentTimeMillis();
        System.out.println("耗费时长："+(l1-l));
        System.out.println(count);
//        statistics.forEach((x) -> System.out.println(x.getPhone()));
//        Thread.sleep(2000);
//        long count1 = mongoTemplate.count(query, Statistic.class);
//        System.out.println(count1);
//        long count2 = mongoTemplate.count(query, Statistic.class);
//        System.out.println(count2);
//        long count3 = mongoTemplate.count(query, Statistic.class);
//        System.out.println(count3);
    }



}
