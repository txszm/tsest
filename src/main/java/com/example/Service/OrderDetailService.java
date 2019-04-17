package com.example.Service;

import com.example.Entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void batchInsert(List<OrderDetail> orderDetailList);
}
