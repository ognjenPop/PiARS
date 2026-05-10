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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        tvUsername = findViewById(R.id.tvUsername);
        btnEvents = findViewById(R.id.btnEvents);
        btnMyEvents = findViewById(R.id.btnMyEvents);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String username = bundle.getString("username");
            tvUsername.setText(username);
        }

        // Podrazumevano prikazi EventsFragment
        loadFragment(new EventsFragment());

        btnEvents.setOnClickListener(view -> loadFragment(new EventsFragment()));

        btnMyEvents.setOnClickListener(view -> loadFragment(new MyEventsFragment()));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
