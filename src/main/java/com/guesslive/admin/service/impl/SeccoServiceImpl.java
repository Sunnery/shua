package com.guesslive.admin.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.guesslive.admin.common.util.ChaoJiYing;
import com.guesslive.admin.common.util.DateUntils;
import com.guesslive.admin.common.util.HttpClientUtil;
import com.guesslive.admin.common.util.MD5Utils;
import com.guesslive.admin.common.util.RandomData;
import com.guesslive.admin.common.util.YiMaUtils;
import com.guesslive.admin.dao.SeccoMapper;
import com.guesslive.admin.model.SeccoAccount;
import com.guesslive.admin.model.StatBucks;
import com.guesslive.admin.service.SeccoService;

@Service
public class SeccoServiceImpl implements SeccoService {
	
	@Autowired
	private SeccoMapper seccoMapper;
	
	protected final Logger logger = Logger.getLogger(getClass());
	
    public void resisterDaily (int num) {
        List<String> list = new ArrayList<>();
        String mobile = "";
        for(int i=0;i<num;i++){
        	try {
            //获取手机号
            mobile = YiMaUtils.getMobile("","11895");
            SeccoAccount seccoAccount = seccoMapper.getSeccoAccount(mobile);
            //获取到重复手机号 释放后重新获取
            while(seccoAccount != null){
                YiMaUtils.releaseMobile(mobile,"11895");
                mobile = YiMaUtils.getMobile("","11895");
                seccoAccount = seccoMapper.getSeccoAccount(mobile);
            }
                register(mobile);
                list.add("NO:"+i+" "+mobile+" 注册成功！");
            }catch (Exception e){
                e.printStackTrace();
                list.add("NO:"+i+" "+mobile+e.getMessage());
            }
        }
        for(String s:list){
            System.err.println(s);
        }
    }

    public List<String> resetpwdBatch (List<String> list){
        List<String> resultList = new ArrayList();
        List<String> collect = new ArrayList();
        for(String mobile:list){
            try {
                String result = resetPwd(mobile);
                resultList.add(mobile);
                collect.add(mobile+":"+result);
            }catch (Exception e){
                e.printStackTrace();
                collect.add(mobile+":"+e.getMessage());
            }
        }
        for(String s:collect){
            System.err.println(s);
        }
        return resultList;
    }

    public void getPointDaily(){
    	String today = DateUntils.getToday();
    	List<SeccoAccount> seccoAccounts = seccoMapper.getUndealSeccoAccountList(today,0,10000);
    	logger.info("########## 获取积分 总记录数："+seccoAccounts.size()+" ###############");
        List<String> result = new ArrayList();
        for(SeccoAccount s:seccoAccounts){
            String mobile = s.getMobile();
            try {
                SeccoAccount seccoAccount =seccoMapper.getSeccoAccount(mobile);
                String upk = loginFather(mobile);
                String oldPoint = getPoint(mobile,upk);
                if(!DateUntils.getToday().equals(DateUntils.getYml(seccoAccount.getLst_sign_date()))){
                    sgin(mobile,upk);
                    seccoAccount.setLst_sign_date(DateUntils.getTodayFull());
                }
                if(!DateUntils.getToday().equals(DateUntils.getYml(seccoAccount.getLst_share_date()))){
                    share(mobile,upk);
                    seccoAccount.setLst_share_date(DateUntils.getTodayFull());
                }
                if(Integer.parseInt(seccoAccount.getPoint())< 500){
                    updateUserInfo(mobile,upk);
                }
                String nowPoint = getPoint(mobile,upk);

                String resultSingle = mobile+" 之前积分:"+oldPoint+" 之后积分"+nowPoint;
                seccoAccount.setLst_upt_time(DateUntils.getTodayFull());
                seccoAccount.setPoint(nowPoint);
                seccoAccount.setAdd_point_today(String.valueOf(Integer.parseInt(nowPoint)-Integer.parseInt(oldPoint)));
                seccoMapper.updateSeccoAccount(seccoAccount);
                //兑换星巴克
//                if(Integer.parseInt(nowPoint) >= 380){
//                    buy(128,mobile,upk);
//                }
                result.add(resultSingle);
            }catch (Exception e){
                e.printStackTrace();
                String resultSingle = mobile+" : "+e.getMessage();
                result.add(resultSingle);
            }
        }
        for(String s:result){
            System.err.println(s);
        }
    }

    public String loginFather(String mobile) throws Exception{
        String result = login(mobile);
        if("resetPwd".equals(result)){
            resetPwd(mobile);
            result = login(mobile);
        }
        return result;
    }

