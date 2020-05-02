package com.imooc.repository;

import com.imooc.dataobject.MonthTicketUserDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface MonthTicketUserRepository extends JpaRepository<MonthTicketUserDO,Integer> {


//    MonthTicketUserDO findByCreateUserAndMonthAndRemark(String uid,String month,String remark);

    List<MonthTicketUserDO> findByCreateUserAndRemarkOrderByIdDesc(String uid,String remark);

    MonthTicketUserDO findByCreateUserAndMonthAndRemarkAndLp(String uid,String month,String remark, String lp);

    MonthTicketUserDO findByOrderNoAndRemark(String orderNo,String remark);
}



