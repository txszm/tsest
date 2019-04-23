package com.example.Service;

import com.example.Entity.OrderMaster;
import com.lly835.bestpay.model.PayResponse;

public interface PayService {
    OrderMaster findOrderById(String orderId);
    PayResponse create(OrderMaster orderMaster);
    void weixin_notify(String notifyData);
}
