package com.huanqiu.shangcheng.utils;

import com.huanqiu.shangcheng.pojo.*;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageUtil {
	public static final  String MESSAGE_NEWS="news";

	public static final String MESSAGE_IMAGE="image";
	public static final  String MESSAGE_TEXT="text";

	public static final  String MESSAGE_IMAGEURL="datang.tunnel.qydev.com/weixin";

	/**
	 * XML转成map
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException{
		InputStream ins = null;
		Map<String,String> map= new HashMap<String, String>();
		try {
		SAXReader reader= new SAXReader();
			 ins=request.getInputStream();
			Document doc=reader.read(ins);
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			
			for (Element ele : list) {
				map.put(ele.getName(), ele.getText());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		ins.close();
		
		return map;
	}
	
	
	
	/**
	 * 将text转成xml类型
	 * @return
	 */
	public static String textMessageToXml(TextMessageForXml textMessage){
		XStream xstream= new XStream();
		xstream.alias("xml",textMessage.getClass());
		String xml = xstream.toXML(textMessage);
		return xml;
	}


	/**
	 * 图文消息转成XML
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream= new XStream();
		xstream.alias("xml",newsMessage.getClass());  //标签互转
		xstream.alias("item",new News().getClass());

		String xml = xstream.toXML(newsMessage);
		return xml;
	}

	/**
	 * 图片消息转xml
	 * @param newsMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xstream= new XStream();
		xstream.alias("xml",imageMessage.getClass());  //标签互转
		String xml = xstream.toXML(imageMessage);
		return xml;
	}


	//如何拼装一个图片消息
	public static String initNewsMessage(String toUserName,String fromUserName,List newsList){
		String message=null;
		//创建一个集合用来接收消息体
		//List<News> newsList=new ArrayList<News>();
		NewsMessage newsMessage=new NewsMessage();

		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		message=newsMessageToXml(newsMessage);
		//message=JSONObject.fromObject(newsMessage).toString();
		return message;
	}

	/**
	 * 组装图片消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String iniImageMessage(String toUserName,String fromUserName){
		String message=null;
		Image image=new Image();
		image.setMediaId("83I0LyUL6ckJlmmjpkRYwr1Ui-A0LdatxHO6mjeWG6K0xvaTLrZbxWAVxp1a86Co");
		ImageMessage imageMessage=new ImageMessage();
		imageMessage.setFromUserName(toUserName);  //设置消息从哪来
		imageMessage.setToUserName(fromUserName);  //设置消息到哪去
		imageMessage.setMsgType(MESSAGE_IMAGE);  //设置文件类型
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		message=imageMessageToXml(imageMessage);
		return message;
	}

	
}
