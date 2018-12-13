package org.spring.springboot;

import com.alibaba.fastjson.JSON;
import org.mybatis.spring.annotation.MapperScan;
import org.spring.springboot.domain.Messages;
import org.spring.springboot.domain.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Spring Boot 应用启动类
 *
 * Created by bysocket on 16/4/26.
 */
// Spring Boot 应用的标识
//@SpringBootApplication
// mapper 接口类扫描包配置
@MapperScan("org.spring.springboot.dao")


@Configuration
@EntityScan("org.spring.springboot.domain")
@SpringBootApplication
// mapper 接口类扫描包配置
//@MapperScan("org.spring.springboot.dao")
@EnableScheduling
//@EntityScan("org.spring.springboot.domain")
//@RestController
public class Application {


    public static void main(String[] args) throws IOException {
        // 程序启动入口
        // 启动嵌入式的 Tomcat 并初始化 Spring 环境及其各 Spring 组件
        SpringApplication.run(Application.class,args);

//        StepExecutor stepExecutor =(StepExecutor) SpringUtil.getBean(StepExecutor.class);
//        stepExecutor.init();


    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("999999999KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("999999999KB");
        return factory.createMultipartConfig();
    }
}
