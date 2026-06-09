package ognjen.popovic.eventsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EventDetailsActivity extends AppCompatActivity {

    private ImageView imgDetailsEvent;

    private TextView tvDetailsName;
    private TextView tvDetailsCategory;
    private TextView tvDetailsLocation;
    private TextView tvDetailsDateTime;
    private TextView tvDetailsDescription;
    private TextView tvDetailsFreePlaces;
    private TextView tvDetailsRating;

    private Button btnDetailsInterested;
    private Button btnDetailsAttending;

    private DatabaseHelper databaseHelper;
    private ServerHelper serverHelper;

    private String username;
    private String serverUserId;
    private String serverEventId;
    private String eventName;

    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        databaseHelper = new DatabaseHelper(this);
        serverHelper = new ServerHelper(this);

        imgDetailsEvent = findViewById(R.id.imgDetailsEvent);

        tvDetailsName = findViewById(R.id.tvDetailsName);
        tvDetailsCategory = findViewById(R.id.tvDetailsCategory);
        tvDetailsLocation = findViewById(R.id.tvDetailsLocation);
        tvDetailsDateTime = findViewById(R.id.tvDetailsDateTime);
        tvDetailsDescription = findViewById(R.id.tvDetailsDescription);
        tvDetailsFreePlaces = findViewById(R.id.tvDetailsFreePlaces);
        tvDetailsRating = findViewById(R.id.tvDetailsRating);

        btnDetailsInterested = findViewById(R.id.btnDetailsInterested);
        btnDetailsAttending = findViewById(R.id.btnDetailsAttending);

        eventName = getIntent().getStringExtra("eventName");
        username = getIntent().getStringExtra("username");
        serverUserId = getIntent().getStringExtra("serverUserId");
        serverEventId = getIntent().getStringExtra("serverEventId");

        currentEvent = databaseHelper.findEventByName(eventName);

        if (currentEvent != null) {

            if (serverEventId == null || serverEventId.isEmpty()) {
                serverEventId = currentEvent.getServerEventId();
            }

            showEventData(currentEvent);
            checkExclusiveEventWindow();
        }

        btnDetailsInterested.setOnClickListener(v ->
                sendAttendanceToServer(DatabaseHelper.STATUS_INTERESTED)
        );

        btnDetailsAttending.setOnClickListener(v -> {

            if (isExclusiveEventExpired()) {

                Toast.makeText(
                        EventDetailsActivity.this,
                        "Vreme za prijavu na ekskluzivni dogadjaj je isteklo",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            sendAttendanceToServer(DatabaseHelper.STATUS_ATTENDING);
        });
    }

    private void showEventData(Event event) {

        imgDetailsEvent.setImageResource(event.getImageResId());

        tvDetailsName.setText(event.getName());
        tvDetailsCategory.setText(event.getCategory());
        tvDetailsLocation.setText(event.getLocation());
        tvDetailsDateTime.setText(event.getDateTime());
        tvDetailsDescription.setText(event.getDescription());

        if (event.isPromoted()) {

            int freePlaces =
                    event.getCapacity() - event.getAttendingCount();

            tvDetailsFreePlaces.setVisibility(View.VISIBLE);

            tvDetailsFreePlaces.setText(
                    getString(
                            R.string.free_places_format,
                            freePlaces,
                            event.getCapacity()
                    )
            );

        } else {

            tvDetailsFreePlaces.setVisibility(View.GONE);
        }

        if (event.getRatingCount() == 0) {

            tvDetailsRating.setText(R.string.no_ratings);

        } else {

            tvDetailsRating.setText(
                    getString(
                            R.string.average_rating_format,
                            event.getAverageRating(),
                            event.getRatingCount()
                    )
            );
        }
    }

    private void checkExclusiveEventWindow() {

        if (isExclusiveEventExpired()) {

            btnDetailsAttending.setEnabled(false);
            btnDetailsAttending.setText("ATTENDING EXPIRED");

        } else {

            btnDetailsAttending.setEnabled(true);
        }
    }

    private boolean isExclusiveEventExpired() {

        if (serverEventId == null || serverEventId.isEmpty()) {
            return false;
        }

        SharedPreferences sharedPreferences =
                getSharedPreferences(
                        ExclusiveEventService.PREFS_EXCLUSIVE_EVENTS,
                        MODE_PRIVATE
                );

        long endTime =
                sharedPreferences.getLong(
                        ExclusiveEventService.EXCLUSIVE_EVENT_END_TIME_PREFIX + serverEventId,
                        -1
                );

        if (endTime == -1) {
            return false;
        }

        return System.currentTimeMillis() > endTime;
    }

    private void sendAttendanceToServer(String commitment) {

        if (serverUserId == null || serverUserId.isEmpty()
                || serverEventId == null || serverEventId.isEmpty()) {

            Toast.makeText(
                    EventDetailsActivity.this,
                    "Nedostaje server ID korisnika ili dogadjaja",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        JSONObject requestBody =
                new JSONObject();

        try {
            requestBody.put("userId", serverUserId);
            requestBody.put("eventId", serverEventId);
            requestBody.put("commitment", commitment);

        } catch (JSONException e) {

            Toast.makeText(
                    EventDetailsActivity.this,
                    "Greska prilikom pripreme podataka",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        serverHelper.postRequest("/attendance", requestBody, new ServerHelper.ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

                boolean localSuccess;

                if (DatabaseHelper.STATUS_INTERESTED.equals(commitment)) {

                    localSuccess =
                            databaseHelper.addInterested(username, eventName);

                    if (localSuccess) {

                        Toast.makeText(
                                EventDetailsActivity.this,
                                R.string.added_to_interested,
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                EventDetailsActivity.this,
                                R.string.already_added_or_error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } else {

                    localSuccess =
                            databaseHelper.addAttending(username, eventName);

                    if (localSuccess) {

                        Toast.makeText(
                                EventDetailsActivity.this,
                                R.string.added_to_attending,
                                Toast.LENGTH_SHORT
                        ).show();

                        refreshEventData();

                    } else {

                        Toast.makeText(
                                EventDetailsActivity.this,
                                R.string.attending_error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }

            @Override
            public void onError(String error) {

                Toast.makeText(
                        EventDetailsActivity.this,
                        "Server nije dostupan ili nema slobodnih mesta",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void refreshEventData() {

        Event event =
                databaseHelper.findEventByName(eventName);

        if (event == null) {
            return;
        }

        currentEvent = event;

        if (event.isPromoted()) {

            int freePlaces =
                    event.getCapacity() - event.getAttendingCount();

            tvDetailsFreePlaces.setVisibility(View.VISIBLE);

            tvDetailsFreePlaces.setText(
                    getString(
                            R.string.free_places_format,
                            freePlaces,
                            event.getCapacity()
                    )
            );

        } else {

            tvDetailsFreePlaces.setVisibility(View.GONE);
        }

        checkExclusiveEventWindow();
    }
}