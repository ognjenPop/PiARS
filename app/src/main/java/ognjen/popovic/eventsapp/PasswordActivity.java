package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    Button btnSavePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        btnSavePassword = findViewById(R.id.btnSavePassword);

        btnSavePassword.setOnClickListener(view -> {
            Toast.makeText(PasswordActivity.this, "Uspešna promena šifre", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}