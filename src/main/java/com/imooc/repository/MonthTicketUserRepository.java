package com.imooc.repository;

import com.imooc.dataobject.MonthTicketUserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface MonthTicketUserRepository extends JpaRepository<MonthTicketUserDO,Integer> {

    @Query(value = "select * from biz_month_ticket_user where remark = 1 and create_user=?1 and `month` >=?2 ", nativeQuery = true)
    List<MonthTicketUserDO> getMonthByUser(String uid,String month);

    List<MonthTicketUserDO> findByCreateUserAndRemarkOrderByIdDesc(String uid,String remark);

    MonthTicketUserDO findByCreateUserAndMonthAndRemarkAndLp(String uid,String month,String remark, String lp);

    MonthTicketUserDO findByOrderNoAndRemark(String orderNo,String remark);
}



