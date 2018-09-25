package com.guesslive.admin.service.thread;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesslive.admin.service.SeccoService;

/**
 * @Author xing.xia@yingheying.com
 * @Date 2018/9/14 17:43
 * @Description
 */
public class RegisterThread implements Runnable{
	
	@Autowired
	private SeccoService seccoService;
	
    int num;
    private int status = 99; //99-初始化  0-执行成功 1-执行失败
    public RegisterThread(SeccoService seccoService,int num) {
        this.num = num;
        this.seccoService = seccoService;
    }

    public void run() {
        System.out.println("注册线程启动.... 本线程注册数量 "+num);
        try {
            seccoService.resisterDaily(num);
        }catch (Exception e){
            status=0;
            e.printStackTrace();
        }
        status=0;
        System.out.println("注册线程结束.... 本线程注册数量 "+num);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
