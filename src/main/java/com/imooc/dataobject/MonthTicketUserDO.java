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
@Table(name = "biz_month_ticket_user")
public class MonthTicketUserDO implements Serializable {
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


	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

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
	 * 设置：套票类型
	 */
	public void setPtypeId(Long ptypeId) {
		this.ptypeId = ptypeId;
	}
	/**
	 * 获取：套票类型
	 */
	public Long getPtypeId() {
		return ptypeId;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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
	 * 设置：使用次数
	 */
	public void setUseNum(BigDecimal useNum) {
		this.useNum = useNum;
	}
	/**
	 * 获取：使用次数
	 */
	public BigDecimal getUseNum() {
		return useNum;
	}
	/**
	 * 设置：月份
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * 获取：月份
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * 设置：描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：描述
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
