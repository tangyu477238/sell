package com.imooc.repository;

import com.imooc.dataobject.VerificationTicketDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface VerificationTicketRepository extends JpaRepository<VerificationTicketDO,Integer> {

    @Query(value = "select password from sys_user where username =?1 ", nativeQuery = true)
    String getPassword(String username);

    //班次
    @Query(value = "select distinct biz_time  from  biz_car_datetime_seat v  "
            +" inner join biz_plan_price p on p.id= v.plan_id "
            +" where p.route_id =?1  and  v.biz_date = ?2  order by biz_time" , nativeQuery = true)
    List<String> getTime(String route, String bizDate);

    @Query(value = "select MAX(biz_date) as biz_date from biz_calendar"
            +" WHERE holiday = ?1 and biz_date<=CURDATE() ", nativeQuery = true)
    String getCalendar(String holiday);

    @Modifying
    @Transactional
    @Query(value = "update biz_sellday set days = ?1  ",nativeQuery = true)
    int updateSellday(Integer days);

    @Query(value = "select " +
            "l.route_id,l.biz_date,l.biz_time,l.from_station,l.to_station,l.info,l.user_mobile,l.user_name ,MAX(l.update_time) as update_time,str_to_date(CONCAT(l.biz_date,l.biz_time,':00'), '%Y-%m-%d %H:%i:%s') as totime" +
            " from biz_seat_order_log l" +
            " left join biz_seat_order s on s.order_no = l.order_no " +
            " where  s.order_no is null  and l.update_time > date_sub(str_to_date(CONCAT(l.biz_date,l.biz_time,':00'), '%Y-%m-%d %H:%i:%s'),interval 2 MINUTE)" +
            " and l.user_mobile =?1 " +
            " group by l.route_id,l.biz_date,l.biz_time,l.info,l.user_mobile,l.user_name ,l.from_station,l.to_station" +
            " order by l.biz_date desc,l.biz_time desc ",nativeQuery = true)
    List<Object[]>  black(String mobile);

    @Modifying
    @Transactional
    @Query(value = "update biz_blacklist set openid = id ,mobile = concat(mobile,'x') where mobile =?1 ",nativeQuery = true)
    int updateBlack(String mobile);

    @Modifying
    @Transactional
    @Query(value = "update biz_car_datetime_seat set seat_Type = 0 where route_id =?1 and biz_date =?2 and biz_time =?3 ",nativeQuery = true)
    int lockAll(Long route, String bizDate, String bizTime);
}



