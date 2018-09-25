package com.guesslive.admin.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author xing.xia@yingheying.com
 * @Date 2018/9/14 16:32
 * @Description
 */
public class HttpClientUtil {
        private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
        static CloseableHttpClient httpClient;
        static CloseableHttpResponse httpResponse;

    public static void main(String[] args) throws Exception {
        startsecco();
    }

    public static void startsecco(){
        List<String> list = new ArrayList<>();
            for(int i = 100;i<=140;i++){
                String result = secco(i);
                if(!"".equals(result)){
                    list.add(result);
                }

            }
            for(String a:list){
                System.err.println(a);
            }
    }
        public static String secco(int num){
            String result = "";
        try {
            Map<String, Object> map = new HashMap<>();
            String rsult1 = HttpClientUtil.sendByHttp(map, "https://user-center.secoo.com/service/appapi/points/mall/product/detail?productId="+num+"&upk=581903016E09D29A64C24CAC6B2C30943D656C76135D5665089292C437542865EA40206A92B3B9C164B2A0D49C345FEA6B6495247C71DBD77BD233303BCFF48DA0D3355E9ACD2A0B2F892115DD6D245DF5257D5C97659A3F491D7AE483F557F80B74947EA6909FB68133BE6E6DE597D0A9BD253777F7F740C7BD&_=1537355011004&callback=jsonp2");
            String clearStr = rsult1.replace("jsonp2({","{").replace("})","}");
            System.out.println(clearStr);
            JSONObject jsonObject=JSONObject.parseObject(clearStr);
            String retMsg = jsonObject.getString("retMsg");
            if("成功".equals(retMsg)){
                String objectStr = jsonObject.getString("object");
                JSONObject objects =JSONObject.parseObject(objectStr);
                Integer beAmount =objects.getInteger("beAmount");
                if(beAmount <= 5000){
                    String extObj = objects.getString("extObj");
                    String productName = objects.getString("productName");
                    String id = objects.getString("id");
                    System.err.println(productName+":"+beAmount+"积分");
                    result = "id:"+id+" "+productName+":"+beAmount+"积分";
                }
            }
        }catch (Exception e){

        }
            return result;
        }


