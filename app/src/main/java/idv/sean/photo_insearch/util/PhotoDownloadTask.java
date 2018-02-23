package idv.sean.photo_insearch.util;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;

import idv.sean.photo_insearch.model.PhotoVO;


public class PhotoDownloadTask extends AsyncTask<Object, Integer, PhotoVO> {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected PhotoVO doInBackground(Object... objects) {
        String url = (String) objects[0];
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getPhoto");
        PhotoVO photoVO = null;
        try {
            String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
            photoVO = gson.fromJson(jsonIn, PhotoVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return photoVO;
    }


}