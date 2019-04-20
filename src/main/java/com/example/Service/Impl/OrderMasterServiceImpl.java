package com.example.Service.Impl;

import com.example.Bean.*;
import com.example.Dto.OrderDetailDto;
import com.example.Dto.OrderDetaliAndMaster;
import com.example.Dto.OrderMasterDto;
import com.example.Entity.OrderDetail;
import com.example.Entity.OrderMaster;
import com.example.Entity.ProductInfo;
import com.example.Repository.OrderDetailRepository;
import com.example.Repository.OrderMasterRepository;
import com.example.Service.OrderDetailService;
import com.example.Service.OrderMasterService;
import com.example.Service.ProductInfoService;
import com.example.util.BigDecimalUtil;
import com.example.util.CustomException;
import com.example.util.IDUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
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

    @Override
    @Transactional
    public ResultResponse findDetail(String openId, String orderId) {
        if (StringUtils.isBlank(openId)||StringUtils.isBlank(orderId)){
            return ResultResponse.fail(ResultEnums.PARAM_ERROR.getMsg());
        }
        OrderMaster orderMaster = orderMasterRepository.findByOrderIdAndBuyerOpenid(orderId, openId);
        if (orderMaster==null){
            return ResultResponse.fail("该用户没有订单");
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        OrderDetaliAndMaster orderDetaliAndMaster = OrderDetaliAndMaster.builder().orderId(orderMaster.getOrderId()).buyerName(orderMaster.getBuyerName())
                .buyerPhone(orderMaster.getBuyerPhone()).buyerAddress(orderMaster.getBuyerAddress()).buyerOpenid(orderMaster.getBuyerOpenid())
                .orderAmount(orderMaster.getOrderAmount()).orderStatus(orderMaster.getOrderStatus()).payStatus(orderMaster.getPayStatus())
                .createTime(orderMaster.getCreateTime()).updateTime(orderMaster.getUpdateTime()).orderDetailList(orderDetails).build();
        return ResultResponse.success(orderDetaliAndMaster);
    }

    @Override
    public ResultResponse findList(String openId,Integer page,Integer size) {
        if (StringUtils.isBlank(openId)){
            return ResultResponse.fail("用户不存在");
        }
        Pageable pageable=new PageRequest(page,size);
        Integer orderStatus=OrderEnum.NEW.getCode();
        List<OrderMaster> orderMasters = orderMasterRepository.findAllByBuyerOpenidAndOrderStatus(openId,orderStatus,pageable);
        if (orderMasters.isEmpty()){
            return ResultResponse.fail("该用户没有订单");
        }
        return ResultResponse.success(orderMasters);
    }

    @Override
    @Transactional
    public ResultResponse cancel(String openId, String orderId) {
        if (StringUtils.isBlank(openId)||StringUtils.isBlank(orderId)){
            return ResultResponse.fail(ResultEnums.PARAM_ERROR.getMsg());
        }
//        orderDetailRepository.deleteByOrderId(orderId);
//        orderMasterRepository.deleteByOrderIdAndBuyerOpenid(orderId,openId);
        OrderMaster orderMaster = orderMasterRepository.findByOrderIdAndBuyerOpenid(orderId, openId);
        orderMaster.setOrderStatus(OrderEnum.CANCEL.getCode());
        orderMasterRepository.save(orderMaster);
        return ResultResponse.success("取消成功");
    }
}
