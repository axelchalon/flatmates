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

import java.util.HashMap;

public class AddFlatouActivity extends AppCompatActivity {

    private static final String USER_PREFS = "flatmates-prefs";
    private Button submitButton;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flatou);
    }

    public void usernumSubmit(View view) {
        submitButton = (Button) findViewById(R.id.usernum_submit_button);
        usernameEditText = (EditText) findViewById(R.id.username_edittext);
        String new_num = usernameEditText.getText().toString();

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);
        HashMap<String,String> params = new HashMap<String, String>();
        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=add_flatmate&user_num=" + usernum + "&new_num=" + new_num, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Intent i = new Intent(ctx, HomeActivity.class);
                        startActivity(i);

                        CharSequence text = "Le coloc a été ajouté !";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(ctx, text, duration);
                        toast.show();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Context context = getApplicationContext();
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();


                        error.printStackTrace();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
    }
}
