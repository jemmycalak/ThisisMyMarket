package com.example.jemmycalak.thisismymarket.view;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.API;
import com.example.jemmycalak.thisismymarket.util.userSharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private EditText name, email, lastPassword, newPassword, notelp;
    private Button save;
    private RadioGroup group;
    private RadioButton jkRB;
    private userSharedPreference session;
    private String mname, memail, mlastpassword, mnewpassword, mnotelp, mjk, idUser, mtoken, token_firebase;
    private AlertDialog.Builder builder, builder1, builder2;
    private API api;
    private int jkSelected ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        getDataUser();
    }

    private void getDataUser() {

        session = new userSharedPreference(getApplicationContext());
        HashMap<String, String> hashMap = session.getUserDetail();
        idUser = hashMap.get(userSharedPreference.KEY_ID);
        mtoken = hashMap.get(userSharedPreference.KEY_TOKEN_SESSION);
        token_firebase = FirebaseInstanceId.getInstance().getToken();

        name.setText(hashMap.get(userSharedPreference.KEY_NAME));
        email.setText(hashMap.get(userSharedPreference.KEY_EMAIL));
        notelp.setText(hashMap.get(userSharedPreference.KEY_NOPE));

        //set values radiogroup button
        String gender = hashMap.get(userSharedPreference.KEY_JK);
        group.check(gender.equals("Laki-Laki") ? R.id.lakiProfile : R.id.wanitaProfile);

    }

    private void init() {

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        api = new API();

        builder = new AlertDialog.Builder(this);
        builder1 = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);

        name = (EditText)findViewById(R.id.nameProfile);
        email = (EditText)findViewById(R.id.emailProfile);
        lastPassword = (EditText)findViewById(R.id.lastpasswordProfile);
        newPassword = (EditText)findViewById(R.id.newpasswordProfile);
        notelp = (EditText)findViewById(R.id.notelpProfile);

        group = (RadioGroup)findViewById(R.id.groupProfile);

        save = (Button)findViewById(R.id.saveProfile);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jkSelected = group.getCheckedRadioButtonId();
                jkRB = (RadioButton)findViewById(jkSelected);

                checkData();
//                Log.d("radio button","===>"+jkRB.getText().toString()+", "+jkSelected);
            }
        });

    }

    private void checkData() {

        mname = name.getText().toString();
        memail = email.getText().toString();
        mlastpassword = lastPassword.getText().toString();
        mnewpassword = newPassword.getText().toString();
        mnotelp = notelp.getText().toString();
        mjk = jkRB.getText().toString();

        if(mname.equals("")||mname.equals(null)||
                memail.equals("")||memail.equals(null)||
                mlastpassword.equals("")||mlastpassword.equals(null)||
                mnewpassword.equals("")||mnewpassword.equals(null)||
                mnotelp.equals("")||mnotelp.equals(null)){
            notif("Masih ada data yang kosong", 0);
        }else{
            notif("Apakah data anda sudah benar ?", 1);
        }

    }

    public void notif(String pesan, int code){
        switch (code){
            case 0:
                builder.setMessage(pesan);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case 1:
                builder1.setMessage(pesan);
                builder1.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveData();
                    }
                });
                builder1.setNegativeButton("Belum", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder2.setCancelable(false);
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                break;
            case 2:
                builder2.setMessage(pesan);
                builder2.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        newPassword.setText("");
                        lastPassword.setText("");
                    }
                });
                builder2.setNegativeButton("Belum", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder2.setCancelable(false);
                AlertDialog alertDialog2 = builder2.create();
                alertDialog2.show();

                break;
        }
    }

    private void saveData() {
        api.updateProfile(idUser, mname, memail, mnotelp, mjk, mlastpassword, mnewpassword, ProfileActivity.this);
    }

    public void updateSharedPreferebce(String idUser, String mname, String memail, String mnotelp, String mjk){

        session.createUserLoginSession(idUser, mname, memail,mnotelp, mjk, mtoken, token_firebase);
    }
}
