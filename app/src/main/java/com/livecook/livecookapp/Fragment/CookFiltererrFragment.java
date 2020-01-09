package com.livecook.livecookapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.livecook.livecookapp.Adapter.CookAdapter;
import com.livecook.livecookapp.Api.MyApplication;
import com.livecook.livecookapp.MainActivity;
import com.livecook.livecookapp.Model.AllcookModel;
import com.livecook.livecookapp.Model.Constants;
import com.livecook.livecookapp.Model.CookModel;
import com.livecook.livecookapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CookFiltererrFragment extends Fragment {
    private static  Boolean ISLOGIN;
    RecyclerView cookrecycler;
    ArrayList<CookModel> data = new ArrayList<>();
    SwipeRefreshLayout cookswip;
    CookAdapter cookAdapter;
    CookModel cookModel;
    ProgressDialog progressDialog;
    ArrayList<AllcookModel.DataBean> data1 = new ArrayList<>();
    Button btn_failed_retry;

    View lyt_failed;
    SharedPreferences prefs;
    String tokenfromlogin;
    String typnumer;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cook_filterr, container, false);
        cookAdapter = new CookAdapter(data1, getActivity());
        prefs = getActivity().getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE);
        tokenfromlogin = prefs.getString(Constants.access_token1, "default value");


        cookrecycler = view.findViewById(R.id.cookrecycler);
        //editsearch = view.findViewById(R.id.simpleSearchView);
        progressDialog = new ProgressDialog(getActivity());
        lyt_failed  = view.findViewById(R.id.lyt_failed_home);
        btn_failed_retry = lyt_failed.findViewById(R.id.failed_retry);
        cookswip = view.findViewById(R.id.cookswip);
        cookswip.setColorSchemeResources
                (R.color.colorPrimary, android.R.color.holo_green_dark,
                        android.R.color.holo_orange_dark,
                        android.R.color.holo_blue_dark);

        checkInternet();
       // getCookerforall();

        cookswip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

               // data1.clear();

                checkInternet();


            }
        });



        btn_failed_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyt_failed.setVisibility(View.GONE);
                hideDialog();
                cookswip.setRefreshing(false);

                displayData();
            }

            public void displayData() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent= new Intent(getContext(), MainActivity.class);
                        startActivity(intent);

                    }
                }, 1000);
            }

        });


     RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(),1);
       // RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());


        cookrecycler.setLayoutManager(manager);
      //  getCooker();

        return view;
    }




    public void getCooker(final  int type_id,final int country_id,final int city_id,final String region,final String name) {
        cookswip.setRefreshing(true);

        //showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.allcooker,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HZM",response);

                        try {
                            JSONObject task_respnse=new JSONObject(response);
                            boolean status=task_respnse.getBoolean("status");
                          //Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                            JSONArray taskarray = task_respnse.getJSONArray("data");
                            for(int i=0;i<taskarray.length();i++) {
                                int id=taskarray.getJSONObject(i).getInt("id");
                                String name = taskarray.getJSONObject(i).getString("name");
                                int type_id=taskarray.getJSONObject(i).getInt("type_id");
                                String type_name = taskarray.getJSONObject(i).getString("type_name");
                                int countryId = taskarray.getJSONObject(i).getInt("country_id");
                                String countryName = taskarray.getJSONObject(i).getString("country_name");
                                int cityId = taskarray.getJSONObject(i).getInt("city_id");
                                String cityName  = taskarray.getJSONObject(i).getString("city_name");
                                String region  = taskarray.getJSONObject(i).getString("region");
                                String avatarURL  = taskarray.getJSONObject(i).getString("avatar_url");
                                String avatar= taskarray.getJSONObject(i).getString("avatar");
                                String description  = taskarray.getJSONObject(i).getString("description");
                                int followersNo  = taskarray.getJSONObject(i).getInt("followers_no");
                                Boolean  isFollowed=taskarray.getJSONObject(i).getBoolean("is_followed");
                                Boolean  isFavorite=taskarray.getJSONObject(i).getBoolean("is_favorite");

                                AllcookModel.DataBean dataBean=new AllcookModel.DataBean();
                                dataBean.setId(id);
                                dataBean.setCity_id(cityId);
                                dataBean.setAvatar_url(avatarURL);
                                dataBean.setCountry_name(countryName);
                                dataBean.setFollowers_no(followersNo);
                                dataBean.setId(id);
                                dataBean.setDescription(description);
                                dataBean.setRegion(region);
                                dataBean.setCity_name(cityName);
                                dataBean.setName(name);
                                dataBean.setCountry_id(countryId);
                                dataBean.setIs_followed(isFollowed);
                                dataBean.setIs_favorite(isFavorite);
                                dataBean.setId(id);

                                data1.add(dataBean);
                                //Toast.makeText(getActivity(), ""+data1, Toast.LENGTH_SHORT).show();


                            }


                            RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                            cookrecycler.setLayoutManager(manager);
                            cookrecycler.setAdapter(cookAdapter);
                            cookAdapter.notifyDataSetChanged();
                            cookswip.setRefreshing(false);
                            cookAdapter.notifyDataSetChanged();


                          //  hideDialog();



                        } catch (JSONException e) {

                            e.printStackTrace();
                            //hideDialog();
                            cookswip.setRefreshing(false);


                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //hideDialog();
                cookswip.setRefreshing(false);


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("country_id" ,country_id+"");
                headers.put("type_id" ,type_id+"");
                headers.put("city_id" ,city_id+"");
                headers.put("region" ,region+"");
                headers.put("name" ,name+"");

                return headers;


            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("country_id" ,country_id+"");
                return headers;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);

    }



    public void getCookerforall() {
        cookswip.setRefreshing(true);

        //showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.allcooker,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("HZM",response);

                        try {
                            JSONObject task_respnse=new JSONObject(response);
                            boolean status=task_respnse.getBoolean("status");
                            JSONArray taskarray = task_respnse.getJSONArray("data");
                            for(int i=0;i<taskarray.length();i++) {
                                int id=taskarray.getJSONObject(i).getInt("id");
                                String name = taskarray.getJSONObject(i).getString("name");
                                int countryId = taskarray.getJSONObject(i).getInt("country_id");
                                String countryName = taskarray.getJSONObject(i).getString("country_name");
                                int cityId = taskarray.getJSONObject(i).getInt("city_id");
                                String cityName  = taskarray.getJSONObject(i).getString("city_name");
                                String region  = taskarray.getJSONObject(i).getString("region");
                                String avatarURL  = taskarray.getJSONObject(i).getString("avatar_url");
                                String description  = taskarray.getJSONObject(i).getString("description");
                                int followersNo  = taskarray.getJSONObject(i).getInt("followers_no");
                                   AllcookModel.DataBean dataBean=new AllcookModel.DataBean();
                                dataBean.setId(id);
                                dataBean.setCity_id(cityId);
                                dataBean.setAvatar_url(avatarURL);
                                dataBean.setCountry_name(countryName);
                                dataBean.setFollowers_no(followersNo);
                                dataBean.setId(id);
                                dataBean.setDescription(description);
                                dataBean.setRegion(region);
                                dataBean.setCity_name(cityName);
                                dataBean.setName(name);
                                dataBean.setCountry_id(countryId);
                                dataBean.setId(id);
                                data1.add(dataBean);

                            }


                            RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                            cookrecycler.setLayoutManager(manager);
                            cookrecycler.setAdapter(cookAdapter);
                            cookAdapter.notifyDataSetChanged();
                            cookswip.setRefreshing(false);
                            cookAdapter.notifyDataSetChanged();


                            //  hideDialog();



                        } catch (JSONException e) {

                            e.printStackTrace();
                            //hideDialog();
                            cookswip.setRefreshing(false);


                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //hideDialog();
                cookswip.setRefreshing(false);


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                return headers;


            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                return headers;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);

    }

    public void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void checkInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

           /* data1.clear();
            getCooker(tokenfromlogin);
            cookAdapter.notifyDataSetChanged();*/

            if (prefs != null) {
                typnumer = prefs.getString(Constants.TYPE, "user");
                ISLOGIN=prefs.getBoolean(Constants.ISLOGIN,false);
             //   Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
             //   Toast.makeText(getActivity(), "type " + typnumer, Toast.LENGTH_SHORT).show();



                 if (typnumer.matches("cooker")) {
                    data1.clear();
                    getCookerforall();
                    cookAdapter.notifyDataSetChanged();
                } else if (typnumer.matches("restaurant")) {

                    data1.clear();
                    getCookerforall();
                    cookAdapter.notifyDataSetChanged();

                } else if (typnumer.matches("user")) {

                    data1.clear();
                    getCooker(1,2,3,"hh","");
                    cookAdapter.notifyDataSetChanged();




                }

            }














        }
        else

        {
            lyt_failed.setVisibility(View.VISIBLE);
            hideDialog();
        }
    }


}
