package com.axelchalon.flatmates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class VerifyActivity extends AppCompatActivity {

    private static final String USER_PREFS = "flatmates-prefs";
    private Button submitButton;
    private EditText tokenEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_verify);
    }

    public void tokenSubmit(View view) {
        submitButton = (Button) findViewById(R.id.token_submit_button);
        tokenEditText = (EditText) findViewById(R.id.token_edittext);
        assert tokenEditText != null;
        String token = tokenEditText.getText().toString().replace(" ", "");

        // Saving token in sharedPreferences
        if (token.trim().length() > 0) {
            SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
            String usernum = prefs.getString("usernum", null);
            String username = prefs.getString("username", null);



            HashMap<String,String> params = new HashMap<String, String>();
            final Context ctx = this;
            JsonObjectRequest myUserRequest = new JsonObjectRequest
                    (Request.Method.GET, "http://swarm.ovh:4/index.php?action=verify_account&user_num=" + usernum + "&token=" + token + "&user_name=" + username, new JSONObject(params), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if (!response.has("success")) {
                                Context context = getApplicationContext();
                                CharSequence text = "Le code n'est pas valide, camarade !";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                            else
                            {
                                Intent i = new Intent(ctx, HomeActivity.class);
                                startActivity(i);

                                CharSequence text = "Bienvenue à bord !";
                                int duration = Toast.LENGTH_LONG;
                                Toast toast = Toast.makeText(ctx, text, duration);
                                toast.show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();


                            Context context = getApplicationContext();
                            CharSequence text = "Mauvais code, camarade !";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            VolleyLog.e("Error: ", error.getMessage());
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
