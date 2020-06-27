package com.imooc.repository;

import com.imooc.dataobject.JpushDo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SqMax on 2018/3/31.
 */
public interface JpushRepository extends JpaRepository<JpushDo,String> {

    JpushDo findByUid(String uid);

}
