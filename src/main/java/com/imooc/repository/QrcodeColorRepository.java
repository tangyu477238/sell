package com.imooc.repository;

import com.imooc.dataobject.QrcodeColorDO;
import com.imooc.dataobject.QrcodeOrderDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SqMax on 2018/3/31.
 */
public interface QrcodeColorRepository extends JpaRepository<QrcodeColorDO,String> {

    QrcodeColorDO findByRouteIdAndBizDate(Long route,String bizDate);
}
