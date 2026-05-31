package ognjen.popovic.eventsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsActivity extends AppCompatActivity {

    private TextView tvNoFriendsActivity;
    private LinearLayout friendsActivityLayout;

    private ServerHelper serverHelper;

    private String serverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        serverHelper =
                new ServerHelper(this);

        serverUserId =
                getIntent().getStringExtra("serverUserId");

        tvNoFriendsActivity =
                findViewById(R.id.tvNoFriendsActivity);

        friendsActivityLayout =
                findViewById(R.id.friendsActivityLayout);

        loadFriendsActivity();
    }

    private void loadFriendsActivity() {

        if (serverUserId == null || serverUserId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Nedostaje server ID korisnika",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        serverHelper.getArrayRequest(
                "/friends-activity/" + serverUserId,
                new ServerHelper.ServerArrayResponseListener() {
                    @Override
                    public void onSuccess(JSONArray response) {

                        friendsActivityLayout.removeAllViews();

                        if (response.length() == 0) {

                            tvNoFriendsActivity.setVisibility(View.VISIBLE);
                            friendsActivityLayout.setVisibility(View.GONE);

                            return;
                        }

                        tvNoFriendsActivity.setVisibility(View.GONE);
                        friendsActivityLayout.setVisibility(View.VISIBLE);

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject object =
                                        response.getJSONObject(i);

                                String username =
                                        object.optString("username", "Unknown");

                                String eventName =
                                        object.optString("eventName", "Unknown");

                                String commitment =
                                        object.optString("commitment", "");

                                addFriendActivityView(
                                        username,
                                        eventName,
                                        commitment
                                );
                            }

                        } catch (JSONException e) {

                            Toast.makeText(
                                    FriendsActivity.this,
                                    "Greska prilikom citanja aktivnosti",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                FriendsActivity.this,
                                "Server nije dostupan",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void addFriendActivityView(String username,
                                       String eventName,
                                       String commitment) {

        TextView textView =
                new TextView(this);

        textView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        textView.setPadding(8, 8, 8, 8);

        textView.setTextColor(
                getResources().getColor(R.color.gray_text)
        );

        textView.setTextSize(16);

        String text =
                username + " je oznacio dogadjaj \"" +
                        eventName + "\" kao " +
                        commitment;

        textView.setText(text);

        friendsActivityLayout.addView(textView);
    }
}