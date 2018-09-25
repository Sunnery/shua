package com.guesslive.admin.common.exception;

public class SessionTimeoutException extends Exception {

	private static final long serialVersionUID = 5383897104554479418L;

	public SessionTimeoutException() {
		
		
	};

	public SessionTimeoutException(String msg) {
		super(msg);
	}
}
