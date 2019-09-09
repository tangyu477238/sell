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
@Table(name = "biz_seat_order_item_log")
public class SeatOrderItemLogDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@Id
	@GeneratedValue
	private Long id;
	//座位id
	private Long seatId;
	//订单id
	private Long orderId;

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：座位id
	 */
	public void setSeatId(Long seatId) {
		this.seatId = seatId;
	}
	/**
	 * 获取：座位id
	 */
	public Long getSeatId() {
		return seatId;
	}
	/**
	 * 设置：订单id
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：订单id
	 */
	public Long getOrderId() {
		return orderId;
	}
}
