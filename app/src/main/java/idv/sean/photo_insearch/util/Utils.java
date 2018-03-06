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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import idv.sean.photo_insearch.model.MemVO;

public class Utils {
    public final static String TAG = "Utils";
    public final static String TAG_GET_REMOTE_DATA = "GetRemoteData";
    public final static int LOGIN = 1;
    public final static int CASES_ALL_DOWNLOAD = 2;
    public final static int SEND_MAIL = 3;
    public final static int REFRESH_MEMBER_DATA = 4;
    public final static int INSERT_POINT = 5;
    public static final int GET_POSTED_CASES = 6;
    public static final int GET_PROCEEDING_CASES = 7;
    public static final int GET_FINISHED_CASES = 8;
    public static final int GET_INVITED_CASES = 9;
    public static final int GET_CLOSED_CASES = 10;
    public static final int CANCEL_CASE = 11;
    public static final int FINISH_CASE = 12;
    public static final int ACCEPT_CASE = 13;
    public static final int REJECT_CASE = 14;
    public static final int GET_APPLICANTS = 15;
    public static final int CHOOSE_MEMBER_TO_CASE = 16;
    public static final int SET_COMMENT = 17;
    public static final int APPLY_CASE = 18;

//    public final static String URL_ANDOROID_CONTROLLER =
//            "http://10.0.2.2:8081/PhotoinSearch_DBPractic/ForAndroidServlet";
    public final static String URL_ANDOROID_CONTROLLER =
            "http://10.120.26.10:8081/PhotoinSearch_DBPractic/ForAndroidServlet";

    public final static String WEBSOCKET_URI =
            "ws://10.120.26.10:8081/PhotoinSearch_DBPractic/AndroidChatWS/";

    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public static ChatWebSocketClient chatWebSocketClient;
    private static Map<String, String> userNamesMap = new HashMap<>();
    private static List<String> userIdsList = new ArrayList<>();
    private static List<MemVO> memList;
    private static MemVO memVO;

    public static Map<String, String> photoType = new HashMap<String, String>() {
        {
            put("風景", "01");
            put("人像", "02");
            put("紀實", "03");
            put("商品", "04");
            put("生態", "05");
            put("概念", "06");
            put("藝術", "07");
            put("新聞", "08");
            put("婚紗", "09");
            put("時裝", "10");
            put("美食", "11");
            put("飛機", "12");
            put("鐵道", "13");
            put("建築", "14");
            put("廢墟", "15");
            put("黑白", "16");
            put("微距", "17");
            put("家庭", "18");
            put("天文", "19");
            put("空中", "20");
            put("水中", "21");
            put("動物", "22");
            put("其他", "23");
        }
    };

    public static byte[] bitmapToPNG(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 轉成PNG不會失真，所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
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

    public static Bitmap downSize(Bitmap srcBitmap, int newSize) {
        if (newSize <= 50)
            newSize = 128; //if too small, set newSize to 128

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        Log.d(TAG, "source image size = " + srcWidth + " x " + srcHeight);
        int longer = Math.max(srcWidth, srcHeight);

        if (longer > newSize) {
            double scale = longer / (double) newSize;
            int dsWidth = (int) (srcWidth / scale);
            int dsHeight = (int) (srcHeight / scale);
            srcBitmap = Bitmap.createScaledBitmap(srcBitmap, dsWidth, dsHeight, false);
            System.gc();
            Log.d(TAG, "\nscale = " + scale +
                    "\nscaled image size = " + srcBitmap.getWidth() + " x " + srcBitmap.getHeight());
        }
        return srcBitmap;
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

    public static void connectWebSocketServer(String userId, Context context) {
        URI uri = null;
        try {
            uri = new URI(WEBSOCKET_URI + userId);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        chatWebSocketClient = new ChatWebSocketClient(uri, context);
        chatWebSocketClient.connect();
    }

    public static void disConnectWebSocketServer() {
        if (chatWebSocketClient != null) {
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
        userNamesMap.clear();
    }

    public static Map<String, String> getUserNamesMap() {
        return userNamesMap;
    }

    public static void setUserNamesMap(Map<String, String> userNamesMap) {
        Utils.userNamesMap = userNamesMap;
        setUserIdsList(new ArrayList<String>(userNamesMap.keySet()));
    }

    public static List<String> getUserIdsList() {
        return userIdsList;
    }

    public static void setUserIdsList(List<String> list) {
        Utils.userIdsList = list;
    }

    public static void setMemVO(MemVO memVO) {
        Utils.memVO = memVO;
    }

    public static MemVO getMemVO() {
        return memVO;
    }

    public static List<MemVO> getMemList() {
        return memList;
    }

    public static void setMemList(List<MemVO> memList) {
        Utils.memList = memList;
    }
}
