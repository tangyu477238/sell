package com.imooc.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
@Table(name = "biz_car_datetime_seat")
public class CarDatetimeSeatDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@Id
	@GeneratedValue
	private Long id;
	//日期
	private String bizDate;
	//时间
	private String bizTime;
	//车型
	private Long carId;
	//路线
	private Long planId;
	//座位名称
	private String name;
	//排数
	private BigDecimal rowIndex;
	//第几个座位
	private BigDecimal colIndex;
	//座位类型（1正常2过道3爱心）
	private Integer seatType;

	@Transient
	private String fromStation;

	@Transient
	private String toStation;


}
