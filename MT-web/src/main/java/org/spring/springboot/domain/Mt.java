package org.spring.springboot.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class Mt  implements Serializable {
    private Long id;
    private String coupon_display_name;
    private String celllphone;
    private String status;
    private int coupon_type;
    private int coupon_amount;
    private String message;
    private String phone;
    private Timestamp create;

    public Timestamp getCreate() {
        return create;
    }

    public void setCreate(Timestamp create) {
        this.create = create;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoupon_display_name() {
        return coupon_display_name;
    }

    public void setCoupon_display_name(String coupon_display_name) {
        this.coupon_display_name = coupon_display_name;
    }

    public String getCelllphone() {
        return celllphone;
    }

    public void setCelllphone(String celllphone) {
        this.celllphone = celllphone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(int coupon_type) {
        this.coupon_type = coupon_type;
    }

    public int getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(int coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "MT{" +
                "id=" + id +
                ", coupon_display_name='" + coupon_display_name + '\'' +
                ", celllphone='" + celllphone + '\'' +
                ", status='" + status + '\'' +
                ", coupon_type=" + coupon_type +
                ", coupon_amount=" + coupon_amount +
                ", message='" + message + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
