package com.example.Service.Impl;

import com.example.Bean.*;
import com.example.Dto.OrderDetailDto;
import com.example.Dto.OrderMasterDto;
import com.example.Entity.OrderDetail;
import com.example.Entity.OrderMaster;
import com.example.Entity.ProductInfo;
import com.example.Repository.OrderMasterRepository;
import com.example.Service.OrderDetailService;
import com.example.Service.OrderMasterService;
import com.example.Service.ProductInfoService;
import com.example.util.BigDecimalUtil;
import com.example.util.CustomException;
import com.example.util.IDUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        List<OrderDetailDto> items=orderMasterDto.getItems();
        List<OrderDetail> orderDetailList= Lists.newArrayList();
        BigDecimal totalPrice=new BigDecimal("0");
        for (OrderDetailDto item:items){
            ResultResponse<ProductInfo> resultResponse=productInfoService.queryById(item.getProductId());
            if (resultResponse.getCode()== ResultEnums.FAIL.getCode()){
                throw new CustomException(resultResponse.getMsg());
            }
            ProductInfo productInfo = resultResponse.getData();
            if (productInfo.getProductStock()<item.getProductQuantity()){
                throw new CustomException(ProductEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID()).productIcon(productInfo.getProductIcon())
                    .productId(item.getProductId()).productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice()).productQuantity(item.getProductQuantity())
                    .build();
            orderDetailList.add(orderDetail);
            //库存数量减少
            productInfo.setProductStock(productInfo.getProductStock()-item.getProductQuantity());
            productInfoService.updateProduct(productInfo);
            totalPrice= BigDecimalUtil.add(totalPrice,BigDecimalUtil.multi(productInfo.getProductPrice(),item.getProductQuantity()));
        }
        String orderId=IDUtils.createIdbyUUID();
        OrderMaster orderMaster =OrderMaster.builder().buyerAddress(orderMasterDto.getAddress()).buyerName(orderMasterDto.getName())
                .buyerOpenid(orderMasterDto.getOpenid()).orderStatus(OrderEnum.NEW.getCode())
                .payStatus(PayEnum.WAIT.getCode()).buyerPhone(orderMasterDto.getPhone())
                .orderId(orderId).orderAmount(totalPrice).build();
        List<OrderDetail> detailList=orderDetailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.batchInsert(detailList);
        orderMasterRepository.save(orderMaster);
        HashMap<String,String> map= Maps.newHashMap();
        map.put("orderId",orderId);
        return ResultResponse.success(map);
    }
}
