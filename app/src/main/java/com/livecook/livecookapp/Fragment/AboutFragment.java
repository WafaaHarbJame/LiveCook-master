package com.livecook.livecookapp.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.livecook.livecookapp.Api.MyApplication;
import com.livecook.livecookapp.MainActivity;
import com.livecook.livecookapp.Model.Constants;
import com.livecook.livecookapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.invoke.ConstantCallSite;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    TextView    polictext;
    WebView webView;
    ProgressDialog progressDialog;
    View lyt_failed;
    Button btn_failed_retry;



    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragmenttext

        View view=inflater.inflate(R.layout.fragment_about, container, false);
        //polictext=view.findViewById(R.id.polictext);
        webView = view.findViewById(R.id.webView);
        progressDialog = new ProgressDialog(getActivity());
        lyt_failed = view.findViewById(R.id.lyt_failed_home);
        btn_failed_retry = lyt_failed.findViewById(R.id.failed_retry);
        btn_failed_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lyt_failed.setVisibility(View.GONE);
                hideDialog();

                displayData();
            }

            public void displayData() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new AboutFragment()).commit();

                    }
                }, 1000);
            }

        });



        webView.setBackgroundColor(0);
        checkInternet();

        return view;
    }


    public void getAboutapp(final String link) {

        showDialog();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    JSONObject jsonObject = response.getJSONObject("data");
                    String text = jsonObject.getString("text");
                    String title=jsonObject.getString("title");


                    if(text!=null){
                        //Typeface font =  ResourcesCompat.getFont(getActivity(), R.font.jannalt_regular);
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setDefaultTextEncodingName("utf-8");
                        //webSettings.setFixedFontFamily(String.valueOf(font));

                         //webView.loadDataWithBaseURL("",text,"text/html",
                              //  null,"\"<html dir=\\\"rtl\\\" lang=\\\"\\\"><body>\" + outhtml + \"</body></html>\"");

                       // webView.loadDataWithBaseURL("",text,"text/html",
                             //   null,"<html><head></head><body>\" +content  + \"</body></html>");
                        Log.e("Text", text);

                        String dataa = "<html><head></head><body>" +
                                text  + "</body></html>";
                        Log.e("Wafaa", dataa);
                        //String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                      //  webView.loadData(header+dataa, "text/html", "UTF-8");

                     webView.loadData(dataa, "text/html", null);

                        String content =
                                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
                                        "<html><head>"+
                                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                                        "</head><body>";

                        content += text + "</body></html>";

                        //webView.loadData(content, "text/html; charset=utf-8", "UTF-8");

                      /*  webView.loadData(
                                Base64.encodeToString(
                                        text.getBytes(StandardCharsets.UTF_8),
                                        Base64.DEFAULT), // encode in Base64 encoded
                                "text/html; charset=utf-8", // utf-8 html content (personal recommendation)
                                "base64");*/


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

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();


                return map;

            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                return headers;
            }

        };
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    public void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void checkInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getAboutapp(Constants.aboutapp);


        } else {
            lyt_failed.setVisibility(View.VISIBLE);
            hideDialog();
        }

    }

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

    }
}
