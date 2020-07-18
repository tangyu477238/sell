package com.imooc.repository;

import com.imooc.dataobject.BlacklistDO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SqMax on 2018/3/31.
 */
public interface BlacklistRepository extends JpaRepository<BlacklistDO,String> {

    List<BlacklistDO>  findByOpenidOrMobile(String openid, String mobile);

}
