package com.guesslive.admin.service.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guesslive.admin.common.util.HttpClientUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by peng.zhang on 2018/3/26.
 */
public class Test {
    public static void main(String[] args) {
//       String t = MD5Utils.stringToMD5("20180925" + "d5s02e3f42w3d7lu9ls03lds").toLowerCase();
//       System.out.println(t);
        String rsult1 = "jsonp4({\"retMsg\":\"成功\",\"object\":{\"points\":3800,\"list\":[{\"shortUrlBar\":\"http:\\/\\/s1se.cn\\/7fu6ba3m6n\",\"shortUrlQrBar\":\"http:\\/\\/s1se.cn\\/7fu6ba3m6n\",\"shortUrlQr\":\"http:\\/\\/s1se.cn\\/7fu6ba3m6n\",\"state\":1,\"redeemCode\":\"http:\\/\\/s1se.cn\\/7fu6ba3m6n\",\"startDate\":1537784694419,\"endDate\":1545580799000}],\"saleId\":31627,\"userPoints\":1790},\"retCode\":0})";
        System.out.println(rsult1);
        String clearStr = rsult1.replace("jsonp4({","{").replace("})","}");
        JSONObject jsonObject=JSONObject.parseObject(clearStr);
        String object = jsonObject.getString("object");
        JSONObject pointsObject=JSONObject.parseObject(object);
        String userPoints = pointsObject.getString("userPoints");
        String list = pointsObject.getString("list");
        List<String> lists = new ArrayList<>();
        lists = JSONObject.parseArray(list, String.class);
        String singleTicket = lists.get(0);
        JSONObject singleTicketObject=JSONObject.parseObject(singleTicket);
        String shortUrlBar = singleTicketObject.getString("shortUrlBar");
        System.out.println(shortUrlBar);

        String rsult2 = HttpClientUtil.sendByHttp(null, shortUrlBar);
        System.out.println(rsult2);

    }

//    MD5Utils.stringToMD5(new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis())) + "d5s02e3f42w3d7lu9ls03lds")
}
