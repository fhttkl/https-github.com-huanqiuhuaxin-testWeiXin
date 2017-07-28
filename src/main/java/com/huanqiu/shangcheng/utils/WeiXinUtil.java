package com.huanqiu.shangcheng.utils;

import com.huanqiu.shangcheng.menu.Button;
import com.huanqiu.shangcheng.menu.ClickButton;
import com.huanqiu.shangcheng.menu.Menu;
import com.huanqiu.shangcheng.menu.ViewButton;
import com.huanqiu.shangcheng.pojo.AccessToken;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeiXinUtil {

    public static  final  String APPID="wx391d816ff3b4637c";
    public static  final  String APPSECRET="c47c762d3262a35a2bc965d54ededa3b";

    /**
     * 获取accesstoken的URL
     */
    public static final  String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public static final  String UPLOAD_URL ="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    public  static final String CREATE_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * get 请求
     * @param url
     * @return
     */
    public static JSONObject doGetStr(String url){
        DefaultHttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(url); //获得请求
        //创建一个空的JSONObject
        JSONObject jsonObject=null;
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet);//执行这个get执行
            System.out.println(httpResponse.toString());
            System.out.println(httpResponse);
            HttpEntity httpEntity= httpResponse.getEntity(); //在请求返回值中获取结果
            //将htttpEntity转成String类型
            if(httpEntity!=null){
                String result= EntityUtils.toString(httpEntity,"UTF-8");//设置成utf-8防止乱码
                //将字符串转成json
               jsonObject= JSONObject.fromObject(result);
            }

        } catch (IOException e) {
            System.out.println("在微信服务器获取access_token失败");
            e.printStackTrace();

        }
        return jsonObject;
    }


    /**
     * post请求
     * @param url
     * @param outStr
     * @return
     */
    public static JSONObject doPostStr(String url,String outStr){
        DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
        HttpPost httpPost=new HttpPost(url);//发送一个post请求
        JSONObject jsonObject=null;
        try {
            httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
            HttpResponse httpResponse= defaultHttpClient.execute(httpPost); //发送一个请求post
            HttpEntity httpEntity= httpResponse.getEntity();
            String result= EntityUtils.toString(httpEntity,"UTF-8");
            jsonObject=jsonObject.fromObject(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取access_token
     * @return
     */
    public static AccessToken getAccessToken(){
        AccessToken token= new AccessToken();
        //将接口地址改成
        String url=ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET",APPSECRET);
        JSONObject jsonObject=doGetStr(url);
        token.setToken(jsonObject.getString("access_token"));
        token.setExpiresIn(jsonObject.getInt("expires_in"));
        return  token;
    }

    public static String fileUpload(String filePath,String accessToken,String type) throws IOException {
        File file=new File(filePath);
        if(!file.exists()||!file.isFile()){
            throw new IOException("文件不存在");
        }
        String url=UPLOAD_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE",type);
        URL urlObj=new URL(url);
        //连接
        HttpURLConnection con=(HttpURLConnection)urlObj.openConnection();
        con.setRequestMethod("POST");//以post方式提交
        con.setDoInput(true); //设置输入
        con.setDoOutput(true); //设置输出
        con.setUseCaches(false);//以post方式提交忽略缓存

        //设置请求头信息
        con.setRequestProperty("Connection","Keep-Alive"); //保持连接， 此处可以设置连接超时keepalvie_timeout
        con.setRequestProperty("Charset","UTF-8");

        //设置边界
        String BOUNDDARY="----------"+System.currentTimeMillis();
        con.setRequestProperty("Content-Type","multipart/form-data;boundary="+BOUNDDARY);

        StringBuilder sb=new StringBuilder();
        sb.append("--");
        sb.append(BOUNDDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head=sb.toString().getBytes("UTF-8");

        //获得输出流
        OutputStream out =new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件以流的方式 推入到url中
        DataInputStream in=new DataInputStream(new FileInputStream(file));
        int bytes=0;
        byte[] bufferOut=new byte[1024];
        while ((bytes=in.read(bufferOut))!=-1){
            out.write(bufferOut,0,bytes);
        }
        in.close();
        //结尾部分
        byte[] foot=("\r\n--"+BOUNDDARY+"--\r\n").getBytes("utf-8");//定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer=new StringBuffer();
        BufferedReader reader=null;
        String result=null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if(result==null){
                result=buffer.toString();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                reader.close();
            }
        }
        JSONObject jsonObject=JSONObject.fromObject(result);
        System.out.println(jsonObject);
        String typeName="media_id";
        if(!"image".equals(type)){
            typeName=type+"_media_id";
        }
        String mediaId=jsonObject.getString(typeName);
        return mediaId;
    }

    /**
     * 组装菜单
     * @return
     */
    public static Menu iniMenu(){
        Menu menu =new Menu();
        ViewButton button11=new ViewButton();
        button11.setName("大唐智美");
        button11.setType("view");
        button11.setUrl("http://tangedu.com.cn/");

        ClickButton button21=new ClickButton();
        button21.setName("扫码事件");
        button21.setType("scancode_push");
        button21.setKey("21");

        ClickButton button22=new ClickButton();
        button22.setName("地址位置");
        button22.setType("location_select");
        button22.setKey("22");
        ClickButton button2=new ClickButton();
        button2.setName("课程介绍");
        button2.setSub_button(new Button[]{button21,button22});

        menu.setButton(new Button[]{button11,button2});
        return menu;
    }


    public static int createMenu(String token,String menu){
        int result=0;
        String url=CREATE_MENU_URL.replace("ACCESS_TOKEN",token);
        JSONObject jsonObject=doPostStr(url,menu);
        if(jsonObject!=null){
            result=jsonObject.getInt("errcode");
        }
        return  result;
    }
}