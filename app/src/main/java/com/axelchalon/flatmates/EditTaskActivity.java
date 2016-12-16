package com.axelchalon.flatmates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class EditTaskActivity extends AppCompatActivity {

    private static final String USER_PREFS = "flatmates-prefs";
    private Button submitButton;
    private EditText usernumEditText;
    private EditText usernameEditText;

    private String wlmid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Intent myIntent = getIntent(); // gets the previously created intent
        String weight = myIntent.getStringExtra("weight"); // will return "FirstKeyValue"
        String name= myIntent.getStringExtra("name");
        wlmid = myIntent.getStringExtra("task_id");

        System.out.println("weight" + weight);
        System.out.println("name" + name);
        System.out.println("wlmid" + wlmid);

        ((EditText) findViewById(R.id.usernum_edittext)).setText(weight);
        ((EditText) findViewById(R.id.username_edittext)).setText(name);
    }

    public void usernumSubmit(View view) {
        submitButton = (Button) findViewById(R.id.usernum_submit_button);
        usernumEditText = (EditText) findViewById(R.id.usernum_edittext);
        usernameEditText = (EditText) findViewById(R.id.username_edittext);
        assert usernumEditText != null;
        String new_weight = usernumEditText.getText().toString();
        String new_task = usernameEditText.getText().toString();

        // Saving usernum in sharedPreferences
        if (new_task.trim().length() > 0 && new_weight.trim().length() > 0) {

            System.out.println("new weight" + new_weight);
            System.out.println("new name" + new_task);

            SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
            String usernum = prefs.getString("usernum", null);
            HashMap<String,String> params = new HashMap<String, String>();
            final Context ctx = this;
            JsonObjectRequest myUserRequest = new JsonObjectRequest
                    (Request.Method.GET, "http://swarm.ovh:4/index.php?action=edit_task&user_num=" + usernum + "&task_id="+wlmid+"&new_name=" + new_task + "&new_weight=" + new_weight, new JSONObject(params), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("WBB");
                            Intent i = new Intent(ctx, HomeActivity.class);
                            startActivity(i);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Context context = getApplicationContext();
                            CharSequence text = "Impossible de se connecter au réseau, camarade !";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            System.out.println("WCC");
                            error.printStackTrace();

                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });

            Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Remplis les champs, camarade !";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
