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
@Table(name = "biz_seat_yuding_order")
public class SeatYudingOrderDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@Id
	@GeneratedValue
	private Long id;
	//乘车日期
	private String bizDate;
	//乘车时间
	private String bizTime;
	//路线
	private Long routeId;
	//班次计划设置
	private Long planId;
	//座位信息
	private String info;
	//单价
	private BigDecimal price;
	//数量
	private BigDecimal num;
	//总金额(支付金额)
	private BigDecimal amout;
	//创建时间
	private Date createTime;
	//最迟支付时间
	private Date updateTime;
	//购买人
	private String createUser;
	//状态（0待支付1已支付）
	private Integer state;
	//备注
	private String remark;

	//出发
	private String fromStation;
	//目的站
	private String toStation;

	//名字
	private String userName;
	//手机号
	private String userMobile;


	//订单号
	private String orderNo;

	//上车点
	private String routeStation;


	//是否验票
	private Integer ckstate;


	//楼盘
	private String lp;
	//工作日
	private String workday;

	private BigDecimal useNum;

	private String endDate;
}
