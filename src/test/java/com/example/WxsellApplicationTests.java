package com.example;

import com.example.Entity.ProductCategory;
import com.example.Repository.ProductCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WxsellApplicationTests {

	@Test
	public void contextLoads() {
        List<ProductCategory> all=productCategoryRepository.findAll();
        all.stream().forEach(System.out::println);
	}
	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Test//增加测试
	public void insert(){
		ProductCategory productCategory = new ProductCategory();
		productCategory.setCategoryName("手机");
		productCategory.setCategoryType(1);
		productCategoryRepository.save(productCategory);
	}
	@Test//查询单个，与修改测试
	public void findOne(){
		Optional<ProductCategory> byId = productCategoryRepository.findById(1);
		ProductCategory productCategory =null;
		if(byId.isPresent()){//判断是否有  我们这里直接用有的测试了
			productCategory = byId.get();
			System.out.println(productCategory);
		}
		productCategory.setCategoryName("家电");
		//修改测试 也是save方法  如果实体类一点没变 不会去修改的
        ProductCategory category = productCategoryRepository.save(productCategory);
        System.out.println(category);
    }

}
