package com.imooc.repository;

import com.imooc.dataobject.Callplan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface BuyTicketRepository extends JpaRepository<Callplan,Integer> {


    @Query(value = "select  r.from_Station as fromStation,r.to_Station as toStation,pp.id "
            +" from biz_car_datetime_seat p  "
            +" inner join biz_plan_price  pp on p.plan_id=pp.id "
            +" inner join biz_route r on r.id = pp.route_id "
            +" where str_to_date(CONCAT(biz_date,biz_time), '%Y-%m-%d %H:%i:%s')>NOW() "
            +"  GROUP BY pp.id,r.from_Station,r.to_Station ", nativeQuery = true)
    List<Object[]> listRoute();

    @Query(value = "select e.row_Index,e.col_Index,e.seat_Type,o.seat_id from biz_car_datetime_seat e "
            +" left join biz_seat_order_item o on o.seat_id=e.id "
            +"  where e.plan_id = ?1 and e.biz_date = ?2 and e.biz_time = ?3 order by e.row_index,e.col_index", nativeQuery = true)
    List<Object[]> listSeatDetail(String route,String time, String moment);

    @Query(value = "select  e.id,e.name,o.seat_id  FROM biz_car_datetime_seat  e "
            +" left join biz_seat_order_item o on o.seat_id=e.id "
            +" where e.plan_id = ?1 and e.biz_date = ?2 and e.biz_time = ?3  and e.name in (?4)", nativeQuery = true)
    List<Object[]> listSeatOder(String route,String time, String moment,String seatNames []);

    @Query(value = "select r.from_station, r.to_station,p.price from  biz_plan_price p "
            +" inner join biz_route r on r.id = p.route_id "
            +"  where p.id = ?1 ", nativeQuery = true)
    List<Object[]> getPlanPrice(String route);

    @Modifying
    @Transactional
    @Query(value = "delete from biz_seat_order  where id = ?1", nativeQuery = true)
    int deleteByOrdId(String orderId);

    @Modifying
    @Transactional
    @Query(value = "delete from biz_seat_order_item  where order_id = ?1", nativeQuery = true)
    int deleteByOrderPid(String orderId);

    //班次
    @Query(value = "select distinct biz_time  from  biz_car_datetime_seat v  "
            +" where plan_id =?1  and  biz_date = ?2 and str_to_date(CONCAT(biz_date,biz_time), '%Y-%m-%d %H:%i:%s')>NOW() " , nativeQuery = true)
    List<String> getBuyTime(String route, String time);

    //补票班次
    @Query(value = "select DATE_FORMAT(bizTime,'%H:%i') as bizTime  from (select MAX(str_to_date(CONCAT(biz_date, biz_time),'%Y-%m-%d %H:%i:%s')) AS bizTime  from  biz_car_datetime_seat v  "
            +" where plan_id =?1  and  biz_date = DATE_FORMAT(now(),'%Y-%m-%d') and str_to_date(CONCAT(biz_date,biz_time), '%Y-%m-%d %H:%i:%s')<NOW() ) t" , nativeQuery = true)
    List<String> getBuyTimeBp(String route);

    //放票天数、放票时间
    @Query(value = "select days,case when NOW()>str_to_date(CONCAT(date_format(now(),'%Y-%m-%d'),time), '%Y-%m-%d %H:%i:%s') then 1 else 0 end as flag "
            +"  from biz_sellday " , nativeQuery = true)
    List<Object[]> getDayTimeFlag();

    //月票剩余
    @Query(value = "select SUM(num) as num from biz_seat_order  "
            +" where remark='月票抵扣' and state ='1' and create_user =?1 "
            +" and biz_date =?2 and biz_time =?3 and plan_id =?4 " , nativeQuery = true)
    List<BigDecimal> getBuyMonthNum(String createUser, String bizDate, String bizTime,Long planId);

    //补票线路
    @Query(value = "select fromStation,toStation,bizDate,id,DATE_FORMAT(bizTime,'%H:%i') as bizTime  from (select  r.from_Station as fromStation,r.to_Station as toStation," +
            "biz_date as bizDate,pp.id,MAX(str_to_date(CONCAT(biz_date, biz_time),'%Y-%m-%d %H:%i:%s')) AS bizTime "
            +" from biz_car_datetime_seat p "
            +" inner join biz_plan_price  pp on p.plan_id=pp.id "
            +" inner join biz_route r on r.id = pp.route_id "
            +" where biz_date=DATE_FORMAT(now(),'%Y-%m-%d')  and str_to_date(CONCAT(biz_date,biz_time), '%Y-%m-%d %H:%i:%s')<NOW() "
            +" GROUP BY biz_date,pp.id,r.from_Station,r.to_Station  ) t" , nativeQuery = true)
    List<Object[]> listBupiao();

    //查询车票预定量
    @Query(value = "SELECT SUM(num) as num  from biz_seat_order s "
            + " where plan_id =?1 and biz_date =?2 and biz_time=?3  and create_user =?4 " , nativeQuery = true)
    List<BigDecimal> getBuyCarNum(String route, String time, String moment, String uid);




}



