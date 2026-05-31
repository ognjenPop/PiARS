package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    LinearLayout startLayout;
    LinearLayout loginLayout;
    LinearLayout registerLayout;

    Button btnLogin;
    Button btnRegister;
    Button btnDoLogin;
    Button btnDoRegister;

    EditText etLoginUsername;
    EditText etLoginPassword;
    EditText etRegisterUsername;
    EditText etRegisterEmail;
    EditText etRegisterPassword;

    CheckBox cbRegisterAdmin;

    DatabaseHelper databaseHelper;
    ServerHelper serverHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        serverHelper = new ServerHelper(this);

        startLayout = findViewById(R.id.startLayout);
        loginLayout = findViewById(R.id.loginLayout);
        registerLayout = findViewById(R.id.registerLayout);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnDoLogin = findViewById(R.id.btnDoLogin);
        btnDoRegister = findViewById(R.id.btnDoRegister);

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);

        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);

        cbRegisterAdmin = findViewById(R.id.cbRegisterAdmin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                registerLayout.setVisibility(View.GONE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);
                loginLayout.setVisibility(View.GONE);
            }
        });

        btnDoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserOnServer();
            }
        });

        btnDoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUserOnServer();
            }
        });
    }

    private void loginUserOnServer() {
        String username = etLoginUsername.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                    MainActivity.this,
                    "Unesite username i password",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            Toast.makeText(
                    MainActivity.this,
                    "Greska prilikom pripreme podataka",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        serverHelper.postRequest("/login", requestBody, new ServerHelper.ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String serverUserId = response.getString("_id");
                    String username = response.getString("username");
                    String email = response.getString("email");
                    boolean isAdmin = response.optBoolean("isAdmin", false);

                    if (!databaseHelper.checkUsernameExists(username)) {
                        databaseHelper.insertUser(
                                username,
                                email,
                                etLoginPassword.getText().toString(),
                                isAdmin
                        );
                    }

                    openEventsActivity(username, email, serverUserId, isAdmin);

                } catch (JSONException e) {
                    Toast.makeText(
                            MainActivity.this,
                            "Greska u odgovoru servera",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(
                        MainActivity.this,
                        "Pogresan username ili password",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void registerUserOnServer() {
        String username = etRegisterUsername.getText().toString();
        String email = etRegisterEmail.getText().toString();
        String password = etRegisterPassword.getText().toString();

        boolean isAdmin = cbRegisterAdmin.isChecked();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                    MainActivity.this,
                    "Popunite sva polja",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("username", username);
            requestBody.put("email", email);
            requestBody.put("password", password);
            requestBody.put("isAdmin", isAdmin);
        } catch (JSONException e) {
            Toast.makeText(
                    MainActivity.this,
                    "Greska prilikom pripreme podataka",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        serverHelper.postRequest("/users", requestBody, new ServerHelper.ServerResponseListener() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String serverUserId = response.getString("_id");
                    String username = response.getString("username");
                    String email = response.getString("email");
                    boolean isAdmin = response.optBoolean("isAdmin", false);

                    if (!databaseHelper.checkUsernameExists(username)
                            && !databaseHelper.checkEmailExists(email)) {

                        databaseHelper.insertUser(
                                username,
                                email,
                                etRegisterPassword.getText().toString(),
                                isAdmin
                        );
                    }

                    Toast.makeText(
                            MainActivity.this,
                            "Registracija uspesna",
                            Toast.LENGTH_SHORT
                    ).show();

                    openEventsActivity(username, email, serverUserId, isAdmin);

                } catch (JSONException e) {
                    Toast.makeText(
                            MainActivity.this,
                            "Greska u odgovoru servera",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(
                        MainActivity.this,
                        "Korisnik vec postoji ili server nije dostupan",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void openEventsActivity(String username,
                                    String email,
                                    String serverUserId,
                                    boolean isAdmin) {

        Intent intent = new Intent(MainActivity.this, EventsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("email", email);
        bundle.putString("serverUserId", serverUserId);
        bundle.putBoolean("isAdmin", isAdmin);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (loginLayout.getVisibility() == View.VISIBLE
                || registerLayout.getVisibility() == View.VISIBLE) {

            startLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            registerLayout.setVisibility(View.GONE);

        } else {
            super.onBackPressed();
        }
    }
}