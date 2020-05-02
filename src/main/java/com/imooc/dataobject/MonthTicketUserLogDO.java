package com.imooc.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 用户月票购买记录
 * 
 * @author tzy
 * @email tangzhiyu@vld-tech.com
 * @date 2018-12-25 21:42:59
 */
@Entity
@DynamicUpdate
@Data
@Table(name = "biz_month_ticket_user_log")
public class MonthTicketUserLogDO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	//id
	private Long id;
	//套票类型
	private Long ptypeId;
	//套票名称
	private String ptypeName;
	//购买人
	private String createUser;
	//票价
	private BigDecimal price;
	//总次数
	private BigDecimal totalNum;
	//使用次数
	private BigDecimal useNum;
	//月份
	private String month;
	//描述
	private String remark;
	//创建时间
	private Date createTime;
	//
	private Date updateTime;

	private String orderNo;

	//楼盘
	private String lp;
}
