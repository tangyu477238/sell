package com.imooc.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum DateTypeEnum {

    WEEK("1",7),
    MONTH("0",31),;

    private String code;
    private Integer num;

    DateTypeEnum(String code, Integer num){
        this.code=code;
        this.num=num;
    }

    public static Integer getNumByCode(String code){
        for(DateTypeEnum eachValue : DateTypeEnum.values()) {
            if (eachValue.getCode().equals(code)) {
                return eachValue.getNum();
            }
        }
        return MONTH.getNum();
    }


    public static void main(String[] args) {

        log.info(DateTypeEnum.getNumByCode(null)+"");
    }

}
