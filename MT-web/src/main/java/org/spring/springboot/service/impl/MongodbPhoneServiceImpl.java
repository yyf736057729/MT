package org.spring.springboot.service.impl;

import org.spring.springboot.domain.Phone;
import org.spring.springboot.service.MongodbPhoneService;
import org.spring.springboot.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class MongodbPhoneServiceImpl implements MongodbPhoneService {
    @Autowired
    private MongoTemplate mongoTemplate;

//    private MongodbPhoneDao mongodbPhoneDao;


    /**
     * mongodb 插入
     * @return
     */
    public void mongodbInsert(Phone phone){
        phone.setStatus("0");
        phone.setCreate(new Date());

        mongoTemplate.insert(phone);
    }

    /**
     * mongodb 查询5000条
     * @return
     */
    public List<Phone> mongodbFindList() {
        //用来封装所有条件的对象
        Query query = new Query();
        //用来构建条件
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"id")));
        Criteria criteria = new Criteria();
        query.addCriteria(Criteria.where("status").is("0"));
        List<Phone> phones = mongoTemplate.find(query.skip(0).limit(5100), Phone.class);//(query,返回类型.class,collectionName);
        return phones;
    }

    /**
     * mongodb修改
     * @param phone
     * @return
     */
    public int mongodbEdit(Phone phone) {
        Criteria phone1 = Criteria.where("phone").in(phone.getPhone());
        Query query = new Query();
        query.addCriteria(phone1);
        if(null != phone.getPhone()){
            Criteria phone2 = Criteria.where("id").in(phone.getId());
            query.addCriteria(phone2);
        }
        Update update = new Update();
        update.set("status", "1");
        mongoTemplate.updateFirst(query, update, Phone.class,"phone");

        return 0;
    }


    /**
     * mongodb修改
     * @param phone
     */
    public void mongodbEditCount(Phone phone) {
        /*UPDATE phone SET `status` = '1', `create`=now() WHERE `id` = #{id};
        *
        * */
        Query query = new Query();
        Criteria phone2 = Criteria.where("id").in(phone.getId());
        query.addCriteria(phone2);
        Update update = new Update();
        update.set("status", "1");
        update.set("create", DateUtil.format1(new Date()));
        mongoTemplate.updateFirst(query, update, Phone.class,"phone");
    }

    /**
     * mongodb 页面
     * @return
     */
    public int mongodbNotCount() {
        Criteria phone = Criteria.where("status").in("0");
        Query query = new Query();
        query.addCriteria(phone);
        long i = mongoTemplate.count(query, "phone");
        return (int) i;
    }

    /**
     * 批量修改
     * @param list
     * @return
     */
    @Override
    public void updateList(List<Phone> list) {
        BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Phone.class);
        for(Phone p:list){
            Update u =  Update.update("status", "1");
            ops.updateOne(query(where("id").is(p.getId())),u);
        }
        ops.execute();
    }

}
