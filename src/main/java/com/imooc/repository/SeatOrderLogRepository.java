package com.imooc.repository;

import com.imooc.dataobject.SeatOrderLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

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
}