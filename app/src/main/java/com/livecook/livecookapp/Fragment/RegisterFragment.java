package com.livecook.livecookapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.livecook.livecookapp.Activity.ClientRegisterActivity;
import com.livecook.livecookapp.Activity.LoginActivity;
import com.livecook.livecookapp.Activity.LoginResturantActivity;
import com.livecook.livecookapp.Activity.LogincookActivity;
import com.livecook.livecookapp.Activity.RegisterActivity;
import com.livecook.livecookapp.Activity.RegisterResturantActivity;
import com.livecook.livecookapp.Activity.RegistercookActivity;
import com.livecook.livecookapp.Adapter.DatumecountryAdapter;
import com.livecook.livecookapp.Api.MyApplication;
import com.livecook.livecookapp.MainActivity;
import com.livecook.livecookapp.Model.Constants;
import com.livecook.livecookapp.Model.Datum;
import com.livecook.livecookapp.Model.SessionManager;
import com.livecook.livecookapp.R;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterFragment extends Fragment {
    private ImageView registercook;
    private ImageView registerresturant;
    private ImageView registerclient;
    SharedPreferences prefs;
    private static final String TAG = "";
    private ImageView mAvatar;
    private Uri mCropImageUri;
    private LinearLayout mContainer;
    ImageView cameraAction;
    String file_name;

    private SessionManager mSessionManager;
    ProgressDialog progressDialog;
    String access_token;


    private Toolbar toolbar;

    private EditText edname;
    private EditText edmobileNumber;
    private EditText edpassward;
    private EditText edrepatepassward;
    private ImageView insertliscense,showimage;
    private Button createaccount;
    ArrayList<Datum> data = new ArrayList<>();
    ArrayList<Datum> city = new ArrayList<>();
    String image_cook_path;
    String imageName;
    SharedPreferences.Editor editor;
    int city_id;
    List<String> countrylist = new ArrayList<String>();
    List<String> citylist = new ArrayList<String>();
    ArrayList<Datum> countrycodelist = new ArrayList<Datum>();

    int country_id;
    int  country_codee;
    String country_mobile_codee;


    Spinner spin;
    Spinner cityname;
    Spinner countrycode;
    String fcm_token;

    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> cityadapter;
    DatumecountryAdapter countrycodeadapter;
    File f;
    boolean country_spinner_click=false;
    boolean city_spinner_click=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        getActivity().getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        edmobileNumber = view.findViewById(R.id.edmobile_number);
        edname=view.findViewById(R.id.edname);
        edpassward = view.findViewById(R.id.edpassward);
        createaccount = view.findViewById(R.id.createaccount);
        edrepatepassward= view.findViewById(R.id.edrepatepassward);
 prefs = getActivity().getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE);

        registercook = view.findViewById(R.id.registercook);
        registerresturant = view.findViewById(R.id.registerresturant);
        registerclient = view.findViewById(R.id.registerclient);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        String token = FirebaseInstanceId.getInstance().getToken();
        prefs = getActivity().getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE);

        mContainer = view.findViewById(R.id.container);
        progressDialog = new ProgressDialog(getActivity());




        //        ActionBar actionBar = ((AppCompatActivity)getApplicationContext()).getSupportActionBar();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        spin = view.findViewById(R.id.countryname);
        cityname = view.findViewById(R.id.cityname);
        countrycode = view.findViewById(R.id.countrycode);
        insertliscense=view.findViewById(R.id.camera_action);
        showimage=view.findViewById(R.id.showimage);
        showSoftKeyboard(edname);

        fcm_token = FirebaseInstanceId.getInstance().getToken();
        if(fcm_token!=null) {
            Log.d("khtwotoken", fcm_token);
        }

        insertliscense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()
                        .setTitle("اختار صورة الرخصة ")
                        .setTitleColor(R.color.green)



                ).show(getActivity());

            }
        });

        countrycode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_mobile_codee=countrycode.getSelectedItem().toString();
                //   Toast.makeText(ClientRegisterActivity.this, ""+countrycode.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getCountrycode();
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edname.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                }


                else if (edname.getText().toString().length()<3) {
                    Toast.makeText(getActivity(), getString(R.string.enter_name_lengh), Toast.LENGTH_SHORT).show();
                }


                else if (edmobileNumber.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
                }

                else if (edmobileNumber.getText().toString().length()<9) {
                    Toast.makeText(getActivity(), getString(R.string.enter_mobile_length), Toast.LENGTH_SHORT).show();
                }
                else if (edpassward.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), getString(R.string.enter_passward), Toast.LENGTH_SHORT).show();
                }
                else if (edrepatepassward.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), getString(R.string.repeatpasswward)
                            , Toast.LENGTH_SHORT).show();
                }
                /*else if (!country_spinner_click) {
                    Toast.makeText(getActivity(), getString(R.string.enter_country), Toast.LENGTH_SHORT).show();
                }
                else if (!city_spinner_click) {
                    Toast.makeText(getActivity(), getString(R.string.entercity), Toast.LENGTH_SHORT).show();
                }*/

                else  if ((edpassward.getText().toString()).matches(edrepatepassward.getText().toString()) ) {
                    //Toast.makeText(RegisterResturantActivity.this, ""+file_name, Toast.LENGTH_SHORT).show();
                    registeruser(edname.getText().toString(), edmobileNumber.getText().toString(), country_id,
                            edpassward.getText().toString(), edrepatepassward.getText().toString(), file_name,city_id,fcm_token);

                    /*edmobileNumber.setText("");
                    edpassward.setText("");
                    edrepatepassward.setText("");*/

                }
                else{
                    Toast.makeText(getActivity(), getString(R.string.not_match_passward), Toast.LENGTH_SHORT).show();

                }

                // registeruser("wafaa","+9720598271258",5,"1234567","1234567");
            }
        });

        registercook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegistercookActivity.class);
                intent.putExtra(Constants.cook_type, "cooker");
                saveObjectToPreferences(Constants.cook_type, "cooker");
                SharedPreferences.Editor editor = prefs.edit();
                intent.putExtra(Constants.TYPE, "cooker");
                editor.putString(Constants.TYPE, "cooker");
                editor.apply();
                getActivity().startActivity(intent);

