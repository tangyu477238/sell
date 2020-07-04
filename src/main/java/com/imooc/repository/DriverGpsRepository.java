package com.imooc.repository;

import com.imooc.dataobject.DriverGpsDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SqMax on 2018/3/31.
 */
public interface DriverGpsRepository extends JpaRepository<DriverGpsDO,String> {

    DriverGpsDO findByRouteIdAndBizDateAndBizTime(Long routeId, String bizDate, String bizTime);

}
