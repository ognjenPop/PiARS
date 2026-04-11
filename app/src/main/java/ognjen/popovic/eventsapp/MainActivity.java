package ognjen.popovic.eventsapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;
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

        // Klik na LOGIN dugme (prikaz forme)
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                registerLayout.setVisibility(View.GONE);
            }
        });

        // Klik na REGISTER dugme (prikaz forme)
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);
                loginLayout.setVisibility(View.GONE);
            }
        });

        // LOGIN logika
        btnDoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();

                if(username.equals("admin") && password.equals("admin")) {
                    Toast.makeText(MainActivity.this, "Login uspešan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Pogrešan username ili password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}