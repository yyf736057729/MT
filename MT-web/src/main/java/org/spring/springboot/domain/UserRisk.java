package org.spring.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author yuyunfeng
 * @create_time 2018/11/30
 * @describe ${class}
 */

@Data
@ToString(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "use_risk_management")
public class UserRisk {
    public Long id ;
    public String cellPhone ;
    public String status;
    public String message;
    public String phone;
    public Date Create;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getCreate() {
        return Create;
    }

    public void setCreate(Date create) {
        Create = create;
    }
}
