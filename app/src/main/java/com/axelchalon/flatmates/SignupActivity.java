package com.axelchalon.flatmates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private static final String USER_PREFS = "flatmates-prefs";
    private Button submitButton;
    private EditText usernumEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup);

        // Check if usernum is already set, if so, directly go to mainMenu
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String savedusernum = prefs.getString("usernum", null);
        if (savedusernum != null) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
    }

    public void usernumSubmit(View view) {
        submitButton = (Button) findViewById(R.id.usernum_submit_button);
        usernumEditText = (EditText) findViewById(R.id.usernum_edittext);
        assert usernumEditText != null;
        String usernum = usernumEditText.getText().toString().replace(" ", "");

        // Saving usernum in sharedPreferences
        if (usernum.trim().length() > 0) {
            SharedPreferences.Editor editor = getSharedPreferences(USER_PREFS, MODE_PRIVATE).edit();
            editor.putString("usernum", usernum);
            editor.commit();

            SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("user_num",usernum);

            final Context ctx = this;
            JsonObjectRequest myUserRequest = new JsonObjectRequest
                    (Request.Method.GET, "http://swarm.ovh:4/index.php", new JSONObject(params), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Intent i = new Intent(ctx, VerifyActivity.class);
                            startActivity(i);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Votre numéro de téléphone, camarade !";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
