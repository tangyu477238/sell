package com.imooc.repository;

import com.imooc.dataobject.QrcodeOrderDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SqMax on 2018/3/31.
 */
public interface QrcodeOrderRepository extends JpaRepository<QrcodeOrderDO,String> {

    QrcodeOrderDO findByOrderNoAndBizDate(Integer orderNo, String bizDate);

}
