package com.example.jemmycalak.thisismymarket.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.jemmycalak.thisismymarket.Config;
import com.example.jemmycalak.thisismymarket.MainActivity;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.userSharedPreference;
import com.example.jemmycalak.thisismymarket.util.VolleySingleton;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tolbar;

    private EditText nmL, eml, pw, pw2, notelp;
    private RadioButton radioButton;
    private RadioGroup group;
    private Button regis;
    private SignInButton google;
    private AlertDialog.Builder builder, builder1;
    private String url= Config.app_url_api+"users/register";
    private String mid, mnama, memail, mjk, mnotelp, mtoken, mtokenfirebase;
    private ProgressDialog mProgressDialog;

    //sharef preferens
    userSharedPreference session;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ///call sharedPreferens
        session= new userSharedPreference(getApplicationContext());

        ////call alertBuilder
        builder=new AlertDialog.Builder(Register.this);
        builder1=new AlertDialog.Builder(Register.this);
        mProgressDialog = new ProgressDialog(Register.this);

        setolbar();

        regis=(Button)findViewById(R.id.daftar_reg);

        regis.setOnClickListener(this);
        getTokenFireBase();
    }

    private void setolbar() {
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.daftar_reg:
                regis();
                break;
        }
    }

    private void regis() {

        nmL=(EditText)findViewById(R.id.nm_reg);
        eml=(EditText)findViewById(R.id.email_reg);
        pw=(EditText)findViewById(R.id.pass_reg);
        pw2=(EditText)findViewById(R.id.pass2_reg);
        notelp=(EditText)findViewById(R.id.notelp_reg);

        group=(RadioGroup)findViewById(R.id.radioGroup);

        //untuk set radio pilihan group radio button
        int selectedID = group.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedID);

        Log.d("radio button","===>"+selectedID+" ,"+radioButton);

        final String nm=nmL.getText().toString();
        final String email=eml.getText().toString();
        final String psw=pw.getText().toString();
        final String psw2=pw2.getText().toString();
        final String jk=radioButton.getText().toString();
        final String notlp=notelp.getText().toString();

        if((nm.length())<=0 || nm==null || email.length()<=0 || email==null||psw.length()<=0 || psw==null||psw2.length()<=0 || psw2==null||notlp.length()<=0 || notlp==null){
            notif("Data masih kosong.", 1);
        }// onClick of button perform this simplest code.
        else if (!email.matches(emailPattern)) {
            notif("Email address tidak benar", 1);
        } else if(notlp.length()<12){
            notif("Nomor kurang lengkap", 1);
        }else if (psw.length() < 8) {
            notif("Password minimal 8 karakter.", 1);
        }else if (notlp.startsWith("628")) {
            notif("Nomor telpon kurang lengkap.", 1);
        }else if (!notlp.startsWith("08")) {
            notif("Nomor telpon tidak di kenal.", 1);
        }else if(! psw.equals(psw2)){
            // ! psw.equals() artinya jika psw tidak sama
            notif("Password dan Konfirmasi password harus sama.", 0);
        }else{
            starLoading();
            //parameter untuk endpoint
            HashMap<String, String > params = new HashMap<String, String>();
            params.put("email",email);
            params.put("nama",nm);
            params.put("password",psw);
            params.put("notelp",notlp);
            params.put("jk",jk);
            params.put("token_firebase", mtokenfirebase);

            /////////////////////Request post method
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("response server","====>"+response);
                    try {
                        JSONObject jo = new JSONObject(String.valueOf(response));
                        final String code, msg, token;
                        code = jo.getString("status");

                        Log.d("code",":"+code);

                        if(code.equals("false")){
                            notif(jo.getString("msg"), 1);

                        }else if(code.equals("true")){
                            token = jo.getString("token");
//                            get jsonArray
                            JSONArray jsonArray = jo.getJSONArray("data");

                            for( int i=0; i<jsonArray.length(); i++){
//                                get JSONObject
                                JSONObject jo1 = jsonArray.getJSONObject(i);

                                builder.setTitle("Notification.");
                                builder.setMessage(jo.getString("msg"));
                                builder.setCancelable(false);

                                //get information user from PHP
                                mid= jo1.getString("id");
                                mnama= jo1.getString("name");
                                memail = jo1.getString("email");
                                mjk = jo1.getString("jk");
                                mnotelp = jo1.getString("notelp");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        insertSharedPrefrence();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    stopLoading();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    stopLoading();
                    notif("Gagal terhubung ke server.", 1);
                }
            });
            VolleySingleton.getmInstance(Register.this).addToRequestque(jsonObjectRequest);
        }
    }

    private void notif(String pesan, int code) {
        switch (code) {
            case 0:
                builder.setTitle("Notification.");
                builder.setMessage(pesan);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pw.setText(null);
                        pw2.setText(null);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case 1:
                builder1.setMessage(pesan);
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                break;
        }
    }

    private void getTokenFireBase() {
        //getTokenFirebase
        mtokenfirebase = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token firebase","====>"+mtokenfirebase);
    }

    private void insertSharedPrefrence(){

        session.createUserLoginSession(mid, mnama, memail, mnotelp, mjk, mtoken, mtokenfirebase);
        // Starting MainActivity using setFlag untuk gk bisa masuk lagi ke register setelah selesai
        Intent i = new Intent(Register.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void starLoading() {
        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(Register.this);
            mProgressDialog.setMessage("Please wait..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void stopLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }



}
