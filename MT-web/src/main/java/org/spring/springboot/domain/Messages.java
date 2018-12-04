package org.spring.springboot.domain;

import org.spring.springboot.util.ExcelAnnotation;

import java.io.Serializable;

public class Messages implements Serializable {
    @ExcelAnnotation(id = 1, name = {"日期"}, width = 5000)
    private String create;//已经领取过了，不能重复领取
    @ExcelAnnotation(id = 2, name = {"已经领取过了，不能重复领取"}, width = 5000)
    private String alreadyReceived;//已经领取过了，不能重复领取
    @ExcelAnnotation(id = 3, name = {"被风控"}, width = 5000)
    private String risk;//手机号被风控，30 天内无法重复领取
    @ExcelAnnotation(id = 4, name = {"用户未中奖"}, width = 5000)
    private String no_prize;//用户未中奖
    @ExcelAnnotation(id = 5, name = {"系统故障"}, width = 5000)
    private String systemFailure;//系统故障
    @ExcelAnnotation(id = 6, name = {"访问频繁"}, width = 5000)
    private String frequently;//访问频繁
    @ExcelAnnotation(id = 7, name = {"错误的手机号"}, width = 5000)
    private String errorPhone;//错误的手机号
    @ExcelAnnotation(id = 8, name = {"领取成功(新用户和老用户)"}, width = 5000)
    private String success;//领取成功
    @ExcelAnnotation(id = 9, name = {"新用户(首单)"}, width = 5000)
    private String newPeople;
    private String counts;
    private String messages;
    private String id;

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getAlreadyReceived() {
        return alreadyReceived;
    }

    public void setAlreadyReceived(String alreadyReceived) {
        this.alreadyReceived = alreadyReceived;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getNo_prize() {
        return no_prize;
    }

    public void setNo_prize(String no_prize) {
        this.no_prize = no_prize;
    }

    public String getSystemFailure() {
        return systemFailure;
    }

    public void setSystemFailure(String systemFailure) {
        this.systemFailure = systemFailure;
    }

    public String getFrequently() {
        return frequently;
    }

    public void setFrequently(String frequently) {
        this.frequently = frequently;
    }

    public String getErrorPhone() {
        return errorPhone;
    }

    public void setErrorPhone(String errorPhone) {
        this.errorPhone = errorPhone;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getNewPeople() {
        return newPeople;
    }

    public void setNewPeople(String newPeople) {
        this.newPeople = newPeople;
    }

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
