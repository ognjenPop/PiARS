package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ServerHelper serverHelper;

    private String username;
    private String serverUserId;
    private boolean isAdmin;

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
            serverUserId = getArguments().getString("serverUserId");
            isAdmin = getArguments().getBoolean("isAdmin", false);
        }

        databaseHelper =
                new DatabaseHelper(getContext());

        serverHelper =
                new ServerHelper(getContext());

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

        if (isAdmin) {
            btnAddEvent.setVisibility(View.VISIBLE);
        } else {
            btnAddEvent.setVisibility(View.GONE);
        }

        adapter = new EventAdapter(
                getContext(),
                databaseHelper.getAllEvents()
        );

        eventsListView.setAdapter(adapter);

        setActiveButton(btnAll);

        loadEventsFromServer();

        btnAll.setOnClickListener(v -> {

            loadEventsFromServer();

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

            intent.putExtra(
                    "serverUserId",
                    serverUserId
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
                            "serverEventId",
                            event.getServerEventId()
                    );

                    intent.putExtra(
                            "username",
                            username
                    );

                    intent.putExtra(
                            "serverUserId",
                            serverUserId
                    );

                    startActivity(intent);
                });

        return view;
    }

    private void loadEventsFromServer() {

        serverHelper.getArrayRequest("/events", new ServerHelper.ServerArrayResponseListener() {
            @Override
            public void onSuccess(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject eventObject =
                                response.getJSONObject(i);

                        Event event =
                                createEventFromJson(eventObject);

                        databaseHelper.insertOrUpdateServerEvent(event);
                    }

                    adapter.setEvents(
                            databaseHelper.getAllEvents()
                    );

                } catch (JSONException e) {

                    Toast.makeText(
                            getContext(),
                            "Greska prilikom citanja dogadjaja sa servera",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onError(String error) {

                Toast.makeText(
                        getContext(),
                        "Server nije dostupan, prikazujem lokalne dogadjaje",
                        Toast.LENGTH_SHORT
                ).show();

                adapter.setEvents(
                        databaseHelper.getAllEvents()
                );
            }
        });
    }

    private Event createEventFromJson(JSONObject eventObject) throws JSONException {

        String serverEventId =
                eventObject.getString("_id");

        String name =
                eventObject.getString("name");

        String description =
                eventObject.optString("description", "");

        String location =
                eventObject.getString("location");

        String eventTime =
                eventObject.getString("eventTime");

        String category =
                eventObject.getString("category");

        boolean promoted =
                eventObject.optBoolean("promoted", false);

        int capacity =
                eventObject.optInt("capacity", 0);

        int numberOfAttendees =
                eventObject.optInt("numberOfAttendees", 0);

        double avgRating =
                eventObject.optDouble("avgRating", 0);

        int numberOfRatings =
                eventObject.optInt("numberOfRatings", 0);

        int imageResId =
                getImageResIdByCategory(category);

        return new Event(
                serverEventId,
                name,
                description,
                location,
                eventTime,
                category,
                imageResId,
                promoted,
                capacity,
                numberOfAttendees,
                avgRating,
                numberOfRatings
        );
    }

    private int getImageResIdByCategory(String category) {
        if (category.equals("Party")) {
            return R.drawable.party1;
        }

        if (category.equals("Festival")) {
            return R.drawable.festival1;
        }

        if (category.equals("Concert")) {
            return R.drawable.concert1;
        }

        if (category.equals("Stand-Up & Theater")) {
            return R.drawable.theater1;
        }

        if (category.equals("Exhibition")) {
            return R.drawable.exhibition1;
        }

        return R.drawable.exit;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null && databaseHelper != null) {

            loadEventsFromServer();

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