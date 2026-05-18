package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    EditText etCurrentPassword;
    EditText etNewPassword;

    Button btnSavePassword;

    DatabaseHelper databaseHelper;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        databaseHelper = new DatabaseHelper(this);

        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);

        btnSavePassword = findViewById(R.id.btnSavePassword);

        username = getIntent().getStringExtra("username");

        btnSavePassword.setOnClickListener(view -> {
            String currentPassword = etCurrentPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();

            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(
                        PasswordActivity.this,
                        "Popunite sva polja",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            boolean currentPasswordCorrect =
                    databaseHelper.loginUser(username, currentPassword);

            if (!currentPasswordCorrect) {
                Toast.makeText(
                        PasswordActivity.this,
                        "Trenutna sifra nije ispravna",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            boolean updateSuccessful =
                    databaseHelper.updatePassword(username, newPassword);

            if (updateSuccessful) {
                Toast.makeText(
                        PasswordActivity.this,
                        "Uspesna promena sifre",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {
                Toast.makeText(
                        PasswordActivity.this,
                        "Greska prilikom promene sifre",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}