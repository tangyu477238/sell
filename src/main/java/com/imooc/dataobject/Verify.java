package com.imooc.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@DynamicUpdate
@Data
@Table(name = "biz_verify")
public class Verify {
    /**类目id.**/
    @Id
    @GeneratedValue
    private Integer id;

    private String mobile;


    private String uid;

    private Date createTime;

    private String verify;

    private Integer ptype;

    private Integer num;

}
