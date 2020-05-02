package com.imooc.repository;

import com.imooc.dataobject.MonthTicketDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface MonthTicketRepository extends JpaRepository<MonthTicketDO,Integer> {

    List<MonthTicketDO> findByStateAndLp(int state, String lp);

    MonthTicketDO  findById(Long id);

}



