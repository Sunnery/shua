package com.guesslive.admin.common.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.guesslive.admin.common.mail.Mail;
import com.guesslive.admin.common.mail.MailUtil;

/**
 * @author: jerry
 * @Email: jierui@guesslive.com
 * @Company: haigou©2016
 * @Action: MQ监听类 默认执行onMessage方法
 * @DATE: 2016-9-23
 */

public class MyMessageListener implements MessageListener {  
	
	@Autowired
	private  MyMessageConverter messageConverter;  
	
	protected final Logger logger = Logger.getLogger(getClass());
	
	public void onMessage(Message message) {  
        if (message instanceof ObjectMessage) {  
            ObjectMessage objMessage = (ObjectMessage) message;  
            try {  
            	Mail mail = (Mail) messageConverter.fromMessage(objMessage);  
                logger.debug("接收到一个ObjectMessage，包含Email对象，准备发送邮件！");
                new MailUtil().send(mail);
            } catch (JMSException e) {  
            	logger.info("接收到一个ObjectMessage，但处理失败！");
            }  
        }  
    }  
   
  
}  
