package com.imooc.repository;

import com.imooc.dataobject.MonthTicketUserDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface MonthTicketUserRepository extends JpaRepository<MonthTicketUserDO,Integer> {


    MonthTicketUserDO findByCreateUserAndMonthAndRemark(String uid,String month,String remark);

    List<MonthTicketUserDO> findByCreateUser(String uid);

    MonthTicketUserDO findByCreateUserAndMonth(String uid,String month);

    MonthTicketUserDO findByOrderNoAndRemark(String orderNo,String remark);
}



