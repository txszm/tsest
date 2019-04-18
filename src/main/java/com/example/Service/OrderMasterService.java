package com.example.Service;

import com.example.Bean.ResultResponse;
import com.example.Dto.OrderMasterDto;

public interface OrderMasterService {
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);
    ResultResponse findDetail(String openId,String orderId);
    ResultResponse findList(String openId,Integer page,Integer size);
    ResultResponse cancel(String openId,String orderId);
}
