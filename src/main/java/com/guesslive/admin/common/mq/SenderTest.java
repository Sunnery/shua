package com.guesslive.admin.common.mq;

import java.util.Date;

/**
 * @author: jerry
 * @Email: jierui@guesslive.com
 * @Company: haigou©2016
 * @Action: MQ生产者测试类
 * @DATE: 2016-9-23
 */

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.guesslive.admin.common.mail.Mail;

public class SenderTest {
	
	public static void main(String[] args) {
		try {
			Mail mail = new Mail();
			StringBuffer mailContent = new StringBuffer();
	        mail.setSubject("【环境出现异常，请关注！");
	        mailContent.append("异常信息：\n");
	        mail.setMessage(mailContent.toString());
			sendMessage(mail);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendMessage(Mail mail)throws Exception {
				// ConnectionFactory ：连接工厂，JMS 用它创建连接
				ConnectionFactory connectionFactory; // Connection ：JMS 客户端到JMS
				// Provider 的连接
				Connection connection = null; // Session： 一个发送或接收消息的线程
				Session session; // Destination ：消息的目的地;消息发送给谁.
				Destination destination; // MessageProducer：消息发送者
				MessageProducer producer; // TextMessage message;
				// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
				connectionFactory = new ActiveMQConnectionFactory(
						ActiveMQConnection.DEFAULT_USER,
						ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
				try { // 构造从工厂得到连接对象
					connection = connectionFactory.createConnection();
					// 启动
					connection.start();
					// 获取操作连接
					session = connection.createSession(Boolean.TRUE,
							Session.AUTO_ACKNOWLEDGE);
					// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
					destination = session.createQueue("email");
					// 得到消息生成者【发送者】
					producer = session.createProducer(destination);
					// 设置不持久化，此处学习，实际根据项目决定
					producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
					// 构造消息，此处写死，项目就是参数，或者方法获取
					ObjectMessage message = session.createObjectMessage(mail);
					// 发送消息到目的地方
					System.out.println("发送消息：" + "ActiveMq 发送的消息"+new Date());
					producer.send(message);
					session.commit();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != connection)
							connection.close();
					} catch (Throwable ignore) {
					}
				}
	}
}