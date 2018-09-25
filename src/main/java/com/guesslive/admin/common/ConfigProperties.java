/**
 * @Title: ConfigProperties.java
 * @Package com.guesslive.admin.common
 * @Description: TODO
 * Copyright: Copyright (c) 2015-2016
 * Company:嗨购科技技术有限公司
 * 
 * @author Haigou-abao
 * @date 2016年6月14日 下午2:10:58
 * @version V1.0
 */

package com.guesslive.admin.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
  * @ClassName: ConfigProperties
  * @Description: 配置属性类
  * @author Haigou-abao
  * @date 2016年6月14日 下午2:10:58
  *
  */
@Component
public class ConfigProperties {

	@Value("#{config['system.id']}")
	private int systemId;
	@Value("#{config['system.account']}")
	private String systemAccount;
	@Value("#{config['system.name']}")
	private String systemName;
	@Value("#{config['system.password']}")
	private String systemPassword;
	@Value("#{config['system.project.apkFilePath']}")
	private String apkFilePath;
	@Value("#{config['system.env']}")
	private String env; // 运行环境
	@Value("#{config['system.version']}")
	private String version; // 系统版本
	@Value("#{config['system.switch.isRecordException']}")
	private boolean isRecordException; // 日志记录开关
	@Value("#{config['system.switch.isSendExceptionMail']}")
	private boolean isSendExceptionMail; // 邮件发送开关
	@Value("#{config['weixin_card_appid']}")
	private String appid;
	@Value("#{config['weixin_card_secret']}")
	private String secret;
	
	//短信接口路径
	@Value("#{config['notify.url']}")
	public String notifyUrl;
	
	//退款接口
	@Value("#{config['refund.url']}")
	public String refundUrl;
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isRecordException() {
		return isRecordException;
	}

	public void setRecordException(boolean isRecordException) {
		this.isRecordException = isRecordException;
	}

	public boolean isSendExceptionMail() {
		return isSendExceptionMail;
	}

	public void setSendExceptionMail(boolean isSendExceptionMail) {
		this.isSendExceptionMail = isSendExceptionMail;
	}

	public int getSystemId() {
		return systemId;
	}
	
	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}
	public String getSystemAccount() {
		return systemAccount;
	}
	
	public void setSystemAccount(String systemAccount) {
		this.systemAccount = systemAccount;
	}
	public String getSystemName() {
		return systemName;
	}
	
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getSystemPassword() {
		return systemPassword;
	}
	
	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}

	public String getApkFilePath() {
		return apkFilePath;
	}

	public void setApkFilePath(String apkFilePath) {
		this.apkFilePath = apkFilePath;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getRefundUrl() {
		return refundUrl;
	}

	public void setRefundUrl(String refundUrl) {
		this.refundUrl = refundUrl;
	}
	
}
