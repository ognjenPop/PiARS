package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class InterestedEventsActivity extends AppCompatActivity {

    private TextView tvNoInterestedEvents;
    private ListView listInterestedEvents;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_events);

        tvNoInterestedEvents =
                findViewById(R.id.tvNoInterestedEvents);

        listInterestedEvents =
                findViewById(R.id.listInterestedEvents);

        adapter = new EventAdapter(
                this,
                AppData.interestedEvents
        );

        listInterestedEvents.setAdapter(adapter);

        if (AppData.interestedEvents.isEmpty()) {

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

                    startActivity(intent);
                });
    }
}