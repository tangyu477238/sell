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
 * @date 2018-12-25 21:42:55
 */

@Entity
@DynamicUpdate
@Data
@Table(name = "biz_month_ticket")
public class MonthTicketDO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	//id
	private Long id;
	//套票名称
	private String ptypeName;
	//票价
	private BigDecimal price;
	//总次数
	private BigDecimal totalNum;
	//0无效1有效
	private Integer state;

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
	 * 设置：套票名称
	 */
	public void setPtypeName(String ptypeName) {
		this.ptypeName = ptypeName;
	}
	/**
	 * 获取：套票名称
	 */
	public String getPtypeName() {
		return ptypeName;
	}
	/**
	 * 设置：票价
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * 获取：票价
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * 设置：总次数
	 */
	public void setTotalNum(BigDecimal totalNum) {
		this.totalNum = totalNum;
	}
	/**
	 * 获取：总次数
	 */
	public BigDecimal getTotalNum() {
		return totalNum;
	}
	/**
	 * 设置：0无效1有效
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：0无效1有效
	 */
	public Integer getState() {
		return state;
	}
}
