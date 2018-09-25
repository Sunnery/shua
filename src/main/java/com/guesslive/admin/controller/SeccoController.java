package com.guesslive.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guesslive.admin.common.util.StringUtil;
import com.guesslive.admin.service.SeccoService;
import com.guesslive.admin.service.thread.PointThread;
import com.guesslive.admin.service.thread.RegisterThread;

@Controller
@RequestMapping("/*")
public class SeccoController {
	
	@Autowired
	private SeccoService seccoService;
	
	@ResponseBody
	@RequestMapping(value="resisterDaily",method=RequestMethod.GET)
	public void resisterDaily(HttpServletRequest request,HttpServletResponse response, int num) throws Exception {
		new Thread(new RegisterThread(seccoService,num)).start();
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.getWriter().print(StringUtil.objTojson("注册线程启动成功！本次注册数量："+num));
	}
	
	@ResponseBody
	@RequestMapping(value="getPointDaily",method=RequestMethod.GET)
	public void getPointDaily(HttpServletRequest request,HttpServletResponse response) throws Exception {
		new Thread(new PointThread(seccoService)).start();
		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.getWriter().print(StringUtil.objTojson("获取积分线程启动成功！请勿重复刷新！"));
	}
	
}