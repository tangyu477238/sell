package com.imooc.service.impl;

import com.imooc.dataobject.SellerInfo;
import com.imooc.repository.SellerInfoRepository;
import com.imooc.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * Created by SqMax on 2018/4/1.
 */
@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }

    @Override
    public void addUser(String openid) {
        SellerInfo si = new SellerInfo();
        si.setSellerId(UUID.randomUUID().toString().replace("-",""));
        si.setUsername(UUID.randomUUID().toString().replace("-",""));
        si.setPassword(UUID.randomUUID().toString().replace("-",""));
        si.setOpenid(openid);
        si.setCreateTime(new Date());
        si.setUpdateTime(new Date());
        repository.save(si);

    }
}
