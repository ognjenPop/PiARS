package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                String username =
                        etLoginUsername.getText().toString();

                String password =
                        etLoginPassword.getText().toString();

                if (username.equals("admin")
                        && password.equals("admin")) {

                    Intent intent =
                            new Intent(
                                    MainActivity.this,
                                    EventsActivity.class
                            );

                    Bundle bundle =
                            new Bundle();

                    bundle.putString(
                            "username",
                            username
                    );

                    bundle.putString(
                            "email",
                            ""
                    );

                    intent.putExtras(bundle);

                    startActivity(intent);

                } else {

                    Toast.makeText(
                            MainActivity.this,
                            "Pogresan username ili password",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });

        btnDoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username =
                        etRegisterUsername.getText().toString();

                String email =
                        etRegisterEmail.getText().toString();

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                EventsActivity.class
                        );

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

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
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