package com.example.lenovo.buct_baogao.HttpUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/28.
 */
public class HttpUtils {
    public static String SESSID_name = null;
    public static String SESSID_value = null;

    public static String SESSID_domain = null;

    public  HttpUtils()
    {

    }

    public static  String getSESSID_name(){
        return SESSID_name;
    }
    public static  String getSESSID_value(){
        return SESSID_value;
    }
    public static  String getSESSID_domain(){
        return SESSID_domain;
    }

    public static  String getPostJson(String path,Map<String,Object> params,String encoding){
        String result="";

        List<BasicNameValuePair> basicNameValuePairs=new ArrayList<BasicNameValuePair>();
        DefaultHttpClient httpClient=new DefaultHttpClient();
        //HttpPost httpPost=new HttpPost(path);
        HttpGet httpGet=new HttpGet(path);
        if (params!=null&&!params.isEmpty())
        {
            for(Map.Entry<String,Object> entry:params.entrySet())
            {
                String key=entry.getKey();
                String value=entry.getValue().toString();
                BasicNameValuePair basicNameValuePair=new BasicNameValuePair(key,value);
                basicNameValuePairs.add(basicNameValuePair);
            }
        }
        try {
            UrlEncodedFormEntity urlEncodedFormEntity=new UrlEncodedFormEntity(basicNameValuePairs,encoding);
            //httpPost.setEntity(urlEncodedFormEntity);
            //HttpResponse httpResponse=httpClient.execute(httpPost);
            if(null != SESSID_name){
                httpGet.setHeader("Cookie", "ASP.NET_SessionId=" + SESSID_value);
            }
            HttpResponse httpResponse =httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode()==200)
            {

                List<Cookie> cookies=httpClient.getCookieStore().getCookies();
                if(cookies.isEmpty()){
                    Log.i("Tag", "-------Cookie NONE");
                }
                else
                {
                    for(int i=0;i< cookies.size();i++){
                        if ("ASP.NET_SessionId".equals(cookies.get(i).getName())) {
                            SESSID_name = cookies.get(i).getName();
                            SESSID_value = cookies.get(i).getValue();
                            SESSID_domain = cookies.get(i).getDomain();

                            break;
                        }
                    }
                }
                result= EntityUtils.toString(httpResponse.getEntity(),encoding);
                //result=getHtmlString(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }




}
