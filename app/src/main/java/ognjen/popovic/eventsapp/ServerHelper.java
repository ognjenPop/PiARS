package ognjen.popovic.eventsapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerHelper {

    private static final String BASE_URL = "http://10.1.145.100:3000";

    private RequestQueue requestQueue;

    public ServerHelper(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public void postRequest(String endpoint,
                            JSONObject requestBody,
                            ServerResponseListener listener) {

        String url = BASE_URL + endpoint;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                response -> listener.onSuccess(response),
                error -> listener.onError(error.toString())
        );

        requestQueue.add(request);
    }

    public void putRequest(String endpoint,
                           JSONObject requestBody,
                           ServerResponseListener listener) {

        String url = BASE_URL + endpoint;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                requestBody,
                response -> listener.onSuccess(response),
                error -> listener.onError(error.toString())
        );

        requestQueue.add(request);
    }

    public void getRequest(String endpoint,
                           ServerResponseListener listener) {

        String url = BASE_URL + endpoint;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> listener.onSuccess(response),
                error -> listener.onError(error.toString())
        );

        requestQueue.add(request);
    }

    public void getArrayRequest(String endpoint,
                                ServerArrayResponseListener listener) {

        String url = BASE_URL + endpoint;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> listener.onSuccess(response),
                error -> listener.onError(error.toString())
        );

        requestQueue.add(request);
    }

    public interface ServerResponseListener {
        void onSuccess(JSONObject response);

        void onError(String error);
    }

    public interface ServerArrayResponseListener {
        void onSuccess(JSONArray response);

        void onError(String error);
    }
}