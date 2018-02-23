package idv.sean.photo_insearch.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public final static int LOGIN = 1;
    public final static int CASES_ALL_DOWNLOAD = 2;
    public final static int TESTUPLOAD = 0;
//    public final static String URL_ANDOROID_CONTROLLER =
//            "http://10.0.2.2:8081/PhotoinSearch_DBPractic//ForAndroidServlet";
    public final static String URL_ANDOROID_CONTROLLER =
        "http://10.120.26.10:8081/PhotoinSearch_DBPractic//ForAndroidServlet";

    public final static String WEBSOCKET_URI =
            "ws://10.120.26.10:8081/WebSocketChatAdvWeb/ChatWS/";

    public final static String TAG = "Utils";
    public final static String TAG_GET_REMOTE_DATA = "GetRemoteData";
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public static ChatWebSocketClient chatWebSocketClient;
    private static List<String> usersList = new ArrayList<>();


    public static byte[] bitmapToPNG(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 轉成PNG不會失真，所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /*
     * options.inJustDecodeBounds取得原始圖片寬度與高度資訊 (但不會在記憶體裡建立實體)
     * 當輸出寬與高超過自訂邊長邊寬最大值，scale設為2 (寬變1/2，高變1/2)
     */
    public static int getImageScale(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        int scale = 1;
        while (options.outWidth / scale >= width ||
                options.outHeight / scale >= height) {
            scale *= 2;
        }
        return scale;
    }

    public static boolean networkConnected(Context context) {
        ConnectivityManager conManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public static String getRemoteData(String url, String jsonOut) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

        //傳送請求到網頁 send request to web
        bw.write(jsonOut);
        Log.d(TAG_GET_REMOTE_DATA, "jsonOut: " + jsonOut);
        bw.close();

        //從網頁取得回應 get response from web
        int responseCode = connection.getResponseCode();
        StringBuilder jsonIn = new StringBuilder();
        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG_GET_REMOTE_DATA, "response code: " + responseCode);
        }

        connection.disconnect();
        Log.d(TAG_GET_REMOTE_DATA, "jsonIn: " + jsonIn);

        //回傳JSON字串  return Json String
        return jsonIn.toString();
    }

    public static void connectWebSocketServer(String userName, Context context){
        if(chatWebSocketClient == null){
            URI uri = null;
            try {
                uri =new URI(WEBSOCKET_URI+userName);
            } catch (URISyntaxException e) {
                Log.e(TAG, e.toString());
            }
            chatWebSocketClient = new ChatWebSocketClient(uri,context);
            chatWebSocketClient.connect();
        }
    }

    public static void disConnectWebSocketServer(){
        if(chatWebSocketClient != null){
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
        usersList.clear();
    }

    public static List<String> getUsersList(){
        return usersList;
    }

    public static void setUsersList(List<String> usersList){
        Utils.usersList = usersList;
    }

}
