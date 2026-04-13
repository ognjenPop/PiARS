package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView tvProfileUsername;
    TextView tvProfileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileUsername = findViewById(R.id.tvProfileUsername);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        tvProfileUsername.setText("Username: " + username);
        tvProfileEmail.setText("Email: " + email);
    }
}