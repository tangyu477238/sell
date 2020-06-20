package com.imooc.repository;

import com.imooc.dataobject.SeatOrderDO;
import org.springframework.data.jpa.repository.JpaRepository;

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
}