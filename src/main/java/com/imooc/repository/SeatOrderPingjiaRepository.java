package com.imooc.repository;

import com.imooc.dataobject.SeatOrderPingjiaDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface SeatOrderPingjiaRepository extends JpaRepository<SeatOrderPingjiaDO,Long> {

    SeatOrderPingjiaDO findByOrderId(Long id);

}