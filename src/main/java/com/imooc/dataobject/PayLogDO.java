package com.imooc.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * InnoDB free: 11264 kB
 * 
 * @author tzy
 * @email tangzhiyu@vld-tech.com
 * @date 2018-12-20 23:00:58
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Data
@Table(name = "biz_pay_log")
public class PayLogDO implements Serializable {
	private static final long serialVersionUID = 1L;


	//id
	@Id
	@GeneratedValue
	private Long id;
	private String orderNo;
	private BigDecimal amout;
	private Date createTime;
	private Integer state;
	private Date updateTime;


}
