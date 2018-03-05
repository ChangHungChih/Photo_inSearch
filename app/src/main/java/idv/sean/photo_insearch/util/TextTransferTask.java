package idv.sean.photo_insearch.util;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;


public class TextTransferTask extends AsyncTask<Object, Void, Object> {
    private static final String TAG = "TextTransferTask";

    //parameter[0] = action, parameter[1] = url
    @Override
    protected Object doInBackground(Object... objects) {
        int action = (int) objects[0];
        String url = (String) objects[1];
        JsonObject jsonObject = new JsonObject();
        String memId = null;

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
                    JsonObject loginResult = Utils.gson.fromJson(jsonIn, JsonObject.class);

                    return loginResult;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.CASES_ALL_DOWNLOAD: //get all cases
                jsonObject.addProperty("action", "cases_all_download");
                try {
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.SEND_MAIL:  //send email
                //param[2] address mail to,
                //param[3] mail subject,
                //param[4] mail content
                String to = (String) objects[2];
                String subject = (String) objects[3];
                String messageText = (String) objects[4];
                jsonObject.addProperty("action", "sendMail");
                jsonObject.addProperty("to", to);
                jsonObject.addProperty("subject", subject);
                jsonObject.addProperty("messageText", messageText);
                try {
                    int result = Integer.parseInt(Utils.getRemoteData(url, jsonObject.toString()));
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NumberFormatException nfe) {
                    return 2;
                }
                break;

            case Utils.REFRESH_MEMBER_DATA:     //param[2] = memberId
                memId = (String) objects[2];
                jsonObject.addProperty("action", "refresh");
                jsonObject.addProperty("memId", memId);
                try {
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.INSERT_POINT:        //param[2] = point to add
                int pointAdd = (int) objects[2];
                memId = Utils.getMemVO().getMem_id();
                jsonObject.addProperty("action", "addPoint");
                jsonObject.addProperty("point", pointAdd);
                jsonObject.addProperty("memId", memId);
                try {
                    Utils.getRemoteData(url, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.GET_POSTED_CASES:
                memId = Utils.getMemVO().getMem_id();
                jsonObject.addProperty("action", "get_posted_cases");
                jsonObject.addProperty("memId", memId);
                try {
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                    Log.d(TAG, jsonIn);
                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.GET_PROCEEDING_CASES:
                memId = Utils.getMemVO().getMem_id();
                jsonObject.addProperty("action", "get_proceeding_cases");
                jsonObject.addProperty("memId", memId);
                try {
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                    Log.d(TAG, jsonIn);
                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.GET_FINISHED_CASES:
                memId = Utils.getMemVO().getMem_id();
                jsonObject.addProperty("action", "get_finished_cases");
                jsonObject.addProperty("memId", memId);
                try {
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                    Log.d(TAG, jsonIn);
                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.GET_INVITED_CASES:
                memId = Utils.getMemVO().getMem_id();
                jsonObject.addProperty("action", "get_invited_cases");
                jsonObject.addProperty("memId", memId);
                try {
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                    Log.d(TAG, jsonIn);
                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.GET_CLOSED_CASES:
                memId = Utils.getMemVO().getMem_id();
                jsonObject.addProperty("action", "get_closed_cases");
                jsonObject.addProperty("memId", memId);
                try {
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                    Log.d(TAG, jsonIn);
                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.CANCEL_CASE:
                String cancelCaseId = (String) objects[2];
                String caseState = (String) objects[3];
                jsonObject.addProperty("action", "cancelCase");
                jsonObject.addProperty("caseId", cancelCaseId);
                jsonObject.addProperty("caseState", caseState);
                try {
                    Utils.getRemoteData(url, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.FINISH_CASE:
                String finishCaseId = (String) objects[2];
                jsonObject.addProperty("action", "finishCase");
                jsonObject.addProperty("caseId", finishCaseId);
                try {
                    Utils.getRemoteData(url, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.ACCEPT_CASE:
                String acceptCaseId = (String) objects[2];
                jsonObject.addProperty("action", "acceptCase");
                jsonObject.addProperty("caseId", acceptCaseId);
                try {
                    Utils.getRemoteData(url, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.REJECT_CASE:
                String rejectCaseId = (String) objects[2];
                jsonObject.addProperty("action", "rejectCase");
                jsonObject.addProperty("caseId", rejectCaseId);
                try {
                    Utils.getRemoteData(url, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.GET_APPLICANTS:
                String applicantCaseId = (String) objects[2];
                jsonObject.addProperty("action", "getApplicants");
                jsonObject.addProperty("caseId", applicantCaseId);
                try {
                    String jsonIn = Utils.getRemoteData(url, jsonObject.toString());
                    return jsonIn;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Utils.CHOOSE_MEMBER_TO_CASE:
                String changeCaseId = (String) objects[2];
                String chosenMemId = (String) objects[3];
                jsonObject.addProperty("action", "chooseMemToCase");
                jsonObject.addProperty("caseId", changeCaseId);
                jsonObject.addProperty("memId", chosenMemId);
                try {
                    Utils.getRemoteData(url, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        return null;
    }
}
