package com.example.Config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {
    @Autowired
    private WeiXinProperties weiXinProperties;
    @Bean
    public BestPayService bestPayService(){
        //微信公众账号支付配置
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        wxPayH5Config.setAppId("wxcec0b9e65c084712");
        wxPayH5Config.setAppSecret("05a7e861c1985ced86af77fb8f7163bc");
        wxPayH5Config.setMchId("1529533061");
        wxPayH5Config.setMchKey("qwertyuiopasdfghjklzxcvbnm123456");
        wxPayH5Config.setKeyPath("F:\\下载文件\\05微信点餐项目\\day07\\微信服务号信息_资料\\证书\\1529533061_20190327_cert\\apiclient_cert.p12");
        wxPayH5Config.setNotifyUrl("http://xmccjyqs.natapp1.cc/sell/pay/test");

//支付类, 所有方法都在这个类里
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config);
        return bestPayService;
    }
}
