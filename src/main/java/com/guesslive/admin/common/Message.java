/**
 * @Title: AipResult.java
 * @Package com.guesslive.admin.common
 * @Description: TODO
 * Copyright: Copyright (c) 2015-2016
 * Company:嗨购科技技术有限公司
 * 
 * @author Haigou-abao
 * @date 2016年9月14日 上午11:06:36
 * @version V1.0
 */

package com.guesslive.admin.common;

/**
  * @ClassName: Message
  * @Description: TODO
  * @author Haigou-abao
  * @date 2016年9月14日 上午11:06:36
  *
  */

public class Message {

	private int code;
	private String message;
	private Object content;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Object getContent() {
		return content;
	}

	public Message() {

	}

	public Message(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Message(int code, String message, Object content) {
		this.code = code;
		this.message = message;
		this.content = content;
	}

	public void setMsg(int code, String message) {
		this.code = code;
		this.message = message;
		this.content = "no content";
	}

	public void setMsg(int code, String message, Object content) {
		this.code = code;
		this.message = message;
		this.content = content;
	}
}
