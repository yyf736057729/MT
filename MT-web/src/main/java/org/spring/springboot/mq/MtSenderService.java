package org.spring.springboot.mq;

import org.spring.springboot.domain.Phone;

import java.util.List;

public interface MtSenderService {

    public void sendUploadPhone(List<Phone> list);

    public void sendPhoneMassage(List<Phone> list);
}
