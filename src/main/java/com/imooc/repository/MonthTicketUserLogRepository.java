package com.imooc.repository;

import com.imooc.dataobject.MonthTicketUserDO;
import com.imooc.dataobject.MonthTicketUserLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface MonthTicketUserLogRepository extends JpaRepository<MonthTicketUserLogDO,Integer> {


//    MonthTicketUserLogDO findByCreateUserAndMonthAndRemark(String uid, String month, String remark);

    List<MonthTicketUserLogDO> findByCreateUserAndRemarkOrderByIdDesc(String uid, String remark);

    MonthTicketUserLogDO findByCreateUserAndMonth(String uid, String month);

    MonthTicketUserLogDO findByOrderNoAndRemark(String orderNo, String remark);

    MonthTicketUserLogDO findByCreateUserAndMonthAndRemarkAndLp(String uid, String month, String remark, String lp);

}



