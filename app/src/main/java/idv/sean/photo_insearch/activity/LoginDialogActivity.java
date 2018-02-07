package idv.sean.photo_insearch.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.TextUploadTask;
import idv.sean.photo_insearch.util.Util;
import idv.sean.photo_insearch.vo.MemVO;

public class LoginDialogActivity extends AppCompatActivity {
    private EditText account, pwd;
    private Button  login, cancel;
    private TextView message;
    private MemVO memVO = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mem_login);
        findViews();
    }

    public void findViews(){
        account = (EditText) findViewById(R.id.etAcc);
        pwd = (EditText)findViewById(R.id.etPwd);
        login = (Button) findViewById(R.id.btnLogInSubmit);
        cancel = (Button) findViewById(R.id.btnLogInCancel);
        message = (TextView) findViewById(R.id.tvMsg);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = account.getText().toString().trim();
                String password = pwd.getText().toString().trim();
                if(user.length() == 0 || password.length() == 0){
                    showMessage("帳號或密碼錯誤");
                    return;
                }

                if(isUserValid(user,password)){
                    SharedPreferences pref = getSharedPreferences("preference",MODE_PRIVATE);
                    String memJson = new Gson().toJson(memVO);
                    pref.edit()
                            .putBoolean("login",true)
                            .putString("user",user)
                            .putString("password",password)
                            .putString("memVO",memJson)
                            .apply();
                    setResult(RESULT_OK);
                    finish();
                }else{
                    showMessage("帳號或密碼錯誤");
                }
            }
        });


    }

    //檢查是否已經登入 check if login or not
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences("preference",MODE_PRIVATE);
        boolean login = pref.getBoolean("login",false);

        if(login){
            String name = pref.getString("user","");
            String pwd = pref.getString("password","");
            if(isUserValid(name,pwd)){
                setResult(RESULT_OK);
                finish();
            }else {
                showMessage("帳號或密碼錯誤");
            }

        }
    }

    private void showMessage(String mesStr){
        message.setText(mesStr);
    }

    private boolean isUserValid(String name, String pwd){
    // 連線至server端檢查帳號密碼是否正確
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        TextUploadTask textUploadTask = new TextUploadTask();
        JsonObject jsonIn;

        try {
            jsonIn = (JsonObject) textUploadTask
                    .execute(Util.LOGIN,Util.URL_ANDOROID_CONTROLLER,name,pwd).get();

            if(jsonIn == null) {
                return false;
            }

            memVO = gson.fromJson(jsonIn.toString(),MemVO.class);
            Log.wtf("memVO",memVO.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return (memVO != null);
    }


}
