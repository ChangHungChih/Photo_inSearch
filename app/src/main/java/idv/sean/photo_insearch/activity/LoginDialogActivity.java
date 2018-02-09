package idv.sean.photo_insearch.activity;

import android.content.Intent;
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
    private EditText etAccount, etPassword;
    private Button btnLogin, btnCancel;
    private TextView tvMessage;
    private MemVO memVO = null;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mem_login);
        findViews();
    }

    public void findViews(){
        etAccount = (EditText) findViewById(R.id.etAcc);
        etPassword = (EditText)findViewById(R.id.etPwd);
        btnLogin = (Button) findViewById(R.id.btnLogInSubmit);
        btnCancel = (Button) findViewById(R.id.btnLogInCancel);
        tvMessage = (TextView) findViewById(R.id.tvMsg);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if(account.length() == 0 || password.length() == 0){
                    showMessage("帳號或密碼錯誤");
                    return;
                }

                //帳密比對成功 進行登入  account and password is correct
                if(isUserValid(account,password)){
                    SharedPreferences pref = getSharedPreferences("preference",MODE_PRIVATE);
                    String memJson = gson.toJson(memVO);

                    //將會員資料以Json字串 存入preference
                    //save member data to preference by using json string
                    pref.edit()
                            .putBoolean("login",true)
                            .putString("memVO",memJson)
                            .apply();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("memVO",memVO);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();

                }else{
                    showMessage("帳號或密碼錯誤");
                }
            }
        });

    }

    //設登入過的帳密回輸入方塊 set logged account and password back
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences("preference",MODE_PRIVATE);
        String memJson = pref.getString("memVO","");
        if(memJson.length() > 0){
            memVO = gson.fromJson(memJson,MemVO.class);
            String acc = memVO.getMem_acc();
            String pwd = memVO.getMem_pwd();
            etAccount.setText(acc);
            etPassword.setText(pwd);
        }

    }

    private void showMessage(String mesStr){
        tvMessage.setText(mesStr);
    }

    private boolean isUserValid(String name, String pwd){
    // 連線至server端檢查帳號密碼是否正確

        TextUploadTask textUploadTask = new TextUploadTask();
        JsonObject jsonIn;

        try {
            jsonIn = (JsonObject) textUploadTask
                    .execute(Util.LOGIN,Util.URL_ANDOROID_CONTROLLER,name,pwd).get();

            if(jsonIn == null) {
                return false;
            }
            memVO = gson.fromJson(jsonIn.toString(),MemVO.class);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return (memVO != null);
    }

}
