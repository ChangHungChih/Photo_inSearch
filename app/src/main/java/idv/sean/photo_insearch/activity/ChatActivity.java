package idv.sean.photo_insearch.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.util.ChatWebSocketClient;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ChatActivity";
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private static final int REQ_PERMISSIONS_STOREGE = 101;
    private LocalBroadcastManager broadcastManager;
    private EditText etMessage;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private ImageView ivCamera, ivPhotoChoose, ivSend;
    private String user;
    private Uri contentUri, croppedImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        handleViews();
        registerChatReceiver();

        //get chatting user
        user = getIntent().getStringExtra("user");

        /****
         *
         *
         * 從這裡開始寫..................Q o Q
         *
         *
         * **********/




    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission_Storage();
        // 設定目前聊天對象
        ChatWebSocketClient.userInChat = user;
    }

    private void handleViews() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        scrollView = (ScrollView) findViewById(R.id.scrollView_message);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout_message);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivPhotoChoose = (ImageView) findViewById(R.id.ivPhotoChoose);
        ivSend = (ImageView) findViewById(R.id.ivSend);

        ivCamera.setOnClickListener(this);
        ivPhotoChoose.setOnClickListener(this);
        ivSend.setOnClickListener(this);
    }

    private void registerChatReceiver() {
        IntentFilter intentFilter = new IntentFilter("chat");
        ChatReceiver chatReceiver = new ChatReceiver();
        broadcastManager.registerReceiver(chatReceiver, intentFilter);
    }

    private void requestPermission_Storage() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        int result = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this
                    , permissions, REQ_PERMISSIONS_STOREGE);
        }
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQ_PERMISSIONS_STOREGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ivCamera.setEnabled(true);
                    ivPhotoChoose.setEnabled(true);
                } else {
                    ivCamera.setEnabled(false);
                    ivPhotoChoose.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {

    }

    private class ChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


        }
    }
}
