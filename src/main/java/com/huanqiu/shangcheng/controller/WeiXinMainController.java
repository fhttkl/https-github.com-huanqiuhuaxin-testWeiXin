package com.huanqiu.shangcheng.controller;


import com.huanqiu.shangcheng.pojo.News;
import com.huanqiu.shangcheng.utils.CheckUtil;
import com.huanqiu.shangcheng.utils.MessageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class WeiXinMainController {

    @RequestMapping(value = "wx.do", method = RequestMethod.GET)
    public void WeiXinDoget(HttpServletRequest req, HttpServletResponse resp) {
        try {
            System.out.println("我是微信服务器，  我开始运行了 ");
            String signature = req.getParameter("signature");
            String timestamp = req.getParameter("timestamp");
            String nonce = req.getParameter("nonce");
            String echostr = req.getParameter("echostr");

            boolean checkSignature = CheckUtil.checkSignature(signature, timestamp, nonce);
            PrintWriter out = resp.getWriter();
            if (checkSignature) {
                out.print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "wx.do", method = RequestMethod.POST)
    public void WeiXinDopost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = null;
        try {
            out = resp.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            Map<String, String> map = MessageUtil.xmlToMap(req);
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String createTime = map.get("CreateTime");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            String msgID = map.get("MsgID");
            System.out.println(content);
            String message = null;

            if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
                if("1".equals(content)){
                    List<News> list=new ArrayList<>();
                    News news=new News();
                    news.setTitle("最重要的决定");
                    news.setDescription("因为幸福 没有捷径 只有经营");
                    news.setPicUrl("http://"+MessageUtil.MESSAGE_IMAGEURL+"/image/xiao.jpg");
                    news.setUrl("http://www.tangedu.com.cn/");
                    News news2=new News();
                    news2.setTitle("我是大唐机器人");
                    news2.setDescription("因为幸福 没有捷径 只有经营");
                    news2.setPicUrl("http://"+MessageUtil.MESSAGE_IMAGEURL+"/image/xiao2.jpg");
                    news2.setUrl("http://www.tangedu.com.cn/");
                    list.add(news);
                    list.add(news2);
                }else if("2".equals(content)){

                    List<News> list=new ArrayList<>();
                    News news=new News();
                    news.setTitle("最重要的决定");
                    news.setDescription("因为幸福 没有捷径 只有经营");
                    news.setPicUrl("http://"+MessageUtil.MESSAGE_IMAGEURL+"/image/xiao.jpg");
                    news.setUrl("http://www.tangedu.com.cn/");
                    News news2=new News();
                    news2.setTitle("我是大唐机器人");
                    news2.setDescription("因为幸福 没有捷径 只有经营");
                    news2.setPicUrl("http://"+MessageUtil.MESSAGE_IMAGEURL+"/image/xiao2.jpg");
                    news2.setUrl("http://www.tangedu.com.cn/");
                    list.add(news);
                    list.add(news2);

                    message=MessageUtil.initNewsMessage(toUserName,fromUserName,list);
                }else if("3".equals(content)){
                    message=MessageUtil.iniImageMessage(toUserName,fromUserName);
                }else if("4".equals(content)){
                    message=MessageUtil.iniMusicMessage(toUserName,fromUserName,"E:\\picture\\daolang\\xiao.jpg","西海情歌","刀郎的西海情歌","http://datang.tunnel.qydev.com/weixin/music/gao.mp3");
                }

            }


            /*
            if ("text".equals(msgType)) {
                TextMessageForXml text = new TextMessageForXml();
                text.setFromUserName(toUserName);
                text.setToUserName(fromUserName);
                text.setMsgType("text");
                text.setCreateTime(new Date().getTime());
                text.setContent("我的服务器端 我是大唐教育" + toUserName + "您发送的消息是" + content);
                text.setMsgId(msgID);
                message = MessageUtil.textMessageToXml(text);
            }*/


            out.print(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

}
