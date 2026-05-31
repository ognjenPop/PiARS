package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView tvProfileUsername;
    TextView tvProfileEmail;

    Button btnPassword;
    Button btnEndSession;

    private String username;
    private String email;
    private String serverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileUsername = findViewById(R.id.tvProfileUsername);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        btnPassword = findViewById(R.id.btnPassword);
        btnEndSession = findViewById(R.id.btnEndSession);

        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        serverUserId = getIntent().getStringExtra("serverUserId");

        tvProfileUsername.setText(username);
        tvProfileEmail.setText(email);

        btnEndSession.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnPassword.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, PasswordActivity.class);

            intent.putExtra("username", username);
            intent.putExtra("serverUserId", serverUserId);

            startActivity(intent);
        });
    }
}