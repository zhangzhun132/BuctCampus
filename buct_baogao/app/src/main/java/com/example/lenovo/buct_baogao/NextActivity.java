package com.example.lenovo.buct_baogao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.buct_baogao.HttpUtils.HttpImage;
import com.example.lenovo.buct_baogao.HttpUtils.HttpUtils;

/**
 * Created by Administrator on 2015/6/1.
 */
public class NextActivity extends Activity{
    private WebView webView;
    private ImageView imageView;
    private Bitmap bitmap;

    private final int GET_CODE_SUCCESS = 1;
    private final int GET_CODE_ERROR = 2;
    private final int LOGIN_SUCCESS = 3;
    private final int LOGIN_ERROR = 4;
    private final String postUrl="http://graduate.buct.edu.cn:8080/pyxx/txhdgl/hdlist.aspx?xh=2014210366";
    final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    int tag =  msg.what;

                    switch(tag){
                case GET_CODE_SUCCESS:
                    //imageView.setImageBitmap(bitmap);
                    break;
                case GET_CODE_ERROR:
                    Toast.makeText(NextActivity.this, "get code error", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_SUCCESS:
                    Toast.makeText(NextActivity.this, "login success", Toast.LENGTH_SHORT).show();

                    break;
                case LOGIN_ERROR:
                    Toast.makeText(NextActivity.this, "login error", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextlayout);
        webView=(WebView)this.findViewById(R.id.webView);
       // imageView=(ImageView)this.findViewById(R.id.imageView);
        Intent intent=getIntent();
        String Name=(String)intent.getStringExtra("CookieName");
        String Value=(String)intent.getStringExtra("CookieValue");
        String Doamin=(String)intent.getStringExtra("CookieDomain");
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        String cookieString = Name + "="
                + Value + "; domain="
                + Doamin;
        cookieManager.setCookie(postUrl, cookieString);
        CookieSyncManager.getInstance().sync();


        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return  true;
            }
        });
        webView.loadUrl(postUrl);
        new GetCodeThread().start();



    }
    private class GetCodeThread extends Thread {
        @Override
        public void run() {
            Message msg = new Message();
            try {
                msg.what = GET_CODE_SUCCESS;
                bitmap = HttpImage.getcode();
            } catch (Exception e) {
                msg.what = GET_CODE_ERROR;
                e.printStackTrace();
            }

            handler.sendMessage(msg);
        }
    }
}
