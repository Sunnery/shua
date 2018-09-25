package com.guesslive.admin.common.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author: jerry
 * @Email: jierui@guesslive.com
 * @Company: haigou©2016
 * @Action: 邮件发送工具实现类
 * @DATE: 2016-9-9
 */
@Component
public class MailUtil {

	protected final Logger logger = Logger.getLogger(getClass());

	public boolean send(Mail mail) {
		// 发送email
		HtmlEmail email = new HtmlEmail();
		try {
			// 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
			email.setHostName(mail.getHost());
			// 字符编码集的设置
			email.setCharset(Mail.ENCODEING);
			// 收件人的邮箱
			String[] s = mail.getReceiver().split(";");
			for(String r:s){
				email.addTo(r);
			}
			// 发送人的邮箱
			email.setFrom(mail.getSender(), mail.getName());
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
			email.setAuthentication(mail.getUsername(), mail.getPassword());
			// 要发送的邮件主题
			email.setSubject(mail.getSubject());
			// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
			email.setMsg(mail.getMessage());
			// 发送
			email.send();
			if (logger.isDebugEnabled()) {
				logger.debug(mail.getSender() + " 发送邮件到 " + mail.getReceiver());
			}
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
			logger.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver()
					+ " 失败");
			return false;
		}
	}

}
