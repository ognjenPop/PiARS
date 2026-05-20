package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class InterestedEventsActivity extends AppCompatActivity {

    private TextView tvNoInterestedEvents;
    private ListView listInterestedEvents;
    private EventAdapter adapter;

    private DatabaseHelper databaseHelper;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_events);

        databaseHelper = new DatabaseHelper(this);

        username = getIntent().getStringExtra("username");

        tvNoInterestedEvents =
                findViewById(R.id.tvNoInterestedEvents);

        listInterestedEvents =
                findViewById(R.id.listInterestedEvents);

        ArrayList<Event> interestedEvents =
                databaseHelper.getInterestedEvents(username);

        adapter = new EventAdapter(
                this,
                interestedEvents
        );

        listInterestedEvents.setAdapter(adapter);

        if (interestedEvents.isEmpty()) {

            tvNoInterestedEvents.setVisibility(View.VISIBLE);
            listInterestedEvents.setVisibility(View.GONE);

        } else {

            tvNoInterestedEvents.setVisibility(View.GONE);
            listInterestedEvents.setVisibility(View.VISIBLE);
        }

        listInterestedEvents.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Event event =
                            (Event) adapter.getItem(position);

                    Intent intent =
                            new Intent(
                                    InterestedEventsActivity.this,
                                    EventDetailsActivity.class
                            );

                    intent.putExtra(
                            "eventName",
                            event.getName()
                    );

                    intent.putExtra(
                            "username",
                            username
                    );

                    startActivity(intent);
                });
    }
}