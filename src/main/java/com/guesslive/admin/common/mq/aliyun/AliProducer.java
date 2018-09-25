package com.guesslive.admin.common.mq.aliyun;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.guesslive.admin.common.util.StringUtil;
import com.aliyun.openservices.ons.api.Message;

/**
 * @author: jerry
 * @Email: jierui@guesslive.com
 * @Company: haigou©2016
 * @Action: 阿里云队列消息生产者
 * @DATE: 2016-9-28
 */

@Component
public class AliProducer {
	
	@Value("#{config['aliyun.consumerId']}")
	private String consumerId; // MQ控制台创建的Consumer ID
	@Value("#{config['aliyun.accessKey']}")
	private String accessKey; // 鉴权用AccessKey，在阿里云服务器管理控制台创建
	@Value("#{config['aliyun.secretKey']}")
	private String secretKey; // 鉴权用SecretKey，在阿里云服务器管理控制台创建
	@Value("#{config['aliyun.emailTopic']}")
	private String emailTopic;
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	public void send(String topic,String tag,String key,Object obj) {
	     Properties properties = new Properties();
	     properties.put(PropertyKeyConst.ProducerId, "JAVA_TEST_Topic");// 您在MQ控制台创建的Producer ID
	     properties.put(PropertyKeyConst.AccessKey,"OznAMJjsjZcedPGE");// 鉴权用AccessKey，在阿里云服务器管理控制台创建
	     properties.put(PropertyKeyConst.SecretKey, "QCq70RSxausfCqwYurb7VFrOjVAHJd ");// 鉴权用SecretKey，在阿里云服务器管理控制台创建 QCq70RSxausfCqwYurb7VFrOjVAHJd 
	     Producer producer = ONSFactory.createProducer(properties);
	     // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可
	     producer.start();
         Message msg;
		try {
			msg = new Message( //
			     // Message Topic
				  topic,
			     // Message Tag,
			     // 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在MQ服务器过滤
				  tag,
			     // Message Body
			     // 任何二进制形式的数据， MQ不做任何干预，
			     // 需要Producer与Consumer协商好一致的序列化和反序列化方式
			     StringUtil.objTojson(obj).getBytes("UTF-8"));
//				 String localChartSet = System.getProperty("file.encoding");
//				 System.out.println("localChartSet = "+localChartSet);
				 // 设置代表消息的业务关键属性，请尽可能全局唯一，以方便您在无法正常收到消息情况下，可通过MQ控制台查询消息并补发
		         // 注意：不设置也不会影响消息正常收发
		         msg.setKey(key);
		         // 发送消息，只要不抛异常就是成功
		         // 打印Message ID，以便用于消息发送状态查询
		         SendResult sendResult = producer.send(msg);
		         logger.debug("Send Message success. Message ID is: " + sendResult.getMessageId());
		} catch (Exception e) {
			logger.error("aliMQ生产者发送失败！");
			logger.error(StringUtil.getStackMsg(e));
		}
         
	 }
}
