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

    @Query(value = "select * from biz_seat_yuding_order where workday =?1 and route_id =?2 and LEFT(biz_date,7)=?3 and create_user =?4 ", nativeQuery = true)
    List<SeatYudingOrderDO> listYuding(String holiday, String route, String month, String uid);

    @Query(value = "SELECT count(1) as sl from biz_calendar   "
            + " where holiday =?1  and biz_date >=?2 and left(biz_date,7) =?3 " , nativeQuery = true)
    List<BigInteger> getWorkNum(String holiday, String biz_date, String month);



    @Query(value = "select yy.*"
            + " from biz_seat_yuding_order yy "
            + " inner join biz_calendar c on c.biz_date = CURDATE() and yy.workday = c.holiday "
            + " left join biz_seat_order s on s.biz_date = CURDATE() and s.biz_time = yy.biz_time and s.route_id = yy.route_id"
            + " and s.state = 1 and s.remark='预约出票' and yy.create_user = s.create_user"
            + " where yy.biz_date like '2021-01%' and yy.biz_date<=CURDATE()  and yy.state = 1 and s.create_user is null ", nativeQuery = true)
    List<SeatYudingOrderDO> listSeatYuding();



    @Query(value = "select e.id,e.name"
            + " FROM biz_car_datetime_seat e "
            + " left join biz_seat_order_item o on o.seat_id=e.id"
            + " where e.route_id =?1 and e.biz_date =?2 and e.biz_time =?3  and e.seat_type = 1 and o.seat_id  is null ", nativeQuery = true)
    List<Object[]> getSeatInfo(Long route,String bizDate,String bizTime);

    @Query(value = "SELECT *  from biz_seat_yuding_order where end_date>=CURDATE() and create_user=?1 order by id desc", nativeQuery = true)
    List<SeatYudingOrderDO> listYudingOrder(String uid);
}