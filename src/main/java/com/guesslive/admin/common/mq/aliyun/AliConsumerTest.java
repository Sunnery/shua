package com.guesslive.admin.common.mq.aliyun;
//CID_JAVA_TEST_Topic
//OznAMJjsjZcedPGE
//QCq70RSxausfCqwYurb7VFrOjVAHJd
import java.util.Properties;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guesslive.admin.common.mail.Mail;

public class AliConsumerTest {
	  public static void main(String[] args) {
	        Properties properties = new Properties();
	        properties.put(PropertyKeyConst.ConsumerId, "CID_JAVA_TEST_Topic");// 您在MQ控制台创建的Consumer ID
	        properties.put(PropertyKeyConst.AccessKey, "OznAMJjsjZcedPGE");// 鉴权用AccessKey，在阿里云服务器管理控制台创建
	        properties.put(PropertyKeyConst.SecretKey, "QCq70RSxausfCqwYurb7VFrOjVAHJd");// 鉴权用SecretKey，在阿里云服务器管理控制台创建
	        Consumer consumer = ONSFactory.createConsumer(properties);
	        consumer.subscribe("JAVA_TEST_Topic", "*", new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
            	try {
            		String json =  new String(message.getBody(), "utf-8");
            		Mail mail = new Gson().fromJson(json, new TypeToken<Mail>() {}.getType());
                    System.out.println(mail.getMessage()+"--------------");
				} catch (Exception e) {
					e.printStackTrace();
				}
            	return Action.CommitMessage;
                
            }
	        });
	        consumer.start();
	        System.out.println("Consumer Started");
	    }
}