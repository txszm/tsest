package com.example.Bean;

import lombok.Getter;

@Getter
public enum  OrderEnum {
    NEW(0,"新建订单"),
    FINSH(1,"已完成订单"),
    CANCEL(2,"已取消"),
    ORDER_NOT_EXITS(3,"订单不存在"),
    AMOUNT_CHECK_ERROR(4,"金额不一样");
    private int code;
    private String msg;

    OrderEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
