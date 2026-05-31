package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class RatingActivity extends AppCompatActivity {

    private TextView tvRatingEventName;

    private Button btnStar1;
    private Button btnStar2;
    private Button btnStar3;
    private Button btnStar4;
    private Button btnStar5;

    private Button btnConfirmRating;

    private int selectedRating = 0;

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
        setContentView(R.layout.activity_rating);

        databaseHelper =
                new DatabaseHelper(this);

        serverHelper =
                new ServerHelper(this);

        tvRatingEventName =
                findViewById(R.id.tvRatingEventName);

        btnStar1 =
                findViewById(R.id.btnStar1);

        btnStar2 =
                findViewById(R.id.btnStar2);

        btnStar3 =
                findViewById(R.id.btnStar3);

        btnStar4 =
                findViewById(R.id.btnStar4);

        btnStar5 =
                findViewById(R.id.btnStar5);

        btnConfirmRating =
                findViewById(R.id.btnConfirmRating);

        eventName =
                getIntent().getStringExtra("eventName");

        username =
                getIntent().getStringExtra("username");

        serverUserId =
                getIntent().getStringExtra("serverUserId");

        serverEventId =
                getIntent().getStringExtra("serverEventId");

        currentEvent =
                databaseHelper.findEventByName(eventName);

        if (currentEvent != null) {
            if (serverEventId == null || serverEventId.isEmpty()) {
                serverEventId = currentEvent.getServerEventId();
            }
        }

        tvRatingEventName.setText(eventName);

        btnStar1.setOnClickListener(v ->
                selectRating(1)
        );

        btnStar2.setOnClickListener(v ->
                selectRating(2)
        );

        btnStar3.setOnClickListener(v ->
                selectRating(3)
        );

        btnStar4.setOnClickListener(v ->
                selectRating(4)
        );

        btnStar5.setOnClickListener(v ->
                selectRating(5)
        );

        btnConfirmRating.setOnClickListener(v ->
                sendRatingToServer()
        );

        updateStars();
    }

    private void sendRatingToServer() {

        if (selectedRating == 0) {

            Toast.makeText(
                    RatingActivity.this,
                    R.string.select_rating_error,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (serverUserId == null || serverUserId.isEmpty()
                || serverEventId == null || serverEventId.isEmpty()) {

            Toast.makeText(
                    RatingActivity.this,
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
            requestBody.put("rating", selectedRating);

        } catch (JSONException e) {

            Toast.makeText(
                    RatingActivity.this,
                    "Greska prilikom pripreme podataka",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        serverHelper.postRequest("/ratings", requestBody, new ServerHelper.ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

                try {
                    databaseHelper.addRating(
                            username,
                            eventName,
                            selectedRating
                    );

                    Event updatedEvent =
                            createEventFromJson(response);

                    databaseHelper.insertOrUpdateServerEvent(updatedEvent);

                    Toast.makeText(
                            RatingActivity.this,
                            R.string.rating_saved,
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();

                } catch (JSONException e) {

                    Toast.makeText(
                            RatingActivity.this,
                            "Greska u odgovoru servera",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onError(String error) {

                Toast.makeText(
                        RatingActivity.this,
                        R.string.rating_error,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
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

    private void selectRating(int rating) {

        selectedRating = rating;

        updateStars();
    }

    private void updateStars() {

        updateStarButton(btnStar1, 1);

        updateStarButton(btnStar2, 2);

        updateStarButton(btnStar3, 3);

        updateStarButton(btnStar4, 4);

        updateStarButton(btnStar5, 5);
    }

    private void updateStarButton(Button button,
                                  int value) {

        if (value <= selectedRating) {

            button.setTextColor(
                    ContextCompat.getColor(
                            this,
                            R.color.purple_500
                    )
            );

        } else {

            button.setTextColor(
                    ContextCompat.getColor(
                            this,
                            R.color.gray_text
                    )
            );
        }
    }
}