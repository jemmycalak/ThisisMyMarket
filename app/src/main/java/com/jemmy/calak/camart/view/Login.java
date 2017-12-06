package com.example.jemmycalak.thisismymarket.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jemmycalak.thisismymarket.Config;
import com.example.jemmycalak.thisismymarket.Model.object_user;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.userSharedPreference;
import com.example.jemmycalak.thisismymarket.util.VolleySingleton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 100;
    private Uri photoUri;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String namaG, emailG, imageG, token_firebase;

    private Toolbar tolbar;
    private Button login, dftr;
    private EditText usernameET, pwET;
    private ProgressDialog mProgressDialog;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    private AlertDialog.Builder builder, builder1;
    private String url = Config.app_url_api + "users/login";

    //sharef preferens
    private userSharedPreference session;
    private object_user objectUser;
    private static final String TAG = Login.class.getSimpleName();
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressDialog = new ProgressDialog(this);

        ///call shared preferens
        session = new userSharedPreference(getApplicationContext());
        token_firebase = FirebaseInstanceId.getInstance().getToken();
        Log.d("new Token firebase","===>"+token_firebase);
        setolbar();
        init();

    }

    private void init() {

        usernameET = (EditText) findViewById(R.id.user1);
        pwET = (EditText) findViewById(R.id.pasword1);


        builder = new AlertDialog.Builder(Login.this);
        builder1 = new AlertDialog.Builder(Login.this);

        login = (Button) findViewById(R.id.login);
        dftr = (Button) findViewById(R.id.dftr);
        login.setOnClickListener(this);
        dftr.setOnClickListener(this);
    }

    private void setolbar() {
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                loggginManual();
                break;
            case R.id.dftr:
                startActivity(new Intent(Login.this, Register.class));
                break;
        }

    }

    private void loggginManual() {

        //Getting values from edit texts
        final String user = usernameET.getText().toString().trim();
        final String pw = pwET.getText().toString().trim();

        if ((user.length()) <= 0 || user == null || pw.length() <= 0 || pw == null) {
            builder.setTitle("Notification");
            notif("Masukan data dengan benar.", 0);
        }else if (!user.matches(emailPattern)) {
            notif("Email address tidak benar", 1);
        }else {
            starLoading();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", user);
            params.put("password", pw);
            params.put("token_firebase", token_firebase);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("response", ":" + response);
//                            Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();

                            try {
                                JSONObject jo = new JSONObject(String.valueOf(response));
                                String code = jo.getString("success");
                                String msg = jo.getString("msg");

//                                Log.d("data",":"+code+" ,"+msg);

                                if (code.equals("false")) {
                                    builder.setTitle("Login Gagal.");
                                    notif(jo.getString("msg"), 0);
                                } else if (code.equals("true")) {

                                    String id, nm, email, jk, notelp, token;
                                    //ini untuk ambil token dari session
                                    token = jo.getString("token");


                                    JSONArray jsonArray = jo.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        new JSONObject
                                        JSONObject jo1 = jsonArray.getJSONObject(i);

//                                        Log.d("jo1",":"+jo1);
                                        //get information user from API
                                        id = jo1.getString("id");
                                        nm = jo1.getString("name");
                                        email = jo1.getString("email");
                                        jk = jo1.getString("jk");
                                        notelp = jo1.getString("notelp");
                                        String token_firebase1 = jo1.getString("token_firebase");

                                        // masukan data ke shared preferens
                                        session.createUserLoginSession(id, nm, email, notelp, jk, token, token_firebase1);
                                        finish();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                stopLoading();
                            }
                            stopLoading();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    stopLoading();
                    Toast.makeText(Login.this, "Trouble from request data.", Toast.LENGTH_SHORT).show();
                }
            }) {
                //                /////////////////////////////////////REQUEST HEADER////////////////////////////////////////////
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map reqHeader = new HashMap();
                    reqHeader.put("API-KEY", "jemmy_calak_api_key");

                    return reqHeader;
                }
            };
            VolleySingleton.getmInstance(Login.this).addToRequestque(jsonObjectRequest);
        }
    }

    public void notif(String pesan, int code) {
        switch (code) {
            case 0:
                builder.setMessage(pesan);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usernameET.setText("");
                        pwET.setText("");
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


    private void starLoading() {
        if (mProgressDialog == null) {
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
