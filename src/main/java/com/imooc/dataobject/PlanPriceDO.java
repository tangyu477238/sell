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



/**
 * InnoDB free: 11264 kB
 * 
 * @author tzy
 * @email tangzhiyu@vld-tech.com
 * @date 2018-12-20 23:33:11
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Data
@Table(name = "biz_plan_price")
public class PlanPriceDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@Id
	@GeneratedValue
	private Long id;
	//路线
	private Long planId;
	//业主票价
	private BigDecimal price;
	//非业主票价
	private BigDecimal fprice;
	//车型
	private Long carId;
	//工作日
	private String sday;
	//限制非业主购买班次
	private String fday;
	//节假日班次
	private String wday;






}
