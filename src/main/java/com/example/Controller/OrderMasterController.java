package com.example.Controller;

import com.example.Bean.ResultResponse;
import com.example.Dto.OrderMasterDto;
import com.example.Service.OrderMasterService;
import com.example.util.JsonUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {
    @Autowired
    private OrderMasterService orderMasterService;

    @PostMapping("create")
    @ApiOperation(value = "创建订单接口", httpMethod = "POST", response = ResultResponse.class)
    public ResultResponse create(
            @Valid @ApiParam(name="订单对象",value = "传入json格式",required = true)
            OrderMasterDto orderMasterDto, BindingResult bindingResult){
        Map<String,String> map= Maps.newHashMap();
        if (bindingResult.hasErrors()){
            List<String> errList=bindingResult.getFieldErrors().stream().map(err ->
                    err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(errList));
            return ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }


    @GetMapping(value = "detail")
    @ApiOperation(value = "查询订单详情接口", httpMethod = "GET", response = ResultResponse.class)
    public ResultResponse findDetail( String orderId,String openId){
        return orderMasterService.findDetail(openId,orderId);
    }
    @GetMapping(value = "list")
    @ApiOperation(value = "查询订单列表接口", httpMethod = "GET", response = ResultResponse.class)
    public ResultResponse findList(String openId,Integer page,Integer size){
        return orderMasterService.findList(openId,page,size);
    }

    @GetMapping(value = "cancel")
    @ApiOperation(value = "取消订单接口", httpMethod = "GET", response = ResultResponse.class)
    public ResultResponse cancel(String orderId,String openId){
        return orderMasterService.cancel(openId,orderId);
    }
}