    public static CloseableHttpClient createSSLClientDefault() {
            try {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                    // 信任所有
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                }).build();
                HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
                return HttpClients.custom().setSSLSocketFactory(sslsf).build();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
            return HttpClients.createDefault();

        }

        /**
         * 发送https请求
         *
         * @param
         * @throws Exception
         */
        public static synchronized String sendByHttp(Map<String, Object> params, String url) {
            try {
                List<NameValuePair> listNVP = new ArrayList<NameValuePair>();
                if (params != null) {
                    for (String key : params.keySet()) {
                        listNVP.add(new BasicNameValuePair(key, params.get(key).toString()));
                    }
                }
                HttpPost httpPost = new HttpPost(url);
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listNVP, "UTF-8");
                logger.info("创建请求httpPost-URL={},params={}", url, listNVP);
                httpPost.setEntity(entity);
                httpPost.setHeader("User-Agent","Secoo-iPhone/6.0.10 (iPhone; iOS 11.4.1; Scale/2.00)");
                httpPost.setHeader("session_id","AF13629A8EF144739112594442E43B3F");
                httpPost.setHeader("Cookie","SUN=18684557128; Sid=79680101C8D6494450C65B3454F6F1FF896F19FB135D5665089292C437542837B7147F3B97BCB8CC62B5F1D49E355DB36A3792707521DAD27AD164353B9AA08DA0D3355E9ACD2A0B2F892115DF6D2656F12D735F95649D3E491D70B6D1F505FE0A73C770A3929FB78235BE6C38EC9E85A8BB783376A1A7129BBA; session_secoo_id=AF13629A8EF144739112594442E43B3F; gr_user_id=90b8e105-5926-4923-b6e3-6ca33c0e80b1; grwng_uid=759adf46-f603-474e-a201-d15d7a1f0c9c");
                httpPost.setHeader("upk","");
                httpPost.setHeader("app-id","644873678");
                httpPost.setHeader("X-Tingyun-Id","IYSvMxBahhI;c=2;r=291089862;u=cb3142da2891f55386891279351266ee::703268AA7228D3C1");
                httpPost.setHeader("channel","AppStore");
                httpPost.setHeader("device-id","66121BC4-1789-45C6-8F67-420D38D45938");
//                httpPost.setHeader("Content-Length","113");
                httpPost.setHeader("platform-type","0");
                httpPost.setHeader("platform","iPhone8,1");
                httpPost.setHeader("Connection","keep-alive");
                httpPost.setHeader("Accept-Language","zh-Hans-CN;q=1");
                httpPost.setHeader("idfv","2528EE15-F9A8-4192-AF30-2EE18E2254D4");
                httpPost.setHeader("app-ver","6.0.10");
                httpPost.setHeader("product","0");
                httpPost.setHeader("platform-ver","11.4.1");
                httpPost.setHeader("Accept","*/*");
                httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
                httpPost.setHeader("Accept-Encoding","gzip");
                httpPost.setHeader("idfa","DF9AE239-26AA-4D13-AF40-76B76322592F");

                httpClient = HttpClientUtil.createSSLClientDefault();
                httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                    return jsObject;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    httpResponse.close();
                    httpClient.close();
                    logger.info("请求流关闭完成");
                } catch (IOException e) {
                    logger.info("请求流关闭出错");
                    e.printStackTrace();
                }
            }
        }


    public static String sendByHttpForShare(Map<String, Object> params, String url,String mobile,String upk) {
        try {
            List<NameValuePair> listNVP = new ArrayList<NameValuePair>();
            if (params != null) {
                for (String key : params.keySet()) {
                    listNVP.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listNVP, "UTF-8");
            logger.info("创建请求httpPost-URL={},params={}", url, listNVP);
            httpPost.setEntity(entity);
            httpPost.setHeader("User-Agent","Secoo-iPhone/6.0.10 (iPhone; iOS 11.4.1; Scale/2.00)");
            httpPost.setHeader("session_id","AF13629A8EF144739112594442E43B3F");
            httpPost.setHeader("Cookie","SUN="+mobile+"; Sid="+upk+"; gr_user_id=90b8e105-5926-4923-b6e3-6ca33c0e80b1; grwng_uid=759adf46-f603-474e-a201-d15d7a1f0c9c");
            httpPost.setHeader("upk",upk);
            httpPost.setHeader("app-id","644873678");
            httpPost.setHeader("X-Tingyun-Id","IYSvMxBahhI;c=2;r=291089862;u=cb3142da2891f55386891279351266ee::703268AA7228D3C1");
            httpPost.setHeader("channel","AppStore");
            httpPost.setHeader("device-id","66121BC4-1789-45C6-8F67-420D38D45938");
//                httpPost.setHeader("Content-Length","113");
            httpPost.setHeader("platform-type","0");
            httpPost.setHeader("platform","iPhone8,1");
            httpPost.setHeader("Connection","keep-alive");
            httpPost.setHeader("Accept-Language","zh-Hans-CN;q=1");
            httpPost.setHeader("idfv","2528EE15-F9A8-4192-AF30-2EE18E2254D4");
            httpPost.setHeader("app-ver","6.0.10");
            httpPost.setHeader("product","0");
            httpPost.setHeader("platform-ver","11.4.1");
            httpPost.setHeader("Accept","*/*");
            httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
            httpPost.setHeader("Accept-Encoding","gzip");
            httpPost.setHeader("idfa","DF9AE239-26AA-4D13-AF40-76B76322592F");

            httpClient = HttpClientUtil.createSSLClientDefault();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                String jsObject = EntityUtils.toString(httpEntity, "UTF-8");
                return jsObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpResponse.close();
                httpClient.close();
                logger.info("请求流关闭完成");
            } catch (IOException e) {
                logger.info("请求流关闭出错");
                e.printStackTrace();
            }
        }
    }

         public static boolean go(){
             Map<String, Object> map = new HashMap<>();
             map.put("signature", "9CE47538D53B3150352898977DEF7886");
             map.put("act", "getList");
             map.put("partnerCode", "ios_api");
             map.put("c", "goods");
             map.put("format", "json");
             map.put("pagesize", "10");
             map.put("token", "e4033e326b701abeef9d27e2e4b71b4546b7d068");
             map.put("page", "1");
             map.put("timestamp", "20180914124111");
             map.put("cosVersion", "15007");
             String rsult1 = HttpClientUtil.sendByHttp(map, "https://cos1.changan001.cn/api/webserver_api.php");

             Map<String, Object> map1 = new HashMap<>();
             map1.put("signature", "969C306DB2DE59E743D243B9518252BB");
             map1.put("act", "getList");
             map1.put("partnerCode", "ios_api");
             map1.put("c", "goods");
             map1.put("format", "json");
             map1.put("pagesize", "10");
             map1.put("token", "50b0412f95dda48e45b2130cb9429e1b67f556ce");
             map1.put("page", "2");
             map1.put("timestamp", "20180914170912");
             map1.put("cosVersion", "15007");
             String rsult2 = HttpClientUtil.sendByHttp(map1, "https://cos1.changan001.cn/api/webserver_api.php");

             if(queryGoods(rsult1) || queryGoods(rsult2)){
                 notifySMS();
                 return false;
             }else{
                 return true;
             }
         }

         public static boolean queryGoods(String str){
        boolean isStop = false;
        JSONObject jsonObject=JSONObject.parseObject(str);
        String data = jsonObject.getString("data");
        JSONObject jsonObjectData=JSONObject.parseObject(data);
        String lists = jsonObjectData.getString("list");
        JSONArray jsonArray = JSON.parseArray(lists);
        for (Object obj : jsonArray) {
            JSONObject jsonObjecta = (JSONObject) obj;
            if( jsonObjecta.getInteger("goods_number")>0){
                System.err.println(jsonObjecta.getString("goods_name")+"库存："+jsonObjecta.getInteger("goods_number"));
                if(jsonObjecta.getInteger("goods_id") == 81 //电源
                        || jsonObjecta.getInteger("goods_id") == 83 //排插
                        ){
                    isStop = true;
                }
            }else {
                System.out.println(jsonObjecta.getString("goods_name")+"库存："+jsonObjecta.getInteger("goods_number"));
            }

        }
        return  isStop;
    }


    static void notifySMS() {

    }

}