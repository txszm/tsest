package com.example.Controller;

import com.example.Bean.ResultResponse;
import com.example.Service.ProductInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("buyer/product")
@Api(description = "商品信息接口")//使用swagger的注解对类的描述
public class ProductInfoController {
    @Autowired
    private ProductInfoService productInfoService;
//    @RequestMapping("list")
    @GetMapping("list")
    @ApiOperation(value = "查询商品列表")
    public ResultResponse list() {
    return productInfoService.queryList();
    }
}
