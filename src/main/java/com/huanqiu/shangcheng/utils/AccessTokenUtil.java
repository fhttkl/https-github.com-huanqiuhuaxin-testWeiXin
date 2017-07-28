package com.huanqiu.shangcheng.utils;

import com.huanqiu.shangcheng.pojo.AccessToken;
import net.sf.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccessTokenUtil {

    /**
     * 获取access_token
     * @return
     */
    public static AccessToken getAccessToken(){
        AccessToken token = getAccessTokenForProperties();
        //比较时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("获取AccessToken的当前系统时间:"+df.format(new Date()));// new Date()为获取当前系统时间
        try {
            Date tokenDate = df.parse(token.getTime());
            Map<String,String> dateMap = getDatePoor(new Date(), tokenDate);
            //大于一天
            if(Integer.valueOf(dateMap.get("day"))!=0){
                 token = getAccessTokenForWeiXin();
                writeAccessTokenToProperties(AccessTokenUtil.class.getResource("/config/AccessToken.properties").getPath(),token);
                 return token;
            }
            if(Integer.valueOf(dateMap.get("hour"))>=2){
                token = getAccessTokenForWeiXin();
                writeAccessTokenToProperties(AccessTokenUtil.class.getResource("/config/AccessToken.properties").getPath(),token);
                return token;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  token;
    }

    /**
     * 发送请求在微信服务器中获取token
     * @return AccessToken
     */
    public static AccessToken getAccessTokenForWeiXin(){
        System.out.println("在微信获取AccessToken");
        AccessToken token= new AccessToken();
        //将接口地址改成
        String url=WeiXinUtil.ACCESS_TOKEN_URL.replace("APPID",WeiXinUtil.APPID).replace("APPSECRET",WeiXinUtil.APPSECRET);
        JSONObject jsonObject=WeiXinUtil.doGetStr(url);
        token.setToken(jsonObject.getString("access_token"));
        token.setExpiresIn(jsonObject.getInt("expires_in"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        token.setTime(df.format(new Date()));
        return token;
    }


    /**
     * 在配置文件中获取accesstoken
     * @return
     */
    public static AccessToken getAccessTokenForProperties(){
        AccessToken accessToken=new AccessToken();
        try {
            HashMap<String,String> map=  GetAllProperties(AccessTokenUtil.class.getResource("/config/AccessToken.properties").getPath());
            accessToken.setToken(map.get("AccessToken.content"));
            accessToken.setExpiresIn(Integer.valueOf(map.get("AccessToken.expiresIn")));
            accessToken.setTime(map.get("AccessToken.time"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    /**
     * 将accessToken 写入到配置文件中
     * @param filePath
     * @param accessToken
     */
    public static void writeAccessTokenToProperties(String filePath,AccessToken accessToken){
        try {
            WriteProperties(filePath,"AccessToken.content",accessToken.getToken());
            WriteProperties(filePath,"AccessToken.time",accessToken.getTime());
            WriteProperties(filePath,"AccessToken.expiresIn",String.valueOf(accessToken.getExpiresIn()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入token失败");
        }
    }

    //写入Properties信息
     public static void WriteProperties (String filePath, String pKey, String pValue) throws IOException {
         Properties pps = new Properties();
         InputStream in = new FileInputStream(filePath);
         //从输入流中读取属性列表（键和元素对）
         pps.load(in);
         //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
         //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
         OutputStream out = new FileOutputStream(filePath);
         pps.setProperty(pKey, pValue);
         //以适合使用 load 方法加载到 Properties 表中的格式，
         //将此 Properties 表中的属性列表（键和元素对）写入输出流
         pps.store(out, "Update " + pKey + " name");
     }

    //读取Properties的全部信息
     public static HashMap<String,String> GetAllProperties(String filePath) throws IOException {
        HashMap<String,String> map =new HashMap<String,String>();
        Properties pps = new Properties();
         InputStream in = new BufferedInputStream(new FileInputStream(filePath));
         pps.load(in);
         Enumeration en = pps.propertyNames(); //得到配置文件的名字
         while(en.hasMoreElements()) {
             String strKey = (String) en.nextElement();
             String strValue = pps.getProperty(strKey);
             map.put(strKey,strValue);
             System.out.println(strKey + "=" + strValue);
         }
         return map;
     }

    //根据Key读取Value
      public static String GetValueByKey(String filePath, String key) {
          Properties pps = new Properties();
          try {
              InputStream in = new BufferedInputStream (new FileInputStream(filePath));
              pps.load(in);
              String value = pps.getProperty(key);
              System.out.println(key + " = " + value);
              return value;
          }catch (IOException e) {
              e.printStackTrace();
              return null;
          }
      }

    /**
     * 计算两个日期的时间差
     */
    public static Map getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        Map<String,String> map=new HashMap<>();
        map.put("day",String.valueOf(day));
        map.put("hour",String.valueOf(hour));
        map.put("min",String.valueOf(min));
        return map;
    }
    public static void main(String[] args) {
      /*  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        //将字符串转成日期
        try {
            Date date = df.parse( "2017-07-27 08:51:00" );
            System.out.println(df.format(date));
            Map<String,String> map=  getDatePoor(new Date(),date);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        AccessToken accesToken = getAccessToken();
        System.out.println(accesToken.getToken());
    }

}
