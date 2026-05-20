package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class EventsFragment extends Fragment {

    private ListView eventsListView;

    private EventAdapter adapter;

    private Button btnAll;
    private Button btnParty;
    private Button btnFestival;
    private Button btnConcert;
    private Button btnTheater;
    private Button btnExhibition;
    private Button btnAddEvent;

    private DatabaseHelper databaseHelper;

    private String username;

    public EventsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view =
                inflater.inflate(
                        R.layout.fragment_events,
                        container,
                        false
                );

        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        databaseHelper =
                new DatabaseHelper(getContext());

        eventsListView =
                view.findViewById(R.id.eventsListView);

        btnAll =
                view.findViewById(R.id.btnAll);

        btnParty =
                view.findViewById(R.id.btnParty);

        btnFestival =
                view.findViewById(R.id.btnFestival);

        btnConcert =
                view.findViewById(R.id.btnConcert);

        btnTheater =
                view.findViewById(R.id.btnTheater);

        btnExhibition =
                view.findViewById(R.id.btnExhibition);

        btnAddEvent =
                view.findViewById(R.id.btnAddEvent);

        adapter = new EventAdapter(
                getContext(),
                databaseHelper.getAllEvents()
        );

        eventsListView.setAdapter(adapter);

        setActiveButton(btnAll);

        btnAll.setOnClickListener(v -> {

            adapter.setEvents(
                    databaseHelper.getAllEvents()
            );

            setActiveButton(btnAll);
        });

        btnParty.setOnClickListener(v -> {

            adapter.setEvents(
                    databaseHelper.getEventsByCategory(
                            "Party"
                    )
            );

            setActiveButton(btnParty);
        });

        btnFestival.setOnClickListener(v -> {

            adapter.setEvents(
                    databaseHelper.getEventsByCategory(
                            "Festival"
                    )
            );

            setActiveButton(btnFestival);
        });

        btnConcert.setOnClickListener(v -> {

            adapter.setEvents(
                    databaseHelper.getEventsByCategory(
                            "Concert"
                    )
            );

            setActiveButton(btnConcert);
        });

        btnTheater.setOnClickListener(v -> {

            adapter.setEvents(
                    databaseHelper.getEventsByCategory(
                            "Stand-Up & Theater"
                    )
            );

            setActiveButton(btnTheater);
        });

        btnExhibition.setOnClickListener(v -> {

            adapter.setEvents(
                    databaseHelper.getEventsByCategory(
                            "Exhibition"
                    )
            );

            setActiveButton(btnExhibition);
        });

        btnAddEvent.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            getActivity(),
                            CreateEventActivity.class
                    );

            startActivity(intent);
        });

        eventsListView.setOnItemClickListener(
                (parent, view1, position, id) -> {

                    Event event =
                            (Event) adapter.getItem(position);

                    Intent intent =
                            new Intent(
                                    getActivity(),
                                    EventDetailsActivity.class
                            );

                    intent.putExtra(
                            "eventName",
                            event.getName()
                    );

                    intent.putExtra(
                            "username",
                            username
                    );

                    startActivity(intent);
                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null && databaseHelper != null) {

            adapter.setEvents(
                    databaseHelper.getAllEvents()
            );

            setActiveButton(btnAll);
        }
    }

    private void setActiveButton(Button activeButton) {

        btnAll.setBackgroundResource(R.color.disabled);
        btnParty.setBackgroundResource(R.color.disabled);
        btnFestival.setBackgroundResource(R.color.disabled);
        btnConcert.setBackgroundResource(R.color.disabled);
        btnTheater.setBackgroundResource(R.color.disabled);
        btnExhibition.setBackgroundResource(R.color.disabled);

        activeButton.setBackgroundResource(R.color.purple_500);
    }
}