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
@Table(name = "biz_verification_ticket")
public class VerificationTicketDO implements Serializable {
	private static final long serialVersionUID = 1L;


	//id
	@Id
	@GeneratedValue
	private Long id;

	private Long orderId;
	//更新时间
	private Date updateTime;
	private String createUser;


	//乘车日期
	private String bizDate;
	//乘车时间
	private String bizTime;
	//路线
	private Long routeId;

	private String routeStation;

	//名字
	private String userName;
	//手机号
	private String userMobile;
	//座位信息
	private String info;
	//数量
	private BigDecimal num;
}
