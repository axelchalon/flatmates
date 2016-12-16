package com.axelchalon.flatmates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

import static android.content.Context.MODE_PRIVATE;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private static final String USER_PREFS = "flatmates-prefs";
    ListView mListView;

    private String selectedBesogne;
    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("woobzi");
        View view;
        if (getArguments().getInt(ARG_PAGE) == 1) {
            view = inflater.inflate(R.layout.fragment_besognes, container, false);
            getTasksDone(view);
        } else if (getArguments().getInt(ARG_PAGE) == 2) {
            view = inflater.inflate(R.layout.fragment_coloc, container, false);
            getFlatmates(view);
            getTasks(view);
        }
        else {
            view = inflater.inflate(R.layout.fragment_profil, container, false);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPage);
            getMyTasksDone(view);
        }
        return view;
    }



    protected void getFlatmates(final View view) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this.getActivity().getApplicationContext();
        final Activity hacktt = this.getActivity();
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=get_all_points&user_num=" + usernum, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        mListView = (ListView) view.findViewById(R.id.homeUsersListView);
                        System.out.println("FLATMATES");
                        try {
                            JSONArray scores = response.getJSONArray("scores");
                            List<String> users = new ArrayList<String>();

                            for(int i=0;i<scores.length();i++){
                                JSONObject jsob = scores.optJSONObject(i);
                                users.add(jsob.getString("num") + ". " + jsob.getString("name") + " : " + jsob.getString("points"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(hacktt,
                                    android.R.layout.simple_list_item_1, users);
                            mListView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
                        error.printStackTrace();

                        Context context = ctx;
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(ctx).add(myUserRequest);
    }


    protected void getTasks(final View view) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();


        final Context ctx = this.getActivity().getApplicationContext();
        final Activity hacktt = this.getActivity();
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=get_tasks&user_num=" + usernum, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        mListView = (ListView) view.findViewById(R.id.homeTasksListView);
                        System.out.println("TASKS");
                        try {
                            JSONArray scores = response.getJSONArray("history");

                            List<String> tasks = new ArrayList<String>();

                            for(int i=0;i<scores.length();i++){
                                JSONObject jsob = scores.optJSONObject(i);
                                System.out.println(jsob.getString("id") + ". " + jsob.getString("task") + " : " + Integer.toString(jsob.getInt("weight")));
                                tasks.add(jsob.getString("id") + ". " + jsob.getString("task") + " : " + Integer.toString(jsob.getInt("weight")));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(hacktt,
                                    android.R.layout.simple_list_item_1, tasks);
                            mListView.setAdapter(adapter);

                            registerForContextMenu(mListView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
                        error.printStackTrace();

                        Context context = ctx;
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(ctx).add(myUserRequest);
    }


    protected void getTasksDone(final View view) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();


        final Context ctx = this.getActivity().getApplicationContext();
        final Activity hacktt = this.getActivity();
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=get_tasks_done&user_num=" + usernum, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        mListView = (ListView) view.findViewById(R.id.homeTasksDoneListView);
                        System.out.println("TASKS");
                        try {
                            JSONArray scores = response.getJSONArray("history");

                            List<String> tasks = new ArrayList<String>();

                            for(int i=0;i<scores.length();i++){
                                JSONObject jsob = scores.optJSONObject(i);
                                tasks.add(jsob.getString("name") + " a fait : " + jsob.getString("task") + " le " + jsob.getString("timestamp"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(hacktt,
                                    android.R.layout.simple_list_item_1, tasks);
                            mListView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
                        error.printStackTrace();

                        Context context = ctx;
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(ctx).add(myUserRequest);
    }


    protected void getMyTasksDone(final View view) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();


        final Context ctx = this.getActivity().getApplicationContext();
        final Activity hacktt = this.getActivity();
        JsonObjectRequest myUserRequest = new JsonObjectRequest
                (Request.Method.GET, "http://swarm.ovh:4/index.php?action=get_tasks_done&user_num=" + usernum, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        mListView = (ListView) view.findViewById(R.id.homeTasksDoneListViewMM);
                        System.out.println("TASKS");
                        try {
                            JSONArray scores = response.getJSONArray("history");

                            List<String> tasks = new ArrayList<String>();

                            for(int i=0;i<scores.length();i++){
                                JSONObject jsob = scores.optJSONObject(i);
                                tasks.add(jsob.getString("task") + " le " + jsob.getString("timestamp"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(hacktt,
                                    android.R.layout.simple_list_item_1, tasks);
                            mListView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WCC");
                        error.printStackTrace();

                        Context context = ctx;
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(ctx).add(myUserRequest);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedBesogne = ((TextView) info.targetView).getText().toString();


        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE).edit();
        editor.putString("selcmitem", selectedBesogne);
        editor.commit();

        menu.setHeaderTitle("Besogne");
        menu.add(0, v.getId(), 0, "C'est fait !");
        menu.add(0, v.getId(), 0, "Supprimer la besogne");
        menu.add(0, v.getId(), 0, "Nouvelle besogne");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        System.out.println("Bselected bes" + selectedBesogne);

        if(item.getTitle()=="C'est fait !"){function1(item.getItemId());}
        else if(item.getTitle()=="Supprimer la besogne"){function2(item.getItemId());}
        else if(item.getTitle()=="Nouvelle besogne"){function3(item.getItemId());}
        else {return false;}
        return true;
    }

    public void function1(int id){
        System.out.println("id" + id);
        System.out.println("selected bes" + selectedBesogne);

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String selectedBesogne = prefs.getString("selcmitem", null);


        String points = selectedBesogne.substring(selectedBesogne.lastIndexOf(" ") + 1);
        String selected = selectedBesogne.substring(0, selectedBesogne.indexOf("."));
        System.out.println("points" + points);
        System.out.println("selected" + selected);
        didTask(selected);
        Toast.makeText(this.getActivity().getApplicationContext(), "Super ! Cela te fait " + points + " points en plus !", Toast.LENGTH_SHORT).show();
    }
    public void function2(int id){

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String selectedBesogne = prefs.getString("selcmitem", null);


        String points = selectedBesogne.substring(selectedBesogne.lastIndexOf(" ") + 1);
        String selected = selectedBesogne.substring(0, selectedBesogne.indexOf("."));

        deleteTask(selected);
        Toast.makeText(this.getActivity().getApplicationContext(), "La besogne a été supprimée !", Toast.LENGTH_SHORT).show();
    }
    public void function3(int id){

        Intent i = new Intent(this.getActivity().getApplicationContext(), NewTaskActivity.class);
        startActivity(i);
    }

    protected void deleteTask(String task_id) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this.getActivity().getApplicationContext();
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

                        Context context = ctx;
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(ctx).add(myUserRequest);
    }




    protected void didTask(String task_id) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String usernum = prefs.getString("usernum", null);

        HashMap<String,String> params = new HashMap<String, String>();

        final Context ctx = this.getActivity().getApplicationContext();
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

                        Context context = ctx;
                        CharSequence text = "Impossible de se connecter au réseau, camarade !";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        VolleyLog.e("Error: ", error.getMessage());
                    }
                });

        Volley.newRequestQueue(ctx).add(myUserRequest);
    }


}