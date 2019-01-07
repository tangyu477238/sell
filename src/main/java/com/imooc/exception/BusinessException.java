package com.imooc.exception;

import lombok.Data;

/**
 * @Auther: Administrator
 * @Date: 2019\1\5 0005 23:23
 * @Description:
 */
@Data
public class BusinessException extends RuntimeException {

    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;


}