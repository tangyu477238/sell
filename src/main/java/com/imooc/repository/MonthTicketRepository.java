package com.imooc.repository;

import com.imooc.dataobject.MonthTicketDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface MonthTicketRepository extends JpaRepository<MonthTicketDO,Integer> {

    MonthTicketDO  findByState(int state);

    MonthTicketDO  findById(Long id);

}



