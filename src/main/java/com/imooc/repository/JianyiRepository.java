package com.imooc.repository;

import com.imooc.dataobject.JianyiDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SqMax on 2018/3/17.
 */
public interface JianyiRepository extends JpaRepository<JianyiDO,Integer> {

//    JianyiDO findByIdAndState(Long id, int state);

}