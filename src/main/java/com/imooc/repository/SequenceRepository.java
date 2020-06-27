package com.imooc.repository;

import com.imooc.dataobject.SequenceDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface SequenceRepository extends JpaRepository<SequenceDO,Integer> {

    SequenceDO findByBizDate(String bizDate);


}



