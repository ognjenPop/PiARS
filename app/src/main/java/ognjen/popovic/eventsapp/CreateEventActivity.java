package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity {

    private EditText etCreateName;
    private EditText etCreateDescription;
    private EditText etCreateLocation;
    private EditText etCreateDateTime;
    private EditText etCreateCapacity;

    private Spinner spinnerCreateCategory;
    private CheckBox cbPromoted;
    private Button btnCreateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        etCreateName =
                findViewById(R.id.etCreateName);

        etCreateDescription =
                findViewById(R.id.etCreateDescription);

        etCreateLocation =
                findViewById(R.id.etCreateLocation);

        etCreateDateTime =
                findViewById(R.id.etCreateDateTime);

        etCreateCapacity =
                findViewById(R.id.etCreateCapacity);

        spinnerCreateCategory =
                findViewById(R.id.spinnerCreateCategory);

        cbPromoted =
                findViewById(R.id.cbPromoted);

        btnCreateEvent =
                findViewById(R.id.btnCreateEvent);

        cbPromoted.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {
                        etCreateCapacity.setVisibility(View.VISIBLE);
                    } else {
                        etCreateCapacity.setVisibility(View.GONE);
                    }
                });

        btnCreateEvent.setOnClickListener(v ->
                createEvent()
        );
    }

    private void createEvent() {

        String name =
                etCreateName.getText().toString().trim();

        String description =
                etCreateDescription.getText().toString().trim();

        String location =
                etCreateLocation.getText().toString().trim();

        String dateTime =
                etCreateDateTime.getText().toString().trim();

        String category =
                spinnerCreateCategory
                        .getSelectedItem()
                        .toString();

        if (name.isEmpty()
                || location.isEmpty()
                || dateTime.isEmpty()) {

            Toast.makeText(
                    this,
                    R.string.required_fields_error,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Event newEvent;

        if (cbPromoted.isChecked()) {

            String capacityText =
                    etCreateCapacity.getText()
                            .toString()
                            .trim();

            int capacity;

            try {

                capacity =
                        Integer.parseInt(capacityText);

            } catch (NumberFormatException e) {

                Toast.makeText(
                        this,
                        R.string.capacity_error,
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (capacity <= 0) {

                Toast.makeText(
                        this,
                        R.string.capacity_error,
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            newEvent =
                    EventFactory.createPromotedEvent(
                            name,
                            description,
                            location,
                            dateTime,
                            category,
                            R.drawable.exit,
                            capacity
                    );

        } else {

            newEvent =
                    EventFactory.createRegularEvent(
                            name,
                            description,
                            location,
                            dateTime,
                            category,
                            R.drawable.exit
                    );
        }

        AppData.allEvents.add(newEvent);

        Toast.makeText(
                this,
                R.string.event_created,
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}