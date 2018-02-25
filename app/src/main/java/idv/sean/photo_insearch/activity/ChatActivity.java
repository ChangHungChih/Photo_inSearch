package idv.sean.photo_insearch.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import idv.sean.photo_insearch.R;
import idv.sean.photo_insearch.model.ChatMessage;
import idv.sean.photo_insearch.model.MemVO;
import idv.sean.photo_insearch.util.ChatWebSocketClient;
import idv.sean.photo_insearch.util.Utils;

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
    private String user, memName;
    private Uri contentUri, croppedImageUri;
    private MemVO memVO;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        handleViews();
        registerChatReceiver();

        //get member
        SharedPreferences preferences = getSharedPreferences("preference", MODE_PRIVATE);
        String memJson = preferences.getString("memVO", "");
        memVO = Utils.gson.fromJson(memJson, MemVO.class);
        this.memName = memVO.getMem_name();

        //get chatting user
        user = getIntent().getStringExtra("user");
        setTitle("User: " + user);

        // message不為null代表這頁是由notification開啟，而非由FriendsFragment轉來
        String messageType = getIntent().getStringExtra("messageType");
        if (messageType != null) {
            String messageContent = getIntent().getStringExtra("messageContent");
            switch (messageType) {
                case "text":
                    showMessage(user, messageContent, true);
                    break;
                case "image":
                    byte[] image = Base64.decode(messageContent, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    showImage(user, bitmap, true);
                    break;
                default:
                    break;
            }
        }
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
        Intent intent;
        switch (view.getId()) {
            case R.id.ivCamera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                // targeting Android 7.0 (API level 24) and higher,
                // storing images using a FileProvider.
                // passing a file:// URI across a package boundary causes a FileUriExposedException.
                contentUri = FileProvider.getUriForFile
                        (this, getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (isIntentAvailable(this, intent)) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Toast.makeText(this, "無拍照程式", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ivPhotoChoose:
                intent = new Intent(Intent.ACTION_PICK
                        , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_IMAGE);
                break;

            case R.id.ivSend:
                String message = etMessage.getText().toString();
                if (message.trim().isEmpty()) {
//                    Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
                    return;
                }
                showMessage(memName, message, false);
                etMessage.setText(null);
                //send message by json
                ChatMessage chatMessage = new ChatMessage
                        ("chat", memVO.getMem_id(), user, message, "text");
                String chatMessageJson = gson.toJson(chatMessage);
                Utils.chatWebSocketClient.send(chatMessageJson);
                Log.d(TAG, "output: " + chatMessageJson);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int newsize = 400;
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    Log.d(TAG, "REQ_TAKE_PICTURE: " + contentUri.toString());
                    crop(contentUri);
                    break;

                case REQ_PICK_IMAGE:
                    Uri uri = data.getData();
                    crop(uri);
                    break;

                case REQ_CROP_PICTURE:
                    Log.d(TAG, "REQ_CROP_PICTURE: " + croppedImageUri.toString());
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream
                                (getContentResolver().openInputStream(croppedImageUri));
                        Bitmap downsizedImage = Utils.downSize(bitmap, newsize);
                        showImage(memName, downsizedImage, false);
                        // 將欲傳送的對話訊息轉成JSON後送出
                        String message = Base64.encodeToString
                                (Utils.bitmapToPNG(downsizedImage), Base64.DEFAULT);
                        ChatMessage chatMessage =
                                new ChatMessage("chat", memName, user, message, "image");
                        String chatmessageJson = gson.toJson(chatMessage);
                        Utils.chatWebSocketClient.send(chatmessageJson);
                        Log.d(TAG, "output: " + chatmessageJson);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        croppedImageUri = Uri.fromFile(file);
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // the recipient of this Intent can read soruceImageUri's data
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // set image source Uri and type
            cropIntent.setDataAndType(sourceImageUri, "image/*");
            // send crop message
            cropIntent.putExtra("crop", "true");
            // aspect ratio of the cropped area, 0 means user define
            cropIntent.putExtra("aspectX", 0);   // this sets the max width
            cropIntent.putExtra("aspectY", 0);   // this sets the max height
            // output with and height, 0 keeps original size
            cropIntent.putExtra("outputX", 0);
            cropIntent.putExtra("outputY", 0);
            // whether keep original aspect ratio
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            // whether return data by the intent
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQ_CROP_PICTURE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast.makeText(this, "此裝置不支援裁切圖片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 將文字訊息呈現在畫面上
     *
     * @param sender  發訊者
     * @param message 訊息內容
     * @param left    true代表訊息要貼在左邊(發訊者為他人)，false代表右邊(發訊者為自己)
     */
    private void showMessage(String sender, String message, boolean left) {
        String text = sender + ": " + message;
        View view;
        TextView tvMsg;
        //自己和他人的文字位置不同
        if (left) {
            view = View.inflate(this, R.layout.message_left, null);
            tvMsg = view.findViewById(R.id.tv_msgLeft);
        } else {
            view = View.inflate(this, R.layout.message_right, null);
            tvMsg = view.findViewById(R.id.tv_msgRight);
        }

        tvMsg.setText(text);
        linearLayout.addView(view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void showImage(String sender, Bitmap bitmap, boolean left) {
        String text = sender + ": ";
        View view;
        TextView tvMsg;
        ImageView ivPicture;
        if (left) {
            view = View.inflate(this, R.layout.image_left, null);
            tvMsg = view.findViewById(R.id.tv_imgLeft);
            ivPicture = view.findViewById(R.id.iv_imgLeft);
        } else {
            view = View.inflate(this, R.layout.image_right, null);
            tvMsg = view.findViewById(R.id.tv_imgRight);
            ivPicture = view.findViewById(R.id.iv_imgRight);
        }
        tvMsg.setText(text);
        ivPicture.setImageBitmap(bitmap);
        linearLayout.addView(view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities
                (intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ChatWebSocketClient.userInChat = null;
    }

    // 接收到聊天訊息會在TextView呈現
    private class ChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
            String sender = chatMessage.getSender();
            String messageType = chatMessage.getMessageType();

            // 接收到聊天訊息，若發送者與目前聊天對象相同，就顯示訊息
            if (sender.equals(user)) {
                switch (messageType) {
                    case "text":
                        showMessage(sender, chatMessage.getContent(), true);
                        break;
                    case "image":
                        byte[] image = Base64.decode(chatMessage.getContent(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                        showImage(sender, bitmap, true);
                        break;
                    default:
                        break;
                }
            }
            Log.d(TAG, "received message: " + message);
        }
    }

}
