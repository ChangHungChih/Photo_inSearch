package idv.sean.photo_insearch.util;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;


public class TextTransferTask extends AsyncTask<Object, Void, Object> {


    //parameter[0] = action, parameter[1] = url
    @Override
    protected Object doInBackground(Object... objects) {
        int action = (int) objects[0];
        String url = (String) objects[1];
        JsonObject jsonObject = new JsonObject();

        switch (action) {
            //會員登入 Member Login
            //parameter[2] = mem_acc, parameter[3] = mem_pwd
            case Utils.LOGIN:
                String name = (String) objects[2];
                String password = (String) objects[3];
                jsonObject.addProperty("action", "android_login");
                jsonObject.addProperty("mem_acc", name);
                jsonObject.addProperty("mem_pwd", password);

                try {
                    //取得回傳Json字串  get Json String from web
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());

                    //轉成JsonObject
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    JsonObject loginResult = gson.fromJson(jsonIn, JsonObject.class);

                    return loginResult;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Utils.CASES_ALL_DOWNLOAD:
                jsonObject.addProperty("action","android_cases_all_download");

                try {
                    String jsonIn = Utils.getRemoteData(url,jsonObject.toString());

                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Utils.TESTUPLOAD:

                jsonObject.addProperty("action", "uploadText");
                jsonObject.addProperty("text", (String) objects[2]);

                try {
                    Utils.getRemoteData(url, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();

                }
                break;

        }


        return null;
    }
}
