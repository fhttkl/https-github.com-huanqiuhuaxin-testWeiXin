package com.huanqiu.test;

import com.huanqiu.shangcheng.pojo.AccessToken;
import com.huanqiu.shangcheng.utils.WeiXinUtil;

import java.io.IOException;

public class WeiXinTest {
    public static void main(String[] args) {
        AccessToken token = WeiXinUtil.getAccessToken();
        System.out.println("票据"+token.getToken());
        System.out.println("有效时间"+token.getExpiresIn());
        String path= "E:\\picture\\datang\\xiao.jpg";
        try {
            String mediaId=WeiXinUtil.fileUpload(path,token.getToken(),"image");
            System.out.println(mediaId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
