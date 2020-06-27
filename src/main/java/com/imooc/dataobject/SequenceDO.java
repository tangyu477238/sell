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
@Table(name = "biz_sequence")
public class SequenceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//id
	@Id
	@GeneratedValue
	private Long id;

	private String bizDate;

	private Integer num;

	//楼盘
	private String lp;
}
