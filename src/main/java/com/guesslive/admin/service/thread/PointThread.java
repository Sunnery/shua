package com.guesslive.admin.service.thread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.guesslive.admin.model.SeccoAccount;
import com.guesslive.admin.service.SeccoService;
/**
 * @Author xing.xia@yingheying.com
 * @Date 2018/9/14 17:43
 * @Description
 */
public class PointThread implements Runnable{
	
	@Autowired
	private SeccoService seccoService;
	
    int num;
    public List<SeccoAccount> seccoAccounts;
    private int status = 99; //99-初始化  0-执行成功 1-执行失败
    public PointThread(SeccoService seccoService) {
    	this.seccoService = seccoService;
    }

    public void run() {
        System.out.println("积分线程启动.... 线程序列 "+num);
        try {
        	seccoService.getPointDaily();
        }catch (Exception e){
            status=0;
            e.printStackTrace();
        }
        status=0;
        System.out.println("积分线程结束.... 线程序列 "+num);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
