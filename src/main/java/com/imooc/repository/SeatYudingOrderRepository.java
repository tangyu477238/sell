package com.imooc.repository;

import com.imooc.dataobject.SeatYudingOrderDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 *
 */
public interface SeatYudingOrderRepository extends JpaRepository<SeatYudingOrderDO, Long> {

    @Query(value = "select biz_time,route_id from biz_seat_yuding ", nativeQuery = true)
    List<Object[]> getYudingTimes();


    @Query(value = "select * from biz_seat_yuding_order where state = 1 and workday =?1 and route_id =?2 and LEFT(biz_date,7)=?3 and create_user =?4 ", nativeQuery = true)
    List<SeatYudingOrderDO> listYuding(String holiday, String route, String month, String uid);

    @Query(value = "SELECT count(1) as sl from biz_calendar   "
            + " where holiday =?1  and biz_date >=?2 and biz_date<=?3 " , nativeQuery = true)
    List<BigInteger> getWorkNum(String holiday, String biz_date, String endDate);


    @Query(value = "select yy.*"
            + " from biz_seat_yuding_order yy "
            + " inner join biz_calendar c on c.biz_date =?1 and yy.workday = c.holiday "
            + " left join biz_seat_order s on s.biz_date =?1 and s.biz_time = yy.biz_time and s.route_id = yy.route_id"
            + " and s.state = 1 and s.remark='预约出票' and yy.create_user = s.create_user"
            + " where yy.biz_date<=?1 and ?1<=yy.end_date   and yy.state = 1 and s.create_user is null ", nativeQuery = true)
    List<SeatYudingOrderDO> listSeatYuding(String bizDate);



    @Query(value = "select e.id,e.name"
            + " FROM biz_car_datetime_seat e "
            + " left join biz_seat_order_item o on o.seat_id=e.id"
            + " where e.route_id =?1 and e.biz_date =?2 and e.biz_time =?3  and e.seat_type = 1 and o.seat_id  is null ", nativeQuery = true)
    List<Object[]> getSeatInfo(Long route,String bizDate,String bizTime);

    @Query(value = "SELECT *  from biz_seat_yuding_order where end_date>=CURDATE() and create_user=?1 order by id desc", nativeQuery = true)
    List<SeatYudingOrderDO> listYudingOrder(String uid);

    @Query(value = "select DISTINCT plan_id,price from biz_car_datetime_seat where  route_id =?1  and biz_date = ?2 and biz_time = ?3 ", nativeQuery = true)
    List<Object[]> getPlanPrice(String route, String bizDate, String bizTime);

    @Query(value = "SELECT MAX(biz_date) as biz_date from ("
            + " select biz_date  from biz_calendar "
            + " WHERE holiday = ?1 and biz_date>=?2 "
            + " order by biz_date asc LIMIT 0,?3 ) t ", nativeQuery = true)
    String getEndDateNum(String holiday, String startDate, Integer dayNum);


    @Query(value = "select holiday from biz_calendar WHERE biz_date=?1 ", nativeQuery = true)
    String getHoliday(String bizDate);


    @Query(value = "SELECT MIN(biz_date) as biz_date from ("
            + " select biz_date  from biz_calendar "
            + " WHERE holiday = ?1 and biz_date>=?2 ) t ", nativeQuery = true)
    String getStartDate(String holiday, String bizDate);

    @Query(value = "SELECT MAX(biz_date) as biz_date from ("
            + " select biz_date  from biz_calendar "
            + " WHERE holiday = ?1 and biz_date>=?2 and LEFT(biz_date,7)=?3  order by biz_date LIMIT 0,?4 ) t ", nativeQuery = true)
    String getLastDate(String holiday, String bizDate, String month, int dateTypeNum);

    @Query(value = "select create_user from biz_seat_order where biz_date > ?1 group by create_user ", nativeQuery = true)
    List<String> listLastOrderCreateUser(String bizDate);


    @Query(value = "select"
            + " CONCAT(t.route_id,t.biz_date,t.biz_time) as rdt,"
            + " case when (t.zuo-IFNULL(t1.num,0)) < 1 then '售完' when t.zuo-IFNULL(t1.num,0)<15 then '少量'  else '' end as mzl"
            + " from ("
            + " select"
            + " s.route_id,s.biz_date,s.biz_time,SUM(s.seat_type) as zuo"
            + " from biz_car_datetime_seat s"
            + " where s.route_id =?1 and s.biz_date =?2   "
            + " group by s.route_id,s.biz_date,s.biz_time"
            + " ) t"
            + " left join ("
            + " select"
            + " route_id,biz_date,biz_time,IFNULL(SUM(num),0) as num"
            + " from biz_seat_order s"
            + " where s.route_id =?1 and s.biz_date =?2  "
            + " group by s.biz_date,s.biz_time,s.route_id"
            + " ) t1 on t.biz_date=t1.biz_date  and t.biz_time = t1.biz_time and t.route_id = t1.route_id", nativeQuery = true)
    List<Object[]> getMzl(Long route, String bizDate);
}