package com.imooc.repository;

import com.imooc.dataobject.SeatOrderDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface SeatOrderRepository extends JpaRepository<SeatOrderDO,Integer> {

    SeatOrderDO findByIdAndState(Long id,int state);

    SeatOrderDO findByOrderNoAndState(String orderNo,int state);

    SeatOrderDO findByIdAndStateAndCreateUser(Long id,Integer state,String createUser);

    List<SeatOrderDO> findByStateAndCreateUserOrderByIdDesc(int state, String uid);

    SeatOrderDO findByOrderNo(String orderNo);

    List<SeatOrderDO> findByCreateUserAndRouteIdAndBizDateAndBizTimeAndStateAndCkstate(String createUser,
                                                               Long routeId,
                                                               String bizDate,
                                                               String bizTime,
                                                               Integer state,
                                                               Integer ckState);

    List<SeatOrderDO> findByRouteIdAndBizDateAndBizTimeAndInfoAndState(Long routeId,
                                                               String bizDate,
                                                               String bizTime,
                                                               String info,
                                                               Integer state);

    @Query(value = "select * from biz_seat_order "
            +" where  (info LIKE CONCAT(?1,'%')  or info LIKE CONCAT('%,',?1,'%') )  and route_id =?2 "
            +" and biz_date =?3 and biz_time =?4 " , nativeQuery = true)
    SeatOrderDO getOrderBySeat(String info,
                              Long routeId,
                              String bizDate,
                              String bizTime);

//    @Query(value = "select * from biz_seat_order where biz_date = '2020-06-30' and create_time <'2020-06-28 21:01:22'", nativeQuery = true)
//    List<SeatOrderDO> getorderlist();

    List<SeatOrderDO> findByRouteIdAndBizDateAndBizTimeAndState(Long routeId,
                                                                       String bizDate,
                                                                       String bizTime,
                                                                       Integer state);
}