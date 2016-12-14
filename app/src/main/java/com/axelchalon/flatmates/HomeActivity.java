package com.axelchalon.flatmates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private static final String USER_PREFS = "flatmates-prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        getHighscores();
        getTasks();
        getFlatmates();
        renameTask("8","Tâche (10)");
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

                        System.out.println("COLOCS");
                        try {
                            JSONArray scores = response.getJSONArray("scores");

                            for(int i=0;i<scores.length();i++){
                                JSONObject jsob = scores.optJSONObject(i);
                                System.out.println(jsob.getString("name") + " : " + jsob.getString("points"));
                                // @todo hydrate view
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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


    protected void getTasks() {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=get_tasks&user_num=" + usernum, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("TASKS");
                        try {
                            JSONArray scores = response.getJSONArray("history");

                            for(int i=0;i<scores.length();i++){
                                JSONObject jsob = scores.optJSONObject(i);
                                System.out.println(jsob.getString("id") + ". " + jsob.getString("task") + " : " + Integer.toString(jsob.getInt("weight")));
                                // @todo hydrate view
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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

    protected void getFlatmates() {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=get_flatmates&user_num=" + usernum, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("FLATMATES");
                        try {
                            JSONArray scores = response.getJSONArray("flatmates");

                            for(int i=0;i<scores.length();i++){
                                JSONObject jsob = scores.optJSONObject(i);
                                System.out.println(jsob.getString("num") + ". " + jsob.getString("name"));
                                // @todo hydrate view
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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

    protected void addFlatmate(String new_num) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=add_flatmate&user_num=" + usernum + "&new_num=" + new_num, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("NEW FLATMATE - OK");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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

    protected void didTask(String task_id) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=did_task&user_num=" + usernum + "&task_id=" + task_id, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("DID TASK OK");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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
                        System.out.println("NEW TASK OK");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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

    protected void deleteTask(String task_id) {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this;
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=delete_task&user_num=" + usernum + "&task_id=" + task_id, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("DELETE TASK OK");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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
                        System.out.println("CHANGE TASK WEIGHT OK");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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
                        System.out.println("RENAME TASK OK");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
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
