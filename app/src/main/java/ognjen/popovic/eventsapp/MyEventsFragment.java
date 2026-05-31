package ognjen.popovic.eventsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyEventsFragment extends Fragment {

    Button btnInterestedEvents;
    Button btnAttendingEvents;
    Button btnMyProfile;

    private String username;
    private String email;
    private String serverUserId;

    public MyEventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_events, container, false);

        if (getArguments() != null) {
            username = getArguments().getString("username");
            email = getArguments().getString("email");
            serverUserId = getArguments().getString("serverUserId");
        }

        btnInterestedEvents = view.findViewById(R.id.btnInterestedEvents);
        btnAttendingEvents = view.findViewById(R.id.btnAttendingEvents);
        btnMyProfile = view.findViewById(R.id.btnMyProfile);

        btnInterestedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(
                                getActivity(),
                                InterestedEventsActivity.class
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
            }
        });

        btnAttendingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(
                                getActivity(),
                                AttendingEventsActivity.class
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
            }
        });

        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =
                        new Intent(
                                getActivity(),
                                ProfileActivity.class
                        );

                intent.putExtra(
                        "username",
                        username
                );

                intent.putExtra(
                        "email",
                        email
                );

                intent.putExtra(
                        "serverUserId",
                        serverUserId
                );

                startActivity(intent);
            }
        });

        return view;
    }
}