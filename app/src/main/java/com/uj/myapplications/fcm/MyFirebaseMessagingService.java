package com.uj.myapplications.fcm;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uj.myapplications.activities.SplashActivity;
import com.uj.myapplications.utility.Config;
import com.uj.myapplications.utility.NotificationUtil;
import org.json.JSONObject;
import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static String D_TOKEN = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Timber.d(TAG, "Message data payload: " + remoteMessage.getData());
            // sendNotification(remoteMessage.getData().get("data"));
           /* if (Config.getUser) {
                Timber.d(TAG, "Message data payload: " + remoteMessage.getMenuDataPojo());
                sendNotification(remoteMessage.getMenuDataPojo().get("data"));
            }*/
        }

    }

    private void sendNotification(String messageBody) {

        try {
            JSONObject jsonObject = new JSONObject(messageBody);
            //  String eventId = jsonObject.optString("EventId");
            if (jsonObject.has("nType"))
                Timber.e(jsonObject.toString());
            String type = jsonObject.optString("nType");
            NotificationUtil.generateNotification(getApplicationContext(), jsonObject, SplashActivity.class, type);
           /* switch (type) {
                case "0":
                    //Normal One
                    // NotificationUtil.generateNotification(getApplicationContext(), jsonObject, Splash_Screen.class, type);
                    break;
                case "1":
                    //Event Detail type
                    //  NotificationUtil.generateNotification(getApplicationContext(), jsonObject, EventDetailActivity.class, type);
                    break;
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Timber.e("NEW_TOKEN", s);
        Log.e("NEW_TOKEN", s);
        D_TOKEN = s;
    }
}