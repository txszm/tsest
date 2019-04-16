package com.example.Repository;

import com.example.Entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    List<ProductInfo> findByProductStatusAndCategoryTypeIn(Integer status,List<Integer> categoryList);
}
