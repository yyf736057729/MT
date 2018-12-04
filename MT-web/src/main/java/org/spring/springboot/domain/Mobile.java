package org.spring.springboot.domain;

import org.spring.springboot.util.ExcelAnnotation;

import java.io.Serializable;

public class Mobile implements Serializable{
    @ExcelAnnotation(id=2,name={"每日执行总数"},width = 9000)
    private String countByDay ;
//    @ExcelAnnotation(id=3,name={"每日领取总数"},width = 9000)
    private String byCount;
    @ExcelAnnotation(id=3,name={"每日领取新人首单总数"},width = 9000)
    private String listDaySize;
    @ExcelAnnotation(id=1,name={"日期"},width = 9000)
    private String create;
    @ExcelAnnotation(id=4,name={"每日风控数"},width = 9000)
    private String useRisk;
    @ExcelAnnotation(id=5,name={"每日访问频繁数"},width = 9000)
    private String frequently;


    public String getFrequently() {
        return frequently;
    }

    public void setFrequently(String frequently) {
        this.frequently = frequently;
    }

    public String getUseRisk() {
        return useRisk;
    }

    public void setUseRisk(String useRisk) {
        this.useRisk = useRisk;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    @Override
    public String toString() {
        return "Mobile{" +
                "countByDay='" + countByDay + '\'' +
                ", byCount='" + byCount + '\'' +
                ", listDaySize='" + listDaySize + '\'' +
                '}';
    }

    public String getByCount() {
        return byCount;
    }

    public void setByCount(String byCount) {
        this.byCount = byCount;
    }

    public String getListDaySize() {
        return listDaySize;
    }

    public void setListDaySize(String listDaySize) {
        this.listDaySize = listDaySize;
    }

    public String getCountByDay() {
        return countByDay;
    }

    public void setCountByDay(String countByDay) {
        this.countByDay = countByDay;
    }
}
