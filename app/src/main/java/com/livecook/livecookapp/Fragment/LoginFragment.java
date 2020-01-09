package com.livecook.livecookapp.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.livecook.livecookapp.Activity.ChangePasswardActivity;
import com.livecook.livecookapp.Activity.LoginActivity;
import com.livecook.livecookapp.Activity.LoginPageActivity;
import com.livecook.livecookapp.Activity.LoginResturantActivity;
import com.livecook.livecookapp.Activity.LogincookActivity;
import com.livecook.livecookapp.Activity.RegisterActivity;
import com.livecook.livecookapp.Adapter.DatumecountryAdapter;
import com.livecook.livecookapp.Api.MyApplication;
import com.livecook.livecookapp.MainActivity;
import com.livecook.livecookapp.Model.Constants;
import com.livecook.livecookapp.Model.Datum;
import com.livecook.livecookapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private ImageView registercook;
    private ImageView registerresturant;
    private ImageView registerclient;
     SharedPreferences prefs ;
    private ScrollView mScrollView2;
    private Toolbar mToolbar;
    private TextView mCreateacoount;
    private TextView mTvmobile;
    private EditText mEdmobileNumber;
    private TextView mTxpassward;
    private EditText mEdpassward;
    private TextView mCreatenewaccount;
    private Button login;
    private CheckBox saveLoginCheckBox;
    private Boolean saveLogin;
    public String access_token;
    int rememberme=0;
    String country_idd;
    ArrayList<Datum> countrycodelist = new ArrayList<Datum>();
    ImageView bacck;

    DatumecountryAdapter countrycodeadapter;
    int  countrycode_reg;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String  country_id;
    String mobile,password;
    private static final String TAG = "tokenreg";
    Spinner countrycode ;
    String country_mobile_codee;
    ArrayList<Datum> data = new ArrayList<>();
    TextView changepassward;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        prefs = getActivity().getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE);
            registercook = view.findViewById(R.id.registercook);
            registerresturant = view.findViewById(R.id.registerresturant);
            registerclient = view.findViewById(R.id.registerclient);
            registercook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), LogincookActivity.class);
                    intent.putExtra(Constants.cook_type,"cooker");
                    saveObjectToPreferences(Constants.cook_type,"cooker");
                    SharedPreferences.Editor editor = prefs.edit();
                    intent.putExtra(Constants.TYPE,"cooker");
                    editor.putString(Constants.TYPE,"cooker");
                    editor.apply();
                    getActivity().startActivity(intent);
                    getActivity().finish();


                }
            });


        registerresturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginResturantActivity.class);
                intent.putExtra(Constants.restaurant_type,"restaurant,");
                saveObjectToPreferences(Constants.restaurant_type,"restaurant");
                SharedPreferences.Editor editor = prefs.edit();
                intent.putExtra(Constants.TYPE,"restaurant");

                editor.putString(Constants.TYPE,"restaurant");
                editor.apply();

                getActivity().startActivity(intent);
                getActivity().finish();

            }
        });
        registerclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                intent.putExtra(Constants.user_type,"user");
                intent.putExtra(Constants.TYPE,"user");
                saveObjectToPreferences(Constants.user_type,"user");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Constants.TYPE,"user");
                editor.apply();
                getActivity().startActivity(intent);
                getActivity().finish();


            }
        });
        countrycode = view.findViewById(R.id.countrycode);

        //final String token = FirebaseInstanceId.getInstance().getToken();
        //Log.i(TAG, "FCM Registration Token: " + token);
        //Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
        countrycode = view.findViewById(R.id.countrycode);
        getCountrycode();
        prefs = getActivity().getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE);

        editor = prefs.edit();
        progressDialog = new ProgressDialog(getActivity());


        prefs = getActivity().getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE);
        bacck=view.findViewById(R.id.bacck);
        bacck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), LoginPageActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        countrycode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_mobile_codee=countrycode.getSelectedItem().toString();
                countrycode_reg=data.get(position).getId();
                // Toast.makeText(LoginResturantActivity.this, "selected"+countrycode.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(LoginResturantActivity.this, "code"+data.get(position).getCode(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(LoginResturantActivity.this, "id"+data.get(position).getId(), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                countrycode_reg=data.get(0).getId();


            }
        });

        mToolbar = view.findViewById(R.id.toolbar);
        mCreateacoount = view.findViewById(R.id.createacoount);
        mTvmobile = view.findViewById(R.id.tvmobile);
        mEdmobileNumber = view.findViewById(R.id.edmobile_number);
        mTxpassward = view.findViewById(R.id.txpassward);
        mEdpassward = view.findViewById(R.id.edpassward);
        mCreatenewaccount = view.findViewById(R.id.createnewaccount);
        login = view.findViewById(R.id.login);

        mCreatenewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotologin=new Intent(getActivity(), RegisterActivity.class);
                startActivity(gotologin);
                getActivity().finish();

            }
        });

        showSoftKeyboard(mEdmobileNumber);



        getCountrycode();

        prefs = getActivity().getSharedPreferences(Constants.PREF_FILE_CONFIG, Context.MODE_PRIVATE);
        country_idd = prefs.getString(Constants.countrycode_reg, "");
        if(prefs!=null) {

            mobile = prefs.getString(Constants.mobile, "");
            password = prefs.getString(Constants.password, "");
            mEdmobileNumber.setText(mobile);
            mEdpassward.setText(password);
        }
        saveLoginCheckBox = view.findViewById(R.id.saveLoginCheckBox);
        editor = prefs.edit();


        saveLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rememberme=1;
                }

                else {
                    rememberme=0;


                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (mEdmobileNumber.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show();
                }
                else if (mEdmobileNumber.getText().toString().length()<9) {
                    Toast.makeText(getActivity(), getString(R.string.enter_mobile_length), Toast.LENGTH_SHORT).show();
                }




                else if (mEdpassward.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), getString(R.string.enter_passward), Toast.LENGTH_SHORT).show();
                }
                else {


                    if (saveLoginCheckBox.isChecked()) {
                        loginuser(countrycode_reg, mEdmobileNumber.getText().toString(), mEdpassward.getText().toString(), 1, "");


                    } else {
                        loginuser(countrycode_reg, mEdmobileNumber.getText().toString(), mEdpassward.getText().toString(), 0, "");


                    }

                }




            }
        });



        changepassward=view.findViewById(R.id.changepassward);
        changepassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotologin=new Intent(getActivity(), ChangePasswardActivity.class);
                startActivity(gotologin);

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
                        datum.setSortName(sort_name);
                        datum.setNationality(nationality);
                        datum.setFlag(flag);
                        data.add(datum);
                        countrycodelist.add(datum);


                    }
                    countrycodeadapter = new DatumecountryAdapter(getActivity(), countrycodelist);
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


    public void loginuser(final int  country_id,final  String mobile ,final String password,final int  remember_me,
                          final String fcm_token) {
        showDialog();
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
                                editor.putString(Constants.mobile, mEdmobileNumber.getText().toString());
                                editor.putString(Constants.password,mEdpassward.getText().toString());
                                editor.putInt(Constants.user_id,id);
                                editor.putString(Constants.mobile, mEdmobileNumber.getText().toString());
                                editor.putString(Constants.password,mEdpassward.getText().toString());
                                Intent gotologin=new Intent(getActivity(), MainActivity.class);
                                gotologin.putExtra(Constants.access_token1,access_token);
                                gotologin.putExtra(Constants.ressturant_id_profile_publish,id);

                                editor.apply();
                                editor.commit();

                                startActivity(gotologin);

                                hideDialog();
                                getActivity().finish();



                            }
                            else
                            {
                                Toast.makeText(getActivity(), "لم يتم الدخول", Toast.LENGTH_SHORT).show();

                            }

                            hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

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

    public void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.load_login));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}