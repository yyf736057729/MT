package org.spring.springboot.domain;

import java.io.Serializable;
import java.util.List;

public class CountAll implements Serializable {
    List<Statistic> list;
    int countAll;
    int countByDay;
    int listSize;
    int listSize1;

    @Override
    public String toString() {
        return "CountAll{" +
                "list=" + list +
                ", countAll=" + countAll +
                ", countByDay=" + countByDay +
                ", listSize=" + listSize +
                ", listSize1=" + listSize1 +
                '}';
    }

    public List<Statistic> getList() {
        return list;
    }

    public void setList(List<Statistic> list) {
        this.list = list;
    }

    public int getCountAll() {
        return countAll;
    }

    public void setCountAll(int countAll) {
        this.countAll = countAll;
    }

    public int getCountByDay() {
        return countByDay;
    }

    public void setCountByDay(int countByDay) {
        this.countByDay = countByDay;
    }

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public int getListSize1() {
        return listSize1;
    }

    public void setListSize1(int listSize1) {
        this.listSize1 = listSize1;
    }
}
