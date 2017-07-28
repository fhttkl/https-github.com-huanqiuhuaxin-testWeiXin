package com.huanqiu.test;

import com.huanqiu.shangcheng.utils.AccessTokenUtil;
import com.huanqiu.shangcheng.utils.WeiXinUtil;
import net.sf.json.JSONObject;

public class WeiXinTest {
    public static void main(String[] args) {
       /* AccessToken token = WeiXinUtil.getAccessToken();
        System.out.println("票据"+token.getToken());
        System.out.println("有效时间"+token.getExpiresIn());
        String path= "E:\\picture\\datang\\xiao.jpg";
        try {
            String mediaId=WeiXinUtil.fileUpload(path,token.getToken(),"image");
            System.out.println(mediaId);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String menu = JSONObject.fromObject(WeiXinUtil.iniMenu()).toString();
        int result = WeiXinUtil.createMenu(AccessTokenUtil.getAccessToken().getToken(), menu);
        if(result==0){
            System.out.println("创建菜单成功");
        }else{
            System.out.println("错误码:" + result);
        }

    }
}
