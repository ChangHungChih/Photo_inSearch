package idv.sean.photo_insearch.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Locale;

import idv.sean.photo_insearch.activity.ChatActivity;
import idv.sean.photo_insearch.model.ChatMessage;

public class ChatWebSocketClient extends WebSocketClient {
    private static final String TAG = "ChatWebSocketClient";
    private Context context;
    public static String userInChat;
    private Gson gson;

    public ChatWebSocketClient(URI serverUri, Context context) {
        // Draft_17是連接協議，就是標準的RFC 6455（JSR256）
        super(serverUri, new Draft_17());
        this.context = context;
        gson = new Gson();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        String text = String.format(Locale.getDefault(),
                "onOpen: Http status code = %d; status message = %s",
                handshakedata.getHttpStatus(), handshakedata.getHttpStatusMessage());

        Log.d(TAG, "onOpen: " + text);
    }

    // 訊息內容多(例如：圖片)，server端必須以byte型式傳送，此方法可以接收byte型式資料
    @Override
    public void onMessage(ByteBuffer bytes) {
        int length = bytes.array().length;
        String message = new String(bytes.array());
        Log.d(TAG, "onMessage(ByteBuffer): length = " + length);
        onMessage(message);
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, "onMessage" + message);
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
        // type: 訊息種類，有open(有user連線), close(有user離線), chat(其他user傳送來的聊天訊息)
        String type = jsonObject.get("type").getAsString();

        if (type.equals("chat")) {
            ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);

            // 開啟聊天視窗後會將聊天對象儲存在userInChat
            String text = "sender: " + chatMessage.getSender()
                    + "\n userInChat: " + userInChat;
            Log.d(TAG, text);

            /* 接收到聊天訊息但尚未開啟聊天畫面(ChatActivity)或是即便開了聊天對象不是訊息發送者，
               就顯示Notification告知user，user點擊後即開啟聊天畫面 */
            if (userInChat == null || !userInChat.equals(chatMessage.getSender())) {
                showNotification(chatMessage);
                return;
            }
        }
        sendMessageBroadCase(type, message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        String text = String.format(Locale.getDefault(),
                "code = %d, reason = %s, remote = %b",
                code, reason, remote);
        Log.d(TAG, "onClose: " + text);
    }

    @Override
    public void onError(Exception ex) {
        Log.d(TAG, "onError: Exception = " + ex.toString());
    }

    private void sendMessageBroadCase(String messageType, String message) {
        Intent intent = new Intent(messageType);
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void showNotification(ChatMessage chatMessage) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        // 將發送者、訊息種類與內容包在Notification內，方便之後開啟
        bundle.putString("userId", chatMessage.getSender());
        bundle.putString("messageType", chatMessage.getMessageType());
        bundle.putString("messageContent", chatMessage.getContent());
        intent.putExtras(bundle);
        // 必須設定成FLAG_UPDATE_CURRENT，否則會用舊的Bundle
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle("message from " + Utils.getUserNamesMap().get(chatMessage.getSender()))
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(soundUri)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(0, notification.build());

    }

}
