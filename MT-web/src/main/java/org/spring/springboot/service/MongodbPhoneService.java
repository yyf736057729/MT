package org.spring.springboot.service;

import org.spring.springboot.domain.Phone;

import java.util.List;

public interface MongodbPhoneService {
    void mongodbInsert(Phone phone);


    List<Phone> mongodbFindList();

    int mongodbEdit(Phone phone);

    void mongodbEditCount(Phone phone);

    int mongodbNotCount();
    void updateList(List<Phone> list);

}
