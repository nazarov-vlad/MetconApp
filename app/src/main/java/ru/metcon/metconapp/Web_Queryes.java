package ru.metcon.metconapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Web_Queryes extends AppCompatActivity implements View.OnClickListener {
    Button btnSend;
    Button btnSend2;
    private static final String JSON_URL = "http://m1.maxfad.ru/api/users.json";// UTF-8
    private static final String JSON_URL2 = "http://192.168.1.23/CHZMK/hs/Zakaz/TYPES/2550";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_queryes);
        btnSend = (Button) findViewById(R.id.bWebQueriesSend);
        btnSend.setOnClickListener(this);
        btnSend2 = (Button) findViewById(R.id.bWebQueriesSend2);
        btnSend2.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        switch (v.getId()) {
            case R.id.bWebQueriesSend:
                loadJSONFromURL(JSON_URL);
                break;
            case R.id.bWebQueriesSend2:
                loadJSONFromURL(JSON_URL2);
                break;

        }
    }

    private void loadJSONFromURL(String url) {
        final String UserName = "назаров";
        final String PassWord = "128500";
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ListView.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
//                            JSONObject object = new JSONObject(EncodingToUTF8(response));
                            JSONObject object = new JSONObject(response);
                            String name = object.getString("Manager");
                            JSONArray jsonArray = object.getJSONArray("Table");
                            ArrayList<JSONArray> listItems = getArrayListFromJSONArray(jsonArray);
                            ListAdapter adapter = new webListViewAdapter(getApplicationContext(), R.layout.web_queries_row, R.id.textViewCod1, listItems);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        if (error.getCause().toString()=="com.android.volley.AuthFailureError") {
                            Toast.makeText(getApplicationContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                        };

                    }
                }){

            //This is for Headers If You Needed
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("token", ACCESS_TOKEN);
//                return params;
//            }

//            //Pass Your Parameters here
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("User", UserName);
//                params.put("Pass", PassWord);
//                return params;
//            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = UserName + ":" + PassWord;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private ArrayList<JSONArray> getArrayListFromJSONArray(JSONArray jsonArray) {
        ArrayList<JSONArray> aList = new ArrayList<JSONArray>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray obj = jsonArray.getJSONArray(i);
                    aList.add(obj);
                }
            }
        } catch (JSONException js) {
            js.printStackTrace();
        }
        return aList;
    }

    public static String EncodingToUTF8(String response) {
        try {
            byte[] code = response.toString().getBytes("ISO-8859-1");
            response = new String(code, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }
}