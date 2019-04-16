package com.example.Service.Impl;

import com.example.Bean.ResultResponse;
import com.example.Dto.ProductCategoryDto;
import com.example.Entity.ProductCategory;
import com.example.Repository.ProductCategoryRepository;
import com.example.Service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Override
    public ResultResponse<List<ProductCategoryDto>> findAll() {
        List<ProductCategory> productCategoryList=productCategoryRepository.findAll();
        //利用流转换为dto集合
        return ResultResponse.success(productCategoryList.stream().map(productCategory ->
                ProductCategoryDto.build(productCategory))
        .collect(Collectors.toList()));
    }
}
