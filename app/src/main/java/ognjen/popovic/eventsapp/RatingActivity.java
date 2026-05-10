package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class RatingActivity extends AppCompatActivity {

    private TextView tvRatingTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        tvRatingTitle =
                findViewById(R.id.tvRatingTitle);

        String eventName =
                getIntent().getStringExtra("eventName");

        tvRatingTitle.setText(eventName);
    }
}