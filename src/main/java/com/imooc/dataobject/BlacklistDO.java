package com.imooc.dataobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by SqMax on 2018/3/31.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Data
@Table(name = "biz_blacklist")
public class BlacklistDO implements Serializable {
    private static final long serialVersionUID = 1L;
    //id
    @Id
    @GeneratedValue
    private Long id;

    private String openid;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String mobile;
    private String name;

}
