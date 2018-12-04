package org.spring.springboot.mq;

import org.spring.springboot.domain.Phone;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MtSenderServiceImpl implements MtSenderService {
    @Autowired
    private AmqpTemplate template;
    
    public void sendUploadPhone(List<Phone> list) {
        template.convertAndSend("MT-phone-upload",list);
    }

    public void sendPhoneMassage(List<Phone> list) {
        template.convertAndSend("MT-phone",list);
    }



}