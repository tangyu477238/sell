package com.imooc.repository;

import com.imooc.dataobject.SeatOrderItemDO;
import com.imooc.dataobject.SeatOrderItemLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface SeatOrderItemLogRepository extends JpaRepository<SeatOrderItemLogDO,Integer> {

    List<SeatOrderItemLogDO> findByOrderId(Long id);

}