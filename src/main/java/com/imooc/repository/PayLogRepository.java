package com.imooc.repository;

import com.imooc.dataobject.PayLogDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface PayLogRepository extends JpaRepository<PayLogDO,Integer> {

    List<PayLogDO> findByState(int state);

}



