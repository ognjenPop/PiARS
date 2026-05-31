package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordActivity extends AppCompatActivity {

    EditText etCurrentPassword;
    EditText etNewPassword;

    Button btnSavePassword;

    DatabaseHelper databaseHelper;
    ServerHelper serverHelper;

    String username;
    String serverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        databaseHelper = new DatabaseHelper(this);
        serverHelper = new ServerHelper(this);

        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);

        btnSavePassword = findViewById(R.id.btnSavePassword);

        username = getIntent().getStringExtra("username");
        serverUserId = getIntent().getStringExtra("serverUserId");

        btnSavePassword.setOnClickListener(view -> {
            changePasswordOnServer();
        });
    }

    private void changePasswordOnServer() {
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

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("username", username);
            requestBody.put("oldPassword", currentPassword);
            requestBody.put("newPassword", newPassword);

        } catch (JSONException e) {
            Toast.makeText(
                    PasswordActivity.this,
                    "Greska prilikom pripreme podataka",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        serverHelper.putRequest("/password", requestBody, new ServerHelper.ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {

                databaseHelper.updatePassword(
                        username,
                        newPassword
                );

                Toast.makeText(
                        PasswordActivity.this,
                        "Uspesna promena sifre",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(
                        PasswordActivity.this,
                        "Trenutna sifra nije ispravna ili server nije dostupan",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}