package com.example.Repository;


import com.example.Entity.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {
//    @Query(value = "select * from order_master where order_id=?1 and buyer_openid=?2",nativeQuery = true)
    OrderMaster findByOrderIdAndBuyerOpenid(String orderId,String openId);
    List<OrderMaster> findAllByBuyerOpenid(String openId, Pageable pageable);
    void deleteByOrderIdAndBuyerOpenid(String orderId,String openId);
}
