package com.example.Service;

import com.example.Bean.ResultResponse;
import com.example.Entity.ProductInfo;

public interface ProductInfoService {
    ResultResponse queryList();
    ResultResponse<ProductInfo> queryById(String productId);
    void updateProduct(ProductInfo productInfo);
}
