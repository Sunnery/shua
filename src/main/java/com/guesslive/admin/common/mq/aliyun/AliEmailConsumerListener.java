package com.guesslive.admin.common.mq.aliyun;
import java.io.UnsupportedEncodingException;
//CID_JAVA_TEST_Topic
//OznAMJjsjZcedPGE
//QCq70RSxausfCqwYurb7VFrOjVAHJd
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guesslive.admin.common.mail.Mail;
import com.guesslive.admin.common.mail.MailUtil;
import com.guesslive.admin.common.util.StringUtil;
import com.aliyun.openservices.ons.api.Message;

/**
 * @author: jerry
 * @Email: jierui@guesslive.com
 * @Company: haigou©2016
 * @Action: 阿里云队列消息消费者监听类 程序启动时自动执行init方法
 * @DATE: 2016-9-28
 */

public class AliEmailConsumerListener {
	
	@Autowired
	private MailUtil mailUtil;
	@Value("#{config['aliyun.consumerId']}")
	private String consumerId; // MQ控制台创建的Consumer ID
	@Value("#{config['aliyun.accessKey']}")
	private String accessKey; // 鉴权用AccessKey，在阿里云服务器管理控制台创建
	@Value("#{config['aliyun.secretKey']}")
	private String secretKey; // 鉴权用SecretKey，在阿里云服务器管理控制台创建
	@Value("#{config['aliyun.emailTopic']}")
	private String emailTopic;
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	public void init() {
		 Properties properties = new Properties();
		 properties.put(PropertyKeyConst.ConsumerId, consumerId);
	        properties.put(PropertyKeyConst.AccessKey, accessKey);
	        properties.put(PropertyKeyConst.SecretKey, secretKey);
	        Consumer consumer = ONSFactory.createConsumer(properties);
	        consumer.subscribe(emailTopic, "*", new MessageListener() {
	            public Action consume(Message message, ConsumeContext context) {
	                logger.debug("Receive: " +  message);
	                String json;
					try {
//						String localChartSet = System.getProperty("file.encoding");
//						 System.out.println(" AliEmailConsumerListener localChartSet = "+localChartSet);
						json = new String(message.getBody(), "UTF-8");
						Mail mail = new Gson().fromJson(json, new TypeToken<Mail>() {}.getType());
		                mailUtil.send(mail);
					} catch (Exception e) {
						logger.error("aliMQ消费者解析body失败！");
						logger.error(StringUtil.getStackMsg(e));
						return null;
					}
	                return Action.CommitMessage;
	            }
	        });
	        consumer.start();
	        logger.info("AliEmailConsumerListener Started");
	}
}

