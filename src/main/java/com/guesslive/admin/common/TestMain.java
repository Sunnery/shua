package com.guesslive.admin.common;

import com.guesslive.admin.common.mail.Mail;
import com.guesslive.admin.common.mail.MailUtil;

public class TestMain {
	 public static void main(String[] args) {
		 Mail mail = new Mail();  
		 mail.setHost("smtp.guesslive.com");
		 mail.setPassword("Haigou201511");
		 mail.setSender("jierui@guesslive.com");
		 mail.setUsername("jierui@guesslive.com");
		 mail.setName("Haigou-Exception");
		 mail.setReceiver("jierui@guesslive.com");
         mail.setSubject("aaaaaaaaa");  
         mail.setMessage("bbbbbbbbbbbbbbbbb");  
         new MailUtil().send(mail);  
	 }
	}