    public String login(String mobile) throws Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("method", "user.login");
        map.put("cck", "");
        map.put("geocoding", "(null),(null)");
        map.put("password", "dc483e80a7a0bd9ef71d8cf973673924");
        map.put("userName", mobile);
        String rsult1 = HttpClientUtil.sendByHttp(map, "https://user-center.secoo.com/service/appapi?method=user.login");
        System.out.println(rsult1);
        if(rsult1 == null || "".equals(rsult1)){
            rsult1 = HttpClientUtil.sendByHttp(null,"https://user-center.secoo.com/service/appapi?method=user.login");
        }
        JSONObject jsonObject=JSONObject.parseObject(rsult1);
        //验证码
        if(jsonObject.getInteger("retCode") == 10012){
            String getCodeUrl = "https://user-center.secoo.com/service/appapi/captcha/v1?bizType=login";
            String getCodeRsult = HttpClientUtil.sendByHttp(null, getCodeUrl);
            JSONObject getCodeJsonObject=JSONObject.parseObject(getCodeRsult);
            if(getCodeJsonObject.getInteger("retCode")==0){
                String getCodeJsonObject2 = getCodeJsonObject.getString("object");
                JSONObject getCodeJsonObject3=JSONObject.parseObject(getCodeJsonObject2);
                String cck = getCodeJsonObject3.getString("cck");
                String base64Img = getCodeJsonObject3.getString("base64Img");
                System.err.println("验证码图片 ："+base64Img);
                String verifyStr = ChaoJiYing.PostPic_base64("1004",base64Img);
                JSONObject verifyStrObj=JSONObject.parseObject(verifyStr);
                String picId = verifyStrObj.getString("pic_id");
                String verifyCode = verifyStrObj.getString("pic_str");
                System.err.println("识别结果 ："+verifyCode);

                //登陆
                map.put("cck", cck);
                map.put("verifyCode", verifyCode);
                rsult1 = HttpClientUtil.sendByHttp(map, "https://user-center.secoo.com/service/appapi?method=user.login");
                jsonObject=JSONObject.parseObject(rsult1);

                //15903873100 异常!!登陆时验证码自动识别失败 {"retMsg":"密码输入错误超过5次后,账户将被锁定。如您忘记密码,请尝试找回","retCode":10002}
                //15164443467 异常!!登陆时验证码自动识别失败 {"retMsg":"账号与密码不匹配","retCode":10001}
                if(jsonObject.getInteger("retCode") == 10001 || jsonObject.getInteger("retCode") == 10002){
//                    return "resetPwd";
                        throw  new Exception(rsult1);
                }
                if(jsonObject.getInteger("retCode") != 0){
                    //验证失败 报错返分
                    ChaoJiYing.ReportError(picId);
                    throw new Exception("登陆时验证码自动识别失败 "+rsult1);
                }

            }else{
                throw new Exception("登陆时获取验证图片失败  "+getCodeRsult);
            }
        }

