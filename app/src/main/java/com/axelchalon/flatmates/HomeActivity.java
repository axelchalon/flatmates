package com.axelchalon.flatmates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String USER_PREFS = "flatmates-prefs";
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

//        getHighscores();
        // getTasks();
//        getFlatmates();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                HomeActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected void getHighscores() {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=get_all_points&user_num=" + usernum, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONArray scores = response.getJSONArray("scores");

                            for(int i=0;i<scores.length();i++){
                                JSONObject jsob = scores.optJSONObject(i);

                                // @todo hydrate view
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Context context = getApplicationContext();
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
    }


    public void addFlatou(View view){

        Intent i = new Intent(this, AddFlatouActivity.class);
        startActivity(i);
    }

    protected void addFlatmate(String new_num) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=add_flatmate&user_num=" + usernum + "&new_num=" + new_num, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Context context = getApplicationContext();
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
    }

    protected void removeFlatmate(String new_num) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=remove_flatmate&user_num=" + usernum + "&new_num=" + new_num, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Context context = getApplicationContext();
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
    }

    protected void newTask(String task_name, String task_weight) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=new_task&user_num=" + usernum + "&new_task=" + task_name + "&new_weight=" + task_weight, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Context context = getApplicationContext();
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
    }

    protected void changeTaskWeight(String task_id, String new_weight) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=change_task_weight&user_num=" + usernum + "&task_id=" + task_id + "&new_weight=" + new_weight, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Context context = getApplicationContext();
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
    }

    protected void renameTask(String task_id, String new_name) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=rename_task&user_num=" + usernum + "&task_id=" + task_id + "&new_name=" + new_name, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Context context = getApplicationContext();
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
    }

    protected void renameUser(String new_name) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=rename_user&user_num=" + usernum + "&new_name=" + new_name, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                        Context context = getApplicationContext();
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(myUserRequest);
    }
}
