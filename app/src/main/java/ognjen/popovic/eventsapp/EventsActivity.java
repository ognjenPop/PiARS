package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class EventsActivity extends AppCompatActivity {

    TextView tvUsername;
    Button btnEvents;
    Button btnMyEvents;

    private String username;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        tvUsername = findViewById(R.id.tvUsername);
        btnEvents = findViewById(R.id.btnEvents);
        btnMyEvents = findViewById(R.id.btnMyEvents);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            username = bundle.getString("username");
            email = bundle.getString("email");

            tvUsername.setText(username);
        }

        loadEventsFragment();

        btnEvents.setOnClickListener(view ->
                loadEventsFragment()
        );

        btnMyEvents.setOnClickListener(view ->
                loadMyEventsFragment()
        );
    }

    private void loadEventsFragment() {
        EventsFragment fragment =
                new EventsFragment();

        Bundle bundle =
                new Bundle();

        bundle.putString(
                "username",
                username
        );

        fragment.setArguments(bundle);

        loadFragment(fragment);
    }

    private void loadMyEventsFragment() {
        MyEventsFragment fragment =
                new MyEventsFragment();

        Bundle bundle =
                new Bundle();

        bundle.putString(
                "username",
                username
        );

        bundle.putString(
                "email",
                email
        );

        fragment.setArguments(bundle);

        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}