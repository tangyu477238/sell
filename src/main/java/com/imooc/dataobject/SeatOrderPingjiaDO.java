package com.imooc.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 
 * 
 * @author tzy
 * @email tangzhiyu@vld-tech.com
 * @date 2018-12-22 14:48:50
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Data
@Table(name = "biz_seat_order_pingjia")
public class SeatOrderPingjiaDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@Id
	@GeneratedValue
	private Long id;
	//订单id
	private Long orderId;

	private Integer fuwu;

	private String content;



}
