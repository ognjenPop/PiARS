package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        imgDetailsEvent =
                findViewById(R.id.imgDetailsEvent);

        tvDetailsName =
                findViewById(R.id.tvDetailsName);

        tvDetailsCategory =
                findViewById(R.id.tvDetailsCategory);

        tvDetailsLocation =
                findViewById(R.id.tvDetailsLocation);

        tvDetailsDateTime =
                findViewById(R.id.tvDetailsDateTime);

        tvDetailsDescription =
                findViewById(R.id.tvDetailsDescription);

        tvDetailsFreePlaces =
                findViewById(R.id.tvDetailsFreePlaces);

        tvDetailsRating =
                findViewById(R.id.tvDetailsRating);

        btnDetailsInterested =
                findViewById(R.id.btnDetailsInterested);

        btnDetailsAttending =
                findViewById(R.id.btnDetailsAttending);

        String eventName =
                getIntent().getStringExtra("eventName");

        Event event =
                AppData.findByName(eventName);

        if (event != null) {

            imgDetailsEvent.setImageResource(
                    event.getImageResId()
            );

            tvDetailsName.setText(
                    event.getName()
            );

            tvDetailsCategory.setText(
                    event.getCategory()
            );

            tvDetailsLocation.setText(
                    event.getLocation()
            );

            tvDetailsDateTime.setText(
                    event.getDateTime()
            );

            tvDetailsDescription.setText(
                    event.getDescription()
            );

            if (event.isPromoted()) {

                int freePlaces =
                        event.getCapacity()
                                - event.getAttendingCount();

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

                tvDetailsRating.setText(
                        R.string.no_ratings
                );

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

        btnDetailsInterested.setOnClickListener(v ->
                Toast.makeText(
                        EventDetailsActivity.this,
                        R.string.added_to_interested,
                        Toast.LENGTH_SHORT
                ).show()
        );

        btnDetailsAttending.setOnClickListener(v ->
                Toast.makeText(
                        EventDetailsActivity.this,
                        R.string.added_to_attending,
                        Toast.LENGTH_SHORT
                ).show()
        );
    }
}