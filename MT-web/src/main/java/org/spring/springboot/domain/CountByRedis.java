package org.spring.springboot.domain;

import java.io.Serializable;
import java.util.List;

public class CountByRedis  implements Serializable {
    private int countAll;
    private int countByDay;
    private List<Statistic> findstatistics;
    private int listSize;
    private int listSize1;
    private int byCount;
    private int byCount1;
    private int notCount;

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

    public List<Statistic> getFindstatistics() {
        return findstatistics;
    }

    public void setFindstatistics(List<Statistic> findstatistics) {
        this.findstatistics = findstatistics;
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

    public int getByCount() {
        return byCount;
    }

    public void setByCount(int byCount) {
        this.byCount = byCount;
    }

    public int getByCount1() {
        return byCount1;
    }

    public void setByCount1(int byCount1) {
        this.byCount1 = byCount1;
    }

    public int getNotCount() {
        return notCount;
    }

    public void setNotCount(int notCount) {
        this.notCount = notCount;
    }
}
