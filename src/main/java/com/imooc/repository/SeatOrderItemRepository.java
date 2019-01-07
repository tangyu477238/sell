package com.imooc.repository;

import com.imooc.dataobject.SeatOrderDO;
import com.imooc.dataobject.SeatOrderItemDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface SeatOrderItemRepository extends JpaRepository<SeatOrderItemDO,Integer> {

    List<SeatOrderItemDO> findByOrderId(Long id);

}