//                    getActivity().finish();

            }
        });
        registerresturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterResturantActivity.class);
                intent.putExtra(Constants.restaurant_type, "restaurant,");
                saveObjectToPreferences(Constants.restaurant_type, "restaurant");
                SharedPreferences.Editor editor = prefs.edit();
                intent.putExtra(Constants.TYPE, "restaurant");

                editor.putString(Constants.TYPE, "restaurant");
                editor.apply();

                getActivity().startActivity(intent);
//                getActivity().finish();

            }
        });
        registerclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClientRegisterActivity.class);
                intent.putExtra(Constants.user_type, "user");
                intent.putExtra(Constants.TYPE, "user");
                saveObjectToPreferences(Constants.user_type, "user");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Constants.TYPE, "user");
                editor.apply();

                getActivity().startActivity(intent);
//                getActivity().finish();


            }
        });

        return view;
    }


    public void saveObjectToPreferences(String key, Object value) {
        final SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String jsonValue = new Gson().toJson(value);
        editor.putString(key, jsonValue);
        editor.apply();
    }
/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    return true;
                }
                return false;
            }
        });

    }*/

    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void getCountrycode(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://livecook.co/api/v1/constant/countries", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray=response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String nationality = jsonObject.getString("nationality");
                        String sort_name = jsonObject.getString("sort_name");
                        String code = jsonObject.getString("code");
                        String flag = jsonObject.getString("flag");

                        Datum datum=new Datum();
                        datum.setId(id);
                        datum.setName(name);
                        datum.setCode(code);
                        datum.setFlag(flag);

                        datum.setSortName(sort_name);
                        datum.setNationality(nationality);
                        data.add(datum);
                        countrycodelist.add(datum);


                    }
                    countrycodeadapter = new DatumecountryAdapter(getContext(), countrycodelist);
                    countrycode.setAdapter(countrycodeadapter);
                    countrycode.setSelection(4);
                    countrycodeadapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //JSONArray taskarray=response.getJSONArray("task");
                //for (int i = 0; i < taskarray.length(); i++) {



                /*
                try {
                    JSONArray array = response.getJSONArray(AppConstants.CONTACTS_KEY);
                    for(int i=0;i<array.length();i++){

                      //JSONObject jsonObject =   array.getJSONObject(i);
                      //jsonObject.get

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void registeruser(final String name, final String mobile, final int country_id, final String password, final String password_confirmation, final String municipal_license,final  int city_id,final String fcm_token) {
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.restaurant_register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject register_response = new JSONObject(response);
                    boolean status = register_response.getBoolean("status");
                    String message = register_response.getString("message");
                    JSONObject auth=register_response.getJSONObject("auth");
                    int id =auth.getInt("id");
                    access_token=register_response.getString("accessToken");

                    if (status) {

                        editor = prefs.edit();
                        editor = prefs.edit();
                        editor.putString(Constants.access_token1, access_token);
                        editor.putString(Constants.TYPE,"");
                        editor.putInt(Constants.ressturant_id_profile_publish,id);
                        editor.putString(Constants.access_token1, access_token);
                        editor.putString(Constants.TYPE,"restaurant");
                        editor.putInt(Constants.ressturant_id_profile_publish,id);
                        editor.putInt(Constants.ressturant_id_profile_publish,id);
                        editor.putString(Constants.access_token1, access_token);
                        editor.putBoolean(Constants.ISLOGIN,true);
                        editor.putString(Constants.mobile, edmobileNumber.getText().toString());
                        editor.putString(Constants.password,edpassward.getText().toString());
                        editor.putInt(Constants.user_id,id);
                        editor.putString(Constants.mobile, edmobileNumber.getText().toString());
                        editor.putString(Constants.password,edpassward.getText().toString());
                        Intent gotologin=new Intent(getActivity(), MainActivity.class);
                        gotologin.putExtra(Constants.access_token1,access_token);
                        gotologin.putExtra(Constants.ressturant_id_profile_publish,id);
                        editor.apply();
                        editor.commit();


                        loginuser(country_id,edmobileNumber.getText().toString(),edpassward.getText().toString(),0,fcm_token);

                        editor= prefs.edit();
                        editor.putString(Constants.mobile, edmobileNumber.getText().toString());
                        editor.putString(Constants.password,edpassward.getText().toString());
                        editor.apply();





                        Toast.makeText(getActivity(), "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "لم يتم التسجيل" + message, Toast.LENGTH_SHORT).show();

                    }

                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "هذا الجوال مستخدم مسبقا", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "هذا الجوال مستخدم مسبقا", Toast.LENGTH_SHORT).show();
                hideDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("name", name);
                map.put("mobile", mobile);
                map.put("country_id", country_id + "");
                map.put("password", password);
                map.put("password_confirmation", password_confirmation);
                map.put("municipal_license", municipal_license+"");
                map.put("city_id", city_id+"");
                map.put("fcm_token", fcm_token);




                return map;

            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest);


    }
    public void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.load_login));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {

        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    public void loginuser(final int  country_id,final  String mobile ,final String password,final int  remember_me,
                          final String fcm_token) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.restaurant_login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject register_response=new JSONObject(response);
                            JSONObject auth=register_response.getJSONObject("auth");
                            boolean status=register_response.getBoolean("status");
                            access_token=register_response.getString("access_token");
                            String type=register_response.getString("type");
                            int id=auth.getInt("id");


                            if(status){
                                editor = prefs.edit();
                                editor = prefs.edit();
                                editor.putString(Constants.access_token1, access_token);
                                editor.putString(Constants.TYPE,type);
                                editor.putInt(Constants.ressturant_id_profile_publish,id);
                                editor.putString(Constants.access_token1, access_token);
                                editor.putString(Constants.TYPE,type);
                                editor.putInt(Constants.ressturant_id_profile_publish,id);
                                editor.putInt(Constants.ressturant_id_profile_publish,id);
                                editor.putString(Constants.access_token1, access_token);
                                editor.putBoolean(Constants.ISLOGIN,true);
                                editor.putString(Constants.mobile, edmobileNumber.getText().toString());
                                editor.putString(Constants.password,edpassward.getText().toString());
                                editor.putInt(Constants.user_id,id);
                                editor.putString(Constants.mobile, edmobileNumber.getText().toString());
                                editor.putString(Constants.password,edpassward.getText().toString());
                                Intent gotologin=new Intent(getActivity(), MainActivity.class);
                                gotologin.putExtra(Constants.access_token1,access_token);
                                gotologin.putExtra(Constants.ressturant_id_profile_publish,id);

                                editor.apply();
                                editor.commit();

                                startActivity(gotologin);
                                getActivity().finish();



                            }
                            else
                            {
                                Toast.makeText(getActivity(), "قيمة الجوال مستتخدمة من قبل ", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "قيمة الجوال مستتخدمة من قبل ", Toast.LENGTH_SHORT).show();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "قيمة الجوال مستتخدمة من قبل ", Toast.LENGTH_SHORT).show();


            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap();
                map.put("country_id",country_id+"");
                map.put("mobile",mobile);
                map.put("password",password);
                map.put("remember_me",remember_me+"");
                map.put("fcm_token",fcm_token);





                return map;

            }
        }
                ;

        MyApplication.getInstance().addToRequestQueue(stringRequest);


    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent=new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            return true;

        }
        if (item.getItemId() == R.id.nav_home) {
            Intent intent=new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);

    }*/

}