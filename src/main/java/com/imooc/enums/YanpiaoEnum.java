package com.imooc.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum YanpiaoEnum {

    OK1("1","验票成功1张"),
    OK2("2","验票成功2张"),
    OK3("3","验票成功3张"),
    OK4("4","验票成功4张"),
    FAIL("fail","验票失败"),
    SUCCESS("success","验票成功"),
    BANCI("banci","班次不一致"),
    BUPIAO("bupiao","补票验票成功"),
    DOUBLE("double","此车票重复");

    private String code;
    private String message;

    YanpiaoEnum(String code, String message){
        this.code=code;
        this.message=message;
    }

    public static String getMessageByCode(String code){
        for(YanpiaoEnum eachValue : YanpiaoEnum.values()) {
            if (eachValue.getCode().equals(code)) {
                return eachValue.getMessage();
            }
        }
        return YanpiaoEnum.FAIL.getMessage();
    }


    public static void main(String[] args) {
        String s = YanpiaoEnum.getMessageByCode("1");
        log.info(s);
    }

}
