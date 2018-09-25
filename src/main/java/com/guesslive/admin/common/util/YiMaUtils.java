package com.guesslive.admin.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author xing.xia@yingheying.com
 * @Date 2018/9/20 13:48
 * @Description
 */
public class YiMaUtils {

    public static void main(String[] args) throws Exception {
//        String mobile = getMobile("","11895");
//        String msg = getMsg(mobile,"11895");
        String code = getyzm("",4);
        System.out.println(code);
//        releaseMobile(mobile,"11895");
//        System.out.println(mobile);
    }

    public static String getMsg(String mobile,String projectNo) throws InterruptedException {
        String code = "3001";
        String url = "http://api.fxhyd.cn/UserInterface.aspx?action=getsms&token=00517875107a07791765d7a8b8ea64b2276408e0&release=1&itemid="+projectNo+"&mobile="+mobile;
//        long beginTime = System.currentTimeMillis();
        int count = 0;
        while("3001".equals(code)){
            count ++;
            String result = HttpClientUtil.sendByHttp(null,url);
            int index= result.indexOf("|");
            System.err.println("获取手机验证码 "+mobile+" "+result);
            if(index > 0){
                code = result.split("\\|")[1];
            }else{
                code = result;
            }
            Thread.sleep(6000);
        }
        //释放手机号
        YiMaUtils.releaseMobile(mobile,"11895");
        return code;
    }

    public static String getMobile(String mobile,String projectNo) throws Exception {
        String url = "http://api.fxhyd.cn/UserInterface.aspx?action=getmobile&token=00517875107a07791765d7a8b8ea64b2276408e0&itemid="+projectNo+"&excludeno=170.171.172.173.174.175.176.177.178.179";
        if(!mobile.isEmpty()){
            url = url+"&mobile="+mobile;
        }
        String result = HttpClientUtil.sendByHttp(null,url);
        if(result == null || "".equals(result)){
            result = HttpClientUtil.sendByHttp(null,url);
        }
        System.err.println("获取手机号 "+mobile+" "+result);
        int index = 0;
        try{
            index = result.indexOf("|");
        }catch (Exception e){
            throw  new Exception("获取手机号失败 "+e.getMessage());
        }

        if(index > 0){
            mobile = result.split("\\|")[1];
        }else{
            mobile = "15237313734 "+result;
        }
        System.err.println("获取手机号 "+mobile+" "+result.split("\\|")[0]);
        return mobile;
    }

    public static boolean releaseMobile(String mobile,String projectNo){
        String url = "http://api.fxhyd.cn/UserInterface.aspx?action=release&token=00517875107a07791765d7a8b8ea64b2276408e0&itemid="+projectNo+"&mobile="+mobile;
        String result = HttpClientUtil.sendByHttp(null,url);
        System.err.println("释放 "+mobile+" "+result);
        return true;
    }


    public static String getyzm(String body, int YZMLENGTH) {
//        body = "success|【寺库奢侈品】尊敬的寺库用户，您的验证码为7451(寺库客服绝不会索取，切勿告知他人)请在页面中输入以完成。如有疑问，请拨打贵宾专线400-87-56789咨询。";
        // 首先([a-zA-Z0-9]{YZMLENGTH})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{YZMLENGTH})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{YZMLENGTH})后面不能有数字出现

        //	获得数字字母组合
        //    Pattern p = Pattern   .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");

        //	获得纯数字
        Pattern p = Pattern.compile("(?<![0-9])([0-9]{" + YZMLENGTH+ "})(?![0-9])");

        Matcher m = p.matcher(body);
        if (m.find()) {
            return m.group(0);
        }
        return null;
    }
}
