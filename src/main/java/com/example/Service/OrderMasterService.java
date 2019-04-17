package com.example.Service;

import com.example.Bean.ResultResponse;
import com.example.Dto.OrderMasterDto;

public interface OrderMasterService {
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);
}
