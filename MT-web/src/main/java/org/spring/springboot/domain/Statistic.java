package org.spring.springboot.domain;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.ToString;

import org.spring.springboot.util.DateUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

//import org.springframework.data.annotation.Id;

//@Data
//@ToString(callSuper = false)
//@NoArgsConstructor
//@AllArgsConstructor
@Document(collection = "statistics")
public class Statistic  implements Serializable {
    @Id
    private String id ;

    private Date create;
    private String phone;
    private String message;
    private String coupon_display_name;


    public String getCoupon_display_name() {
        return coupon_display_name;
    }

    public void setCoupon_display_name(String coupon_display_name) {
        this.coupon_display_name = coupon_display_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
