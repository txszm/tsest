package com.example.Service.Impl;

import com.example.Bean.ProductEnums;
import com.example.Bean.ResultEnums;
import com.example.Bean.ResultResponse;
import com.example.Dto.ProductCategoryDto;
import com.example.Dto.ProductInfoDto;
import com.example.Entity.ProductInfo;
import com.example.Repository.ProductInfoRepository;
import com.example.Service.ProductCategoryService;
import com.example.Service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoRepository productInfoReqository;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Override
    public ResultResponse queryList() {
        ResultResponse<List<ProductCategoryDto>> categoryServiceResult=productCategoryService.findAll();
        List<ProductCategoryDto> categoryDtoList=categoryServiceResult.getData();
        if (CollectionUtils.isEmpty(categoryDtoList)){
            return categoryServiceResult;
        }
        List<Integer> categoryTypeList=categoryDtoList.stream().map(categorydto ->
                categorydto.getCategoryType()).collect(Collectors.toList());
        List<ProductInfo> productInfoList=productInfoReqository.findByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(),categoryTypeList);
        List<ProductCategoryDto> finalResultList=categoryDtoList.parallelStream().map(categorydto ->{
            categorydto.setProductInfoDtoList(productInfoList.stream().filter(productInfo -> productInfo.getCategoryType()==
                    categorydto.getCategoryType()).map(productInfo ->
                    ProductInfoDto.build(productInfo)).collect(Collectors.toList()));
            return categorydto;
        }).collect(Collectors.toList());
        return ResultResponse.success(finalResultList);
    }

    @Override
    public ResultResponse<ProductInfo> queryById(String productId) {
        if (StringUtils.isBlank(productId)){
            return ResultResponse.fail(ResultEnums.PARAM_ERROR.getMsg()+":"+productId);
        }
        Optional<ProductInfo> byId=productInfoReqository.findById(productId);
        if (!byId.isPresent()){
            return ResultResponse.fail(productId+":"+ResultEnums.NOT_EXITS.getMsg());
        }
        ProductInfo productInfo=byId.get();
        if (productInfo.getProductStatus()== ProductEnums.PRODUCT_DOWN.getCode()){
            return ResultResponse.fail(ProductEnums.PRODUCT_DOWN.getMsg());
        }
        return ResultResponse.success(productInfo);
    }

    @Override
    public void updateProduct(ProductInfo productInfo) {
        productInfoReqository.save(productInfo);
    }
}
