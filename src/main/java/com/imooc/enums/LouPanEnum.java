package com.imooc.enums;

import lombok.Getter;

@Getter
public enum LouPanEnum {

    XINGFUYU("1","万科幸福誉"),
    BEIBUWANKE("2","北部万科城");

    private String code;
    private String message;

    LouPanEnum(String code,String message){
        this.code=code;
        this.message=message;
    }
}
