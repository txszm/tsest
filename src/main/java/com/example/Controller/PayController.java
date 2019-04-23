package com.example.Controller;

import com.example.Entity.OrderMaster;
import com.example.Service.PayService;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("pay")
@Slf4j
public class PayController {
    @Autowired
    private PayService payService;

    @RequestMapping("create")
    public ModelAndView create(@RequestParam("orderId")String orderId, @RequestParam("returnUrl")String returnUrl, Map map){
        OrderMaster orderMaster = payService.findOrderById(orderId);
        PayResponse response = payService.create(orderMaster);
        map.put("payResponse",response);
        map.put("returnUrl",returnUrl);
        return new ModelAndView("weixin/pay",map);
    }
    @RequestMapping("test")
    public void test(){
        log.info("异步回调ok");
    }

    @RequestMapping("notify")
    public ModelAndView weixin_notify(@RequestBody String notifyData){
        log.info("微信支付，异步回调");
        payService.weixin_notify(notifyData);
        return new ModelAndView("weixin/success");
    }
}
