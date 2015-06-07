package com.example.lenovo.buct_baogao.HttpUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Administrator on 2015/5/28.
 */
public class HttpImage {
    public  HttpImage()
    {

    }

    public static Bitmap getcode() {
        HttpClient client=new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://graduate.buct.edu.cn:8080/pyxx/PageTemplate/NsoftPage/yzm/createyzm.aspx");
        HttpResponse httpimage = null;
        try {
            httpimage = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = new byte[0];
        try {
            bytes = EntityUtils.toByteArray(httpimage.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }



}