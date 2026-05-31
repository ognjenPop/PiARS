package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateEventActivity extends AppCompatActivity {

    private EditText etCreateName;
    private EditText etCreateDescription;
    private EditText etCreateLocation;
    private EditText etCreateDateTime;
    private EditText etCreateCapacity;

    private Spinner spinnerCreateCategory;
    private CheckBox cbPromoted;
    private Button btnCreateEvent;

    private DatabaseHelper databaseHelper;
    private ServerHelper serverHelper;

    private String serverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        databaseHelper = new DatabaseHelper(this);
        serverHelper = new ServerHelper(this);

        serverUserId =
                getIntent().getStringExtra("serverUserId");

        etCreateName =
                findViewById(R.id.etCreateName);

        etCreateDescription =
                findViewById(R.id.etCreateDescription);

        etCreateLocation =
                findViewById(R.id.etCreateLocation);

        etCreateDateTime =
                findViewById(R.id.etCreateDateTime);

        etCreateCapacity =
                findViewById(R.id.etCreateCapacity);

        spinnerCreateCategory =
                findViewById(R.id.spinnerCreateCategory);

        cbPromoted =
                findViewById(R.id.cbPromoted);

        btnCreateEvent =
                findViewById(R.id.btnCreateEvent);

        loadCategoriesFromDatabase();

        cbPromoted.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {
                        etCreateCapacity.setVisibility(View.VISIBLE);
                    } else {
                        etCreateCapacity.setVisibility(View.GONE);
                    }
                });

        btnCreateEvent.setOnClickListener(v ->
                createEventOnServer()
        );
    }

    private void loadCategoriesFromDatabase() {
        ArrayList<String> categories =
                databaseHelper.getAllCategories();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categories
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerCreateCategory.setAdapter(adapter);
    }

    private void createEventOnServer() {

        String name =
                etCreateName.getText()
                        .toString()
                        .trim();

        String description =
                etCreateDescription.getText()
                        .toString()
                        .trim();

        String location =
                etCreateLocation.getText()
                        .toString()
                        .trim();

        String dateTime =
                etCreateDateTime.getText()
                        .toString()
                        .trim();

        if (spinnerCreateCategory.getSelectedItem() == null) {

            Toast.makeText(
                    this,
                    R.string.required_fields_error,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String category =
                spinnerCreateCategory
                        .getSelectedItem()
                        .toString();

        if (name.isEmpty()
                || location.isEmpty()
                || dateTime.isEmpty()) {

            Toast.makeText(
                    this,
                    R.string.required_fields_error,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean promoted =
                cbPromoted.isChecked();

        int capacity =
                0;

        if (promoted) {

            String capacityText =
                    etCreateCapacity.getText()
                            .toString()
                            .trim();

            try {

                capacity =
                        Integer.parseInt(capacityText);

            } catch (NumberFormatException e) {

                Toast.makeText(
                        this,
                        R.string.capacity_error,
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (capacity <= 0) {

                Toast.makeText(
                        this,
                        R.string.capacity_error,
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }
        }

        JSONObject requestBody =
                new JSONObject();

        try {
            requestBody.put("name", name);
            requestBody.put("description", description);
            requestBody.put("location", location);
            requestBody.put("eventTime", dateTime);
            requestBody.put("category", category);
            requestBody.put("promoted", promoted);
            requestBody.put("capacity", capacity);

        } catch (JSONException e) {

            Toast.makeText(
                    this,
                    "Greska prilikom pripreme podataka",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        serverHelper.postRequest("/events", requestBody, new ServerHelper.ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

                try {
                    Event event =
                            createEventFromJson(response);

                    boolean insertSuccessful =
                            databaseHelper.insertOrUpdateServerEvent(event);

                    if (insertSuccessful) {

                        Toast.makeText(
                                CreateEventActivity.this,
                                R.string.event_created,
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    } else {

                        Toast.makeText(
                                CreateEventActivity.this,
                                R.string.event_create_error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                } catch (JSONException e) {

                    Toast.makeText(
                            CreateEventActivity.this,
                            "Greska u odgovoru servera",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onError(String error) {

                Toast.makeText(
                        CreateEventActivity.this,
                        "Server nije dostupan ili dogadjaj nije sacuvan",
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
}