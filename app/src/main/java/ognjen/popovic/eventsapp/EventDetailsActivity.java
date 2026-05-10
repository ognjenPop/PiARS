package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView tvEventDetailsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        tvEventDetailsTitle =
                findViewById(R.id.tvEventDetailsTitle);

        String eventName =
                getIntent().getStringExtra("eventName");

        tvEventDetailsTitle.setText(eventName);
    }
}