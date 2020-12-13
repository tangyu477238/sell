package com.imooc.repository;

import com.imooc.dataobject.SeatOrderLogDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface SeatOrderLogRepository extends JpaRepository<SeatOrderLogDO,Integer> {

    SeatOrderLogDO findByIdAndState(Long id, int state);

    SeatOrderLogDO findByOrderNoAndState(String orderNo, int state);

    SeatOrderLogDO findByIdAndStateAndCreateUser(Long id, Integer state, String createUser);

    List<SeatOrderLogDO> findByStateAndCreateUserOrderByIdDesc(int state, String uid);

    SeatOrderLogDO findByOrderNo(String orderNo);


    @Query(value = "select l.create_user,l.from_station,l.to_station,MAX(l.update_time) as update_time, "
            +" str_to_date(CONCAT(l.biz_date,l.biz_time,':00'), '%Y-%m-%d %H:%i:%s') as totime,l.user_name ,COUNT(1) ts,l.route_id,l.biz_date,l.biz_time,l.info,l.user_mobile "
            +" from biz_seat_order_log l "
            +" left join biz_seat_order s on s.order_no = l.order_no "
            +" where  s.order_no is null  and l.update_time > date_sub(str_to_date(CONCAT(l.biz_date,l.biz_time,':00'), '%Y-%m-%d %H:%i:%s'),interval 2 MINUTE) "
            +" and l.biz_date =curdate()  and l.update_time>DATE_ADD(NOW(),INTERVAL -5 MINUTE)  "
            +" group by l.route_id,l.biz_date,l.biz_time,l.info,l.user_mobile,l.user_name,l.from_station,l.to_station,l.create_user", nativeQuery = true)
    List<Object[]> getwarningOrderUsers();


    @Query(value = "select"
            +" user_mobile,create_user,user_name,COUNT(1)  as cishu"
            +" from ("
                +" select "
                +" l.route_id,l.biz_date,l.biz_time,l.info,l.user_mobile,l.user_name ,l.create_user,COUNT(1) ts,MAX(l.update_time) as update_time,"
                +" str_to_date(CONCAT(l.biz_date,l.biz_time,':00'), '%Y-%m-%d %H:%i:%s') as totime"
                +" from biz_seat_order_log l"
                +" left join biz_seat_order s on s.order_no = l.order_no"
                +" where  s.order_no is null and l.update_time > date_sub(str_to_date(CONCAT(l.biz_date,l.biz_time,':00'), '%Y-%m-%d %H:%i:%s'),interval 2 MINUTE)"
                +" and l.user_mobile =?1"
                +" group by l.route_id,l.biz_date,l.biz_time,l.info,l.user_mobile,l.user_name ,l.create_user"
            +" ) t"
            +" group by user_mobile,user_name,create_user"
            +" HAVING COUNT(1)>2 ", nativeQuery = true)
    List<Object[]> addBlacklist(String mobile);
}