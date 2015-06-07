package com.example.lenovo.buct_baogao.JsonTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/5/28.
 */
public class JsonTools {
    public JsonTools(){

    }
    public  static int jsonData(String jsonString){
        int data=0;
        try {
            JSONObject jsonObject=new JSONObject(jsonString);
            data=jsonObject.getInt("alevel");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return data;
    }
}
