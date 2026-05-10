package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RatingActivity extends AppCompatActivity {

    private TextView tvRatingEventName;

    private Button btnStar1;
    private Button btnStar2;
    private Button btnStar3;
    private Button btnStar4;
    private Button btnStar5;

    private Button btnConfirmRating;

    private int selectedRating = 0;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        tvRatingEventName =
                findViewById(R.id.tvRatingEventName);

        btnStar1 =
                findViewById(R.id.btnStar1);

        btnStar2 =
                findViewById(R.id.btnStar2);

        btnStar3 =
                findViewById(R.id.btnStar3);

        btnStar4 =
                findViewById(R.id.btnStar4);

        btnStar5 =
                findViewById(R.id.btnStar5);

        btnConfirmRating =
                findViewById(R.id.btnConfirmRating);

        String eventName =
                getIntent().getStringExtra("eventName");

        tvRatingEventName.setText(eventName);

        event =
                AppData.findByName(eventName);

        btnStar1.setOnClickListener(v -> selectRating(1));

        btnStar2.setOnClickListener(v -> selectRating(2));

        btnStar3.setOnClickListener(v -> selectRating(3));

        btnStar4.setOnClickListener(v -> selectRating(4));

        btnStar5.setOnClickListener(v -> selectRating(5));

        btnConfirmRating.setOnClickListener(v -> {

            if (selectedRating == 0) {

                Toast.makeText(
                        RatingActivity.this,
                        R.string.select_rating_error,
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                if (event != null) {
                    event.addRating(selectedRating);
                }

                Toast.makeText(
                        RatingActivity.this,
                        R.string.rating_saved,
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }
        });

        updateStars();
    }

    private void selectRating(int rating) {

        selectedRating = rating;

        updateStars();
    }

    private void updateStars() {

        updateStarButton(btnStar1, 1);

        updateStarButton(btnStar2, 2);

        updateStarButton(btnStar3, 3);

        updateStarButton(btnStar4, 4);

        updateStarButton(btnStar5, 5);
    }

    private void updateStarButton(Button button,
                                  int value) {

        if (value <= selectedRating) {

            button.setTextColor(
                    ContextCompat.getColor(
                            this,
                            R.color.purple_500
                    )
            );

        } else {

            button.setTextColor(
                    ContextCompat.getColor(
                            this,
                            R.color.gray_text
                    )
            );
        }
    }
}