package com.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.guesslive.admin.service.SeccoService;



@Component
public class TimerTask {
	
	@Autowired
	private SeccoService seccoService;
	
	public void resisterDaily() {
		seccoService.resisterDaily(1);
	}
	
	public void getPointDaily() {
		seccoService.getPointDaily();
	}
	
}
