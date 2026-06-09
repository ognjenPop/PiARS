package ognjen.popovic.eventsapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExclusiveEventService extends Service {

    private static final String CHANNEL_ID = "exclusive_event_channel";
    private static final int NOTIFICATION_ID = 1001;

    private DatabaseHelper databaseHelper;
    private ServerHelper serverHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        databaseHelper =
                new DatabaseHelper(this);

        serverHelper =
                new ServerHelper(this);

        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createExclusiveEvent();

        return START_NOT_STICKY;
    }

    private void createExclusiveEvent() {

        String eventName =
                "Exclusive Event " + System.currentTimeMillis();

        String description =
                "Exclusive event available for a limited time";

        String location =
                "Secret location";

        Calendar calendar =
                Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(
                        "dd.MM.yyyy HH:mm",
                        Locale.getDefault()
                );

        String eventTime =
                simpleDateFormat.format(calendar.getTime());

        String category =
                "Concert";

        boolean promoted =
                true;

        int capacity =
                1;

        JSONObject requestBody =
                new JSONObject();

        try {
            requestBody.put("name", eventName);
            requestBody.put("description", description);
            requestBody.put("location", location);
            requestBody.put("eventTime", eventTime);
            requestBody.put("category", category);
            requestBody.put("promoted", promoted);
            requestBody.put("capacity", capacity);

        } catch (JSONException e) {
            stopSelf();
            return;
        }

        serverHelper.postRequest(
                "/events",
                requestBody,
                new ServerHelper.ServerResponseListener() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        try {
                            Event event =
                                    createEventFromJson(response);

                            databaseHelper.insertOrUpdateServerEvent(event);

                            showExclusiveEventNotification(event);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        stopSelf();
                    }

                    @Override
                    public void onError(String error) {
                        stopSelf();
                    }
                }
        );
    }

    private Event createEventFromJson(JSONObject eventObject) throws JSONException {

        String serverEventId =
                eventObject.getString("_id");

        String name =
                eventObject.getString("name");

        String description =
                eventObject.optString("description", "");

        String location =
                eventObject.getString("location");

        String eventTime =
                eventObject.getString("eventTime");

        String category =
                eventObject.getString("category");

        boolean promoted =
                eventObject.optBoolean("promoted", false);

        int capacity =
                eventObject.optInt("capacity", 0);

        int numberOfAttendees =
                eventObject.optInt("numberOfAttendees", 0);

        double avgRating =
                eventObject.optDouble("avgRating", 0);

        int numberOfRatings =
                eventObject.optInt("numberOfRatings", 0);

        int imageResId =
                getImageResIdByCategory(category);

        return new Event(
                serverEventId,
                name,
                description,
                location,
                eventTime,
                category,
                imageResId,
                promoted,
                capacity,
                numberOfAttendees,
                avgRating,
                numberOfRatings
        );
    }

    private int getImageResIdByCategory(String category) {
        if (category.equals("Party")) {
            return R.drawable.party1;
        }

        if (category.equals("Festival")) {
            return R.drawable.festival1;
        }

        if (category.equals("Concert")) {
            return R.drawable.concert1;
        }

        if (category.equals("Stand-Up & Theater")) {
            return R.drawable.theater1;
        }

        if (category.equals("Exhibition")) {
            return R.drawable.exhibition1;
        }

        return R.drawable.exit;
    }

    private void showExclusiveEventNotification(Event event) {

        Intent intent =
                new Intent(
                        this,
                        EventDetailsActivity.class
                );

        intent.putExtra(
                "eventName",
                event.getName()
        );

        intent.putExtra(
                "serverEventId",
                event.getServerEventId()
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(event.getName())
                        .setContentText("Imate 5 minuta da se prijavite na ekskluzivni dogadjaj")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(
                NOTIFICATION_ID,
                builder.build()
        );
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name =
                    "Exclusive Events";

            String description =
                    "Notifications for exclusive events";

            int importance =
                    NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            name,
                            importance
                    );

            channel.setDescription(description);

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}