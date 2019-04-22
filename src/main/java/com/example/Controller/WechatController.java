package com.example.Controller;

import com.example.util.CustomException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("wechat")
@Slf4j
public class WechatController {
    @Autowired
    private WxMpService wxMpService;

    @RequestMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        String url="http://xmcc.natapp1.cc/sell/wechat/getUserInfo";
        String redirectUrl=wxMpService.oauth2buildAuthorizationUrl(url,
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl,"UTF-8"));
        return "redirect"+redirectUrl;
    }
    @RequestMapping("setUserInfo")
    public String getUserInfo(@RequestParam("code") String code,@RequestParam("state")String returnUrl) throws UnsupportedEncodingException {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try {
            wxMpOAuth2AccessToken=wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("微信获得access_tolen异常:{}",e.getError().getErrorMsg());
            throw new CustomException(e.getError().getErrorCode(),e.getError().getErrorMsg());
        }
        try {
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            log.info("获取用户信息:{}",wxMpUser.getNickname());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String openId=wxMpOAuth2AccessToken.getOpenId();
        return "redirect:"+URLEncoder.encode(returnUrl,"UTF-8")+"?openid="+openId;
    }
    @RequestMapping("testOpenid")
    public void testOpenid(@RequestParam("openid")String openid){
        log.info("获取用户的openid为:{}",openid);
    }

}
