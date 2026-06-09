package ognjen.popovic.eventsapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final int NOTIFICATION_ID_OPEN_WINDOW = 1001;
    private static final int NOTIFICATION_ID_CLOSED_WINDOW = 1002;

    private static final String ACTION_WINDOW_CLOSED =
            "ognjen.popovic.eventsapp.ACTION_WINDOW_CLOSED";

    public static final String PREFS_EXCLUSIVE_EVENTS =
            "exclusive_events_prefs";

    public static final String EXCLUSIVE_EVENT_END_TIME_PREFIX =
            "exclusive_event_end_time_";

    private static final long EXCLUSIVE_WINDOW_DURATION =
            5 * 60 * 1000;

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

        if (intent != null
                && ACTION_WINDOW_CLOSED.equals(intent.getAction())) {

            String eventName =
                    intent.getStringExtra("eventName");

            showWindowClosedNotification(eventName);

            stopSelf();

            return START_NOT_STICKY;
        }

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

                            long endTime =
                                    System.currentTimeMillis()
                                            + EXCLUSIVE_WINDOW_DURATION;

                            saveExclusiveEventEndTime(
                                    event.getServerEventId(),
                                    endTime
                            );

                            showExclusiveEventNotification(
                                    event,
                                    endTime
                            );

                            scheduleWindowClosedNotification(
                                    event,
                                    endTime
                            );

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

    private void saveExclusiveEventEndTime(String serverEventId,
                                           long endTime) {

        if (serverEventId == null || serverEventId.isEmpty()) {
            return;
        }

        SharedPreferences sharedPreferences =
                getSharedPreferences(
                        PREFS_EXCLUSIVE_EVENTS,
                        MODE_PRIVATE
                );

        SharedPreferences.Editor editor =
                sharedPreferences.edit();

        editor.putLong(
                EXCLUSIVE_EVENT_END_TIME_PREFIX + serverEventId,
                endTime
        );

        editor.apply();
    }

    private void scheduleWindowClosedNotification(Event event,
                                                  long endTime) {

        Intent intent =
                new Intent(
                        this,
                        ExclusiveEventService.class
                );

        intent.setAction(ACTION_WINDOW_CLOSED);

        intent.putExtra(
                "eventName",
                event.getName()
        );

        PendingIntent pendingIntent =
                PendingIntent.getService(
                        this,
                        event.getServerEventId().hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    endTime,
                    pendingIntent
            );
        }
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

    private void showExclusiveEventNotification(Event event,
                                                long endTime) {

        SharedPreferences userPreferences =
                getSharedPreferences(
                        "logged_user_prefs",
                        MODE_PRIVATE
                );

        String username =
                userPreferences.getString(
                        "username",
                        ""
                );

        String serverUserId =
                userPreferences.getString(
                        "serverUserId",
                        ""
                );

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

        intent.putExtra(
                "username",
                username
        );

        intent.putExtra(
                "serverUserId",
                serverUserId
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
                        .setContentIntent(pendingIntent)
                        .setWhen(endTime)
                        .setUsesChronometer(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(
                NOTIFICATION_ID_OPEN_WINDOW,
                builder.build()
        );
    }

    private void showWindowClosedNotification(String eventName) {

        if (eventName == null || eventName.isEmpty()) {
            eventName = "Ekskluzivni dogadjaj";
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Prozor za prijavu je zatvoren")
                        .setContentText(
                                "Prijava za " + eventName +
                                        " je zatvorena. Sledeca prilika je za 24h."
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(
                NOTIFICATION_ID_CLOSED_WINDOW,
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