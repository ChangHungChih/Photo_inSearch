package idv.sean.photo_insearch.util;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;


public class TextUploadTask extends AsyncTask<Object, Void, Object> {


    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected Object doInBackground(Object... objects) {
        int action = (int)objects[0];
        String url = (String)objects[1];
        JsonObject jsonObject = new JsonObject();

        switch (action){
            //會員登入 Member Login
            //要傳4個參數 URL, Action, mem_acc, mem_pwd
            case Util.LOGIN:
                String name = (String)objects[2];
                String password = (String)objects[3];
                jsonObject.addProperty("action","android_login");
                jsonObject.addProperty("mem_acc",name);
                jsonObject.addProperty("mem_pwd",password);

                try {
                    //取得回傳Json字串  get Json String from web
                    String jsonIn = Util.getRemoteData(url,jsonObject.toString());

                    //轉成JsonObject
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    JsonObject loginResult = gson.fromJson(jsonIn,JsonObject.class);

                    return loginResult;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case Util.TESTUPLOAD:

                jsonObject.addProperty("action","uploadText");
                jsonObject.addProperty("text",(String) objects[2]);

                try {
                    Util.getRemoteData(url,jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();

                }
                break;

        }



        return null;
    }
}
