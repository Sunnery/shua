package com.guesslive.admin.common.mq;

import java.io.Serializable;
import javax.jms.JMSException;  
import javax.jms.Message;  
import javax.jms.ObjectMessage;  
import javax.jms.Session;  
import org.springframework.jms.support.converter.MessageConversionException;  
import org.springframework.jms.support.converter.MessageConverter;  

/**
 * @author: jerry
 * @Email: jierui@guesslive.com
 * @Company: haigou©2016
 * @Action: MQ消息object类型处理类
 * @DATE: 2016-9-23
 */
   
public class MyMessageConverter implements MessageConverter {  
   
    public Message toMessage(Object object, Session session)  
            throws JMSException, MessageConversionException {  
        return session.createObjectMessage((Serializable) object);  
    }  
   
    public Object fromMessage(Message message) throws JMSException,  
            MessageConversionException {  
        ObjectMessage objMessage = (ObjectMessage) message;  
        return objMessage.getObject();  
    }  
   
}  