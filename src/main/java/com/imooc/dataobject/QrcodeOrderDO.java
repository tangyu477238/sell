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
 * 
 * 
 * @author tzy
 * @email tangzhiyu@vld-tech.com
 * @date 2018-12-22 14:48:49
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Data
@Table(name = "biz_qrcode_order")
public class QrcodeOrderDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@Id
	@GeneratedValue
	private Long id;
	//楼盘
	private String lp;
	//路线
	private Long routeId;
	//乘车日期
	private String bizDate;
	//乘车时间
	private String bizTime;
	//创建时间
	private Date createTime;
	//订单号
	private Integer orderNo;




}
