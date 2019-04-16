package com.example.Service;

import com.example.Bean.ResultResponse;
import com.example.Dto.ProductCategoryDto;

import java.util.List;

public interface ProductCategoryService {
    ResultResponse<List<ProductCategoryDto>> findAll();
}
