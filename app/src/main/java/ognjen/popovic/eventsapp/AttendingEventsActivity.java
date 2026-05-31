package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendingEventsActivity extends AppCompatActivity {

    private TextView tvNoAttendingEvents;
    private TextView tvUpcomingHeader;
    private TextView tvPastHeader;

    private LinearLayout upcomingEventsLayout;
    private LinearLayout pastEventsLayout;

    private DatabaseHelper databaseHelper;

    private String username;
    private String serverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        databaseHelper =
                new DatabaseHelper(this);

        username =
                getIntent().getStringExtra("username");

        serverUserId =
                getIntent().getStringExtra("serverUserId");

        tvNoAttendingEvents =
                findViewById(R.id.tvNoAttendingEvents);

        tvUpcomingHeader =
                findViewById(R.id.tvUpcomingHeader);

        tvPastHeader =
                findViewById(R.id.tvPastHeader);

        upcomingEventsLayout =
                findViewById(R.id.upcomingEventsLayout);

        pastEventsLayout =
                findViewById(R.id.pastEventsLayout);

        ArrayList<Event> attendingEvents =
                databaseHelper.getAttendingEvents(username);

        if (attendingEvents.isEmpty()) {

            tvNoAttendingEvents.setVisibility(View.VISIBLE);
            tvUpcomingHeader.setVisibility(View.GONE);
            tvPastHeader.setVisibility(View.GONE);
            upcomingEventsLayout.setVisibility(View.GONE);
            pastEventsLayout.setVisibility(View.GONE);

            return;
        }

        tvNoAttendingEvents.setVisibility(View.GONE);
        upcomingEventsLayout.setVisibility(View.VISIBLE);
        pastEventsLayout.setVisibility(View.VISIBLE);

        boolean hasUpcoming = false;
        boolean hasPast = false;

        for (Event event : attendingEvents) {

            if (event.isPast()) {

                hasPast = true;
                addPastEvent(event);

            } else {

                hasUpcoming = true;
                addUpcomingEvent(event);
            }
        }

        tvUpcomingHeader.setVisibility(
                hasUpcoming ? View.VISIBLE : View.GONE
        );

        tvPastHeader.setVisibility(
                hasPast ? View.VISIBLE : View.GONE
        );

        upcomingEventsLayout.setVisibility(
                hasUpcoming ? View.VISIBLE : View.GONE
        );

        pastEventsLayout.setVisibility(
                hasPast ? View.VISIBLE : View.GONE
        );
    }

    private void addUpcomingEvent(Event event) {

        LinearLayout itemLayout =
                createTextEventLayout(event);

        itemLayout.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AttendingEventsActivity.this,
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

            startActivity(intent);
        });

        upcomingEventsLayout.addView(itemLayout);
    }

    private void addPastEvent(Event event) {

        LinearLayout rowLayout =
                new LinearLayout(this);

        rowLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setPadding(8, 8, 8, 8);

        LinearLayout textLayout =
                createTextEventLayout(event);

        textLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                )
        );

        textLayout.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AttendingEventsActivity.this,
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

            startActivity(intent);
        });

        Button btnRate =
                new Button(this);

        btnRate.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        btnRate.setText(R.string.rate_upper);
        btnRate.setBackgroundResource(R.color.purple_500);
        btnRate.setTextColor(getResources().getColor(R.color.white));

        btnRate.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            AttendingEventsActivity.this,
                            RatingActivity.class
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

            startActivity(intent);
        });

        rowLayout.addView(textLayout);
        rowLayout.addView(btnRate);

        pastEventsLayout.addView(rowLayout);
    }

    private LinearLayout createTextEventLayout(Event event) {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(8, 8, 8, 8);

        TextView tvName =
                new TextView(this);

        tvName.setText(event.getName());
        tvName.setTextColor(getResources().getColor(R.color.gray_text));
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvDate =
                new TextView(this);

        tvDate.setText(event.getDateTime());
        tvDate.setTextColor(getResources().getColor(R.color.gray_text));
        tvDate.setTextSize(14);

        TextView tvLocation =
                new TextView(this);

        tvLocation.setText(event.getLocation());
        tvLocation.setTextColor(getResources().getColor(R.color.gray_text));
        tvLocation.setTextSize(14);

        layout.addView(tvName);
        layout.addView(tvDate);
        layout.addView(tvLocation);

        return layout;
    }
}