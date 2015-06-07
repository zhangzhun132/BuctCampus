package com.example.lenovo.buct_baogao.SaveFile;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/6/2.
 */
public class SharedPreferencesForLogin{
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Context context;

    public SharedPreferencesForLogin(Context c,String name){
        context = c;
        sp = context.getSharedPreferences(name, 0);
        editor = sp.edit();
    }
    //向SharedPreferences中注入数据
    public void putValue(String key, String value){
        editor = sp.edit();
        editor.putString(key, value);
        //这个提交很重要，别忘记，对xml修改一定别忘了commit()
        editor.commit();
    }
    //根据Key获取对应的Value
    public String getValue(String key){
        return sp.getString(key, null);
    }
    //清除SharedPreferences中的数据，比如点击“忘记密码”
    public void clear(){
        editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}