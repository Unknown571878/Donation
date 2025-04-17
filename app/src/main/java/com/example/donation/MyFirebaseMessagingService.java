package com.example.donation;

/*// MyFirebaseMessagingService.java

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "MyNotificationChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // 푸시 알람 수신 시 처리할 코드
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // 푸시 알람 데이터 확인
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            // 여기서 특정 조건을 확인하고, 조건이 충족되면 알람을 표시
            if (isConditionMet(remoteMessage.getData().get("condition_param"))) {
                // 알람을 표시하는 함수 호출
                showNotification(remoteMessage.getData().get("message"));
            }
        }

        // 푸시 알람 알림 메시지 확인
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void showNotification(String message) {
        // NotificationManager 및 NotificationChannel 설정
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // 알람에 대한 설정(타이틀, 내용, 아이콘 등) 추가
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notifications_24)
                .setContentTitle("My App Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // NotificationManagerCompat을 사용하여 알람 표시
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private boolean isConditionMet(String conditionParam) {
        // 외부에서 특정 조건 확인
        // 조건이 충족되면 true, 그렇지 않으면 false를 반환
        // 예시: conditionParam이 "true"일 때 조건 충족
        return "true".equals(conditionParam);
    }
}

<service android:name=".MyFirebaseMessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>
*/