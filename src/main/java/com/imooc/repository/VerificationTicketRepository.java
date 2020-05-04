package com.imooc.repository;

import com.imooc.dataobject.VerificationTicketDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface VerificationTicketRepository extends JpaRepository<VerificationTicketDO,Integer> {

    //班次
    @Query(value = "select distinct biz_time  from  biz_car_datetime_seat v  "
            +" inner join biz_plan_price p on p.id= v.plan_id "
            +" where p.route_id =?1  and  v.biz_date = ?2   order by biz_time" , nativeQuery = true)
    List<String> getTime(String route, String bizDate);

}