        if(jsonObject.getInteger("retCode") == 10001 || jsonObject.getInteger("retCode") == 10002){
//            return "resetPwd";
            throw  new Exception(rsult1);

        }
        if(jsonObject.getInteger("retCode") != 0){
            throw new Exception(rsult1);
        }
        String object = jsonObject.getString("object");
        JSONObject object1=JSONObject.parseObject(object);
        String upk = object1.getString("upk");
        return upk;
    }

    public void sgin(String mobile,String upk) throws Exception{
        String url = "https://user-center.secoo.com/service/appapi/user/signin?upk="+upk+"&_=1537514663154&callback=jsonp14";
        String rsult1 = HttpClientUtil.sendByHttp(null, url);
        String clearStr = rsult1.replace("jsonp14({","{").replace("})","}");
        System.err.println("每日签到"+clearStr);
    }

    public void share(String mobile,String upk) throws Exception{
        String url = "https://user-center.secoo.com/service/appapi/user/share?bid=1&t="+MD5Utils.getSahreMd5();
        String rsult1 = HttpClientUtil.sendByHttpForShare(null, url,mobile,upk);
        System.err.println("每日分享"+rsult1);
    }

    public void updateUserInfo(String mobile,String upk) throws Exception{
        String baseInfoUrl = "https://user-center.secoo.com/service/appapi/user/info/update";
        String name = RandomData.getChineseName();
        Map<String, Object> map = new HashMap<>();
        map.put("realName", name);
        map.put("gender", "1");
        map.put("birthday", "1987-06-15");
        map.put("label", "[\""+RandomData.getXingzuo()+"\"]");
        String rsult1 = HttpClientUtil.sendByHttpForShare(map, baseInfoUrl,mobile,upk);
        System.err.println("修改基本信息 ："+rsult1);
        JSONObject jsonObject=JSONObject.parseObject(rsult1);
        if(jsonObject.getInteger("recode") != 0){
            throw new Exception(rsult1);
        }

        String addressInfoUrl = "https://las.secoo.com/api/user/vip/save_address";
        Map<String, Object> map1 = new HashMap<>();
        map1.put("address", RandomData.getRoad());
        map1.put("area", "市辖区");
        map1.put("city", "合肥市");
        map1.put("name", name);
        map1.put("phone", mobile);
        map1.put("province", "安徽省");
        String rsult = HttpClientUtil.sendByHttpForShare(map1, addressInfoUrl,mobile,upk);
        System.err.println("修改收货地址"+rsult);
        JSONObject jsonObject1=JSONObject.parseObject(rsult1);
        if(jsonObject1.getInteger("recode") != 0){
            throw new Exception(rsult1);
        }
    }

    public String getPoint(String mobile,String upk) throws Exception{
        String url = "https://user-center.secoo.com/service/appapi/user/points/get?upk="+upk+"&_=1537518144352&callback=jsonp3";
        String rsult1 = HttpClientUtil.sendByHttpForShare(null, url,mobile,upk);
        System.out.println("当前积分："+rsult1);
        String clearStr = rsult1.replace("jsonp3({","{").replace("})","}");
        System.out.println("截取后json :"+clearStr);
        JSONObject jsonObject=JSONObject.parseObject(clearStr);
        if(jsonObject.getInteger("retCode") != 0){
            throw new Exception(rsult1);
        }
        String object = jsonObject.getString("object");
        JSONObject pointsObject=JSONObject.parseObject(object);
        String points = pointsObject.getString("points");
        SeccoAccount seccoAccount =seccoMapper.getSeccoAccount(mobile);
        seccoAccount.setPoint(points);
        seccoMapper.updateSeccoAccount(seccoAccount);
        return points;
    }

    public String resetPwd(String mobile) throws Exception{
        //获取token
        String url = "https://user-center.secoo.com/service/appapi/user/findpwd/verify";
        Map<String, Object> map = new HashMap<>();
        map.put("cck", "");
        map.put("mobile", mobile);
        map.put("verifyCode", "");
        String rsult1 = HttpClientUtil.sendByHttp(map, url);
        System.out.println("获取token返回json:"+rsult1);
        JSONObject jsonObject=JSONObject.parseObject(rsult1);
        String token = "";
        //需要校验验证码
        if("10003".equals(jsonObject.getString("retCode"))){
            String getCodeUrl = "https://user-center.secoo.com/service/appapi/captcha/v1?bizType=findpwd";
            Map<String, Object> getCodmap = new HashMap<>();
            getCodmap.put("bizType", "findpwd");
            String getCodeRsult = HttpClientUtil.sendByHttp(getCodmap, getCodeUrl);
            JSONObject getCodeJsonObject=JSONObject.parseObject(getCodeRsult);
            if(getCodeJsonObject.getInteger("retCode")==0){
                String getCodeJsonObject2 = getCodeJsonObject.getString("object");
                JSONObject getCodeJsonObject3=JSONObject.parseObject(getCodeJsonObject2);
                String cck = getCodeJsonObject3.getString("cck");
                String base64Img = getCodeJsonObject3.getString("base64Img");
                System.err.println("验证码图片 ："+base64Img);
                String verifyStr = ChaoJiYing.PostPic_base64("1004",base64Img);
                JSONObject verifyStrObj=JSONObject.parseObject(verifyStr);
                String picId = verifyStrObj.getString("pic_id");
                String verifyCode = verifyStrObj.getString("pic_str");
                System.err.println("识别结果 ："+verifyCode);
                //再次获取token
                String urlAgain = "https://user-center.secoo.com/service/appapi/user/findpwd/verify";
                Map<String, Object> mapAgain = new HashMap<>();
                mapAgain.put("cck", cck);
                mapAgain.put("mobile", mobile);
                mapAgain.put("verifyCode", verifyCode);
                String rsult1Again = HttpClientUtil.sendByHttp(mapAgain, urlAgain);
                System.err.println("再次获取token返回json:"+rsult1Again);
                JSONObject jsonObjectAgain=JSONObject.parseObject(rsult1Again);
                if(jsonObjectAgain.getInteger("retCode")!=0){
                    //验证失败 报错返分
                    ChaoJiYing.ReportError(picId);
                    throw new Exception("重置密码时验证码自动识别失败");
                }else{
                    String object = jsonObjectAgain.getString("object");
                    JSONObject pointsObject=JSONObject.parseObject(object);
                    token = pointsObject.getString("token");
                }
            }else{
                throw new Exception("重置密码时获取验证图片失败  "+getCodeRsult);
            }
        }else{
            String object = jsonObject.getString("object");
            JSONObject pointsObject=JSONObject.parseObject(object);
            token = pointsObject.getString("token");
        }
        //发送验证码
        String url2 = "https://user-center.secoo.com/service/appapi/user/findpwd/sendmsg";
        Map<String, Object> map2 = new HashMap<>();
        map2.put("token",token);
        String rsult2 = HttpClientUtil.sendByHttp(map2, url2);
        System.out.println("发送验证码返回json:"+rsult2);
        JSONObject jsonObject2=JSONObject.parseObject(rsult2);
        if(jsonObject2.getInteger("retCode")!=0){
            throw  new Exception(rsult2);
        }
        String object2 = jsonObject2.getString("object");
        JSONObject pointsObject2=JSONObject.parseObject(object2);
        String token2 = pointsObject2.getString("token");

        //对接接码平台
        String getMobile = YiMaUtils.getMobile(mobile,"11895");
        //无法获取手机号
        if(!getMobile.equals(mobile)){
            System.err.println("无法获取手机号:"+mobile);
            throw new Exception("无法获取手机号");
        }
        String msg = YiMaUtils.getMsg(getMobile,"11895");
        if("-1".equals(msg)){
            throw new Exception("无法获取短信验证码");
        }
        String code = YiMaUtils.getyzm(msg,6);
        if("".equals(code) || code == null){
            throw new Exception("无法解析短信："+msg);
        }

        //重置密码
        String url3 = "https://user-center.secoo.com/service/appapi/user/findpwd";
        Map<String, Object> map3 = new HashMap<>();
        map3.put("token",token2);
        map3.put("newPwd","dc483e80a7a0bd9ef71d8cf973673924");
        map3.put("mobileCode",code);
        System.out.println("map3 == "+map3);
        String rsult3 = HttpClientUtil.sendByHttp(map3, url3);
        System.out.println("重置密码返回json:"+rsult3);
        JSONObject jsonObject3=JSONObject.parseObject(rsult3);
        if(jsonObject3.getInteger("retCode")!=0){
            throw  new Exception(rsult3);
        }
        return rsult3;
    }



    public String register(String mobile) throws Exception{

        //发送验证码
        String url = "https://user-center.secoo.com/service/appapi/user/register/msg/send";
        Map<String, Object> map = new HashMap<>();
        map.put("cck", "");
        map.put("mobile", mobile);
        map.put("verifyCode", "");
        String rsult1 = HttpClientUtil.sendByHttp(map, url);
        JSONObject jsonObject=JSONObject.parseObject(rsult1);
        //需要校验验证码
        if("10003".equals(jsonObject.getString("retCode"))) {
            String getCodeUrl = "https://user-center.secoo.com/service/appapi/captcha/v1?bizType=reg";
            Map<String, Object> getCodmap = new HashMap<>();
            getCodmap.put("bizType", "reg");
            String getCodeRsult = HttpClientUtil.sendByHttp(getCodmap, getCodeUrl);
            JSONObject getCodeJsonObject = JSONObject.parseObject(getCodeRsult);
            if (getCodeJsonObject.getInteger("retCode") == 0) {
                String getCodeJsonObject2 = getCodeJsonObject.getString("object");
                JSONObject getCodeJsonObject3 = JSONObject.parseObject(getCodeJsonObject2);
                String cck = getCodeJsonObject3.getString("cck");
                String base64Img = getCodeJsonObject3.getString("base64Img");
                System.err.println("验证码图片 ：" + base64Img);
                String verifyStr = ChaoJiYing.PostPic_base64("1004", base64Img);
                JSONObject verifyStrObj = JSONObject.parseObject(verifyStr);
                String picId = verifyStrObj.getString("pic_id");
                String verifyCode = verifyStrObj.getString("pic_str");
                System.err.println("识别结果 ：" + verifyCode);
                //再次发送短信验证码
                String urlAgain = "https://user-center.secoo.com/service/appapi/user/register/msg/send";
                Map<String, Object> mapAgain = new HashMap<>();
                mapAgain.put("cck", cck);
                mapAgain.put("mobile", mobile);
                mapAgain.put("verifyCode", verifyCode);
                String rsult1Again = HttpClientUtil.sendByHttp(mapAgain, urlAgain);
                System.err.println("再次发送短信验证码返回json:" + rsult1Again);
                jsonObject = JSONObject.parseObject(rsult1Again);
                if (jsonObject.getInteger("retCode") != 0) {
                    //验证失败 报错返分
                    ChaoJiYing.ReportError(picId);
                    throw new Exception("注册时时验证码自动识别失败");
                }
            }
        }
        if(jsonObject.getInteger("retCode")!=0){
            throw  new Exception("注册时发送验证码失败"+rsult1);
        }

        String msg = YiMaUtils.getMsg(mobile,"11895");
        if("-1".equals(msg)){
            throw new Exception("无法获取短信验证码");
        }
        String code = YiMaUtils.getyzm(msg,6);
        if("".equals(code) || code == null){
            throw new Exception("无法解析短信："+msg);
        }

        //注册
        String url3 = "https://user-center.secoo.com/service/appapi/user/register";
        Map<String, Object> map3 = new HashMap<>();
        map3.put("geocoding","(null),(null)");
        map3.put("password","dc483e80a7a0bd9ef71d8cf973673924");
        map3.put("userName",mobile);
        map3.put("verifyCode",code);
        System.out.println("map3 == "+map3);
        String rsult3 = HttpClientUtil.sendByHttp(map3, url3);
        System.out.println("新用户注册返回json:"+rsult3);
        JSONObject jsonObject3=JSONObject.parseObject(rsult3);
        if(jsonObject3.getInteger("retCode")!=0){
            throw  new Exception("注册失败 "+rsult3);
        }

        SeccoAccount seccoAccount = new SeccoAccount();
        seccoAccount.setLst_upt_time(DateUntils.getYesterDay());
        seccoAccount.setAdd_point_today("0");
        seccoAccount.setPoint("0");
        seccoAccount.setLst_share_date(DateUntils.getYesterDay());
        seccoAccount.setLst_sign_date(DateUntils.getYesterDay());
        seccoAccount.setBefore_point("0");
        seccoAccount.setCrt_time(DateUntils.getTodayFull());
        seccoAccount.setStatus("0");
        seccoAccount.setMobile(mobile);
        seccoMapper.addSeccoAccount(seccoAccount);
        return mobile;
    }

    public void buy(int productId,String mobile,String upk) throws Exception {
        String url = "https://user-center.secoo.com/service/appapi/points/mall/product/buy?businessId=206&channelId=13&num=1&productId="+productId+"&upk="+upk+"&_=1537712334589&callback=jsonp4";
        String rsult1 = HttpClientUtil.sendByHttpForShare(null, url,mobile,upk);
        System.out.println(rsult1);
        String clearStr = rsult1.replace("jsonp4({","{").replace("})","}");
        JSONObject jsonObject=JSONObject.parseObject(clearStr);
        if(jsonObject.getInteger("retCode") != 0){
            throw new Exception(rsult1);
        }

        String object = jsonObject.getString("object");
        JSONObject pointsObject=JSONObject.parseObject(object);
        String userPoints = pointsObject.getString("userPoints");
        String list = pointsObject.getString("list");
        List<String> lists = new ArrayList<>();
        lists = JSONObject.parseArray(list, String.class);
        String singleTicket = lists.get(0);
        JSONObject singleTicketObject=JSONObject.parseObject(singleTicket);
        String shortUrlBar = singleTicketObject.getString("shortUrlBar");

        SeccoAccount seccoAccount =seccoMapper.getSeccoAccount(mobile);
        seccoAccount.setPoint(userPoints);
        seccoAccount.setExchanged(seccoAccount.getExchanged()+";"+shortUrlBar);
        seccoAccount.setExchanged_date(DateUntils.getTodayFull());
        seccoMapper.updateSeccoAccount(seccoAccount);

        StatBucks statBucks = new StatBucks();
        statBucks.setCrt_date(DateUntils.getTodayFull());
        statBucks.setLst_update(DateUntils.getTodayFull());
        statBucks.setMobile(mobile);
        statBucks.setStatus("0");
        statBucks.setUrl(shortUrlBar);
        seccoMapper.addStarBucks(statBucks);
    }

}
