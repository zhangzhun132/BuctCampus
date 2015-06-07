package com.example.lenovo.buct_baogao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.lenovo.buct_baogao.HttpUtils.HttpUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {



    private Button button;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private RadioButton radioButton;
    private RadioButton radioButton1;
    private RadioGroup radioGroup;
    private String username="";
    private String password="";
    static String YES = "yes";
    static String NO = "no";
    static String name, pass;
    private String isMemory = "";//isMemory变量用来判断SharedPreferences有没有数据，包括上面的YES和NO
    private String FILE = "saveUserNamePwd";//用于保存SharedPreferences的文件
    private SharedPreferences sp = null;//声明一个SharedPreferences
    private final int GET_CODE_SUCCESS = 1;
    private final int PASSWORD_ERROR = 2;
    private final int LOGIN_SUCCESS = 3;
    private final int USERNAME_ERROR = 4;


    private static String url = "";
    private static String postUrl="";
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int tag =  msg.what;

            switch(tag){

                case LOGIN_SUCCESS:
                    Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                    break;
                case PASSWORD_ERROR:
                    Toast.makeText(MainActivity.this, "密码错误！如您忘记密码，请联系学院秘书进行密码重置", Toast.LENGTH_SHORT).show();
                    break;
                case USERNAME_ERROR:
                    Toast.makeText(MainActivity.this, "请输入正确的用户名！", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);


        button = (Button) this.findViewById(R.id.button);
        radioButton=(RadioButton)this.findViewById(R.id.radioButton);
        radioButton1=(RadioButton)this.findViewById(R.id.radioButton2);
        radioGroup=(RadioGroup)this.findViewById(R.id.radioGroup);
        usernameEditText=(EditText)this.findViewById(R.id.editText);
        passwordEditText=(EditText)this.findViewById(R.id.editText2);


        sp = getSharedPreferences(FILE, MODE_PRIVATE);
        isMemory = sp.getString("isMemory", NO);
//进入界面时，这个if用来判断SharedPreferences里面name和password有没有数据，有的话则直接打在EditText上面
        if (isMemory.equals(YES)) {
            name = sp.getString("name", "");
            pass = sp.getString("password", "");
            usernameEditText.setText(name);
            passwordEditText.setText(pass);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(name, usernameEditText.toString());
        editor.putString(pass, passwordEditText.toString());
        editor.commit();
        button.setOnClickListener(this);









    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button:

                username=usernameEditText.getText().toString();
                password=passwordEditText.getText().toString();

                if (username == null || username.equals("")
                        || password == null || password.equals("")) {
                    Toast.makeText(MainActivity.this, "用户名或密码不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {

                    url="http://graduate.buct.edu.cn:8080/pyxx/MyService.ashx?"+"username="+username+"&&password="+password;


                    new Thread(new webviewThread()).start();
                    //new Thread(new userinfoThread()).start();
                }
                break;






        }
    }

    public class webviewThread implements  Runnable{

        @Override
        public void run() {
            Map<String,Object> map=new LinkedHashMap<String,Object>();
            Message message=Message.obtain();
            String result=HttpUtils.getPostJson(url,map,"utf-8");

            System.out.println("-->result"+result);
            //String resultPost=HttpUtils.getPostJson(postUrl,map,"utf-8");
            //System.out.println("-->resultPost"+resultPost);

            String nameData=HttpUtils.SESSID_name;
            String valueData=HttpUtils.SESSID_value;
            String domainData=HttpUtils.SESSID_domain;
            if(result.contains("1"))
            {
                message.what=LOGIN_SUCCESS;
                handler.sendMessage(message);
                Bundle bundle=new Bundle();
                bundle.putString("CookieName",nameData);
                bundle.putString("CookieValue",valueData);
                bundle.putString("CookieDomain",domainData);
                //SaveUserDate();
                remenber();

                Intent intent =new Intent(MainActivity.this,NextActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            if (result.contains("0")){
                message.what=PASSWORD_ERROR;
                handler.sendMessage(message);
            }
            if (result.contains("2")){
                message.what=USERNAME_ERROR;
                handler.sendMessage(message);
            }




        }
    }

    // remenber方法用于判断是否记住密码，checkBox1选中时，提取出EditText里面的内容，放到SharedPreferences里面的name和password中
    public void remenber() {
        if (radioButton.isChecked()) {
            if (sp == null) {
                sp = getSharedPreferences(FILE, MODE_PRIVATE);
            }
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("name", usernameEditText.getText().toString());
            edit.putString("password", passwordEditText.getText().toString());
            edit.putString("isMemory", YES);
            edit.commit();
        } else if (radioButton1.isChecked()) {
            if (sp == null) {
                sp = getSharedPreferences(FILE, MODE_PRIVATE);
            }
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("isMemory", NO);
            edit.commit();
        }
    }

}
