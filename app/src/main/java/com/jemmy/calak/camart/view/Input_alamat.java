package com.jemmy.calak.camart.view;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jemmy.calak.camart.Config;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.util.userSharedPreference;
import com.jemmy.calak.camart.util.API;

import java.util.HashMap;

public class Input_alamat extends AppCompatActivity implements View.OnClickListener {


    private EditText pnrma, almt, provinsi, notelp;
    private Button tambah;

    private AlertDialog.Builder builder, builder1, builder2;
    final String url = Config.app_url_api + "/action/add_address.php";

    //session management user to get id_user
    userSharedPreference session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_alamt);

        //to get id_user
        session = new userSharedPreference(getApplicationContext());

        settolbar();

        pnrma = (EditText) findViewById(R.id.namapenerima_detail);
        almt = (EditText) findViewById(R.id.almt_detail);
        notelp = (EditText) findViewById(R.id.notelp_detail);
        provinsi = (EditText) findViewById(R.id.provinsi);

        builder = new AlertDialog.Builder(Input_alamat.this);
        builder1 = new AlertDialog.Builder(Input_alamat.this);
        builder2 = new AlertDialog.Builder(Input_alamat.this);

        tambah = (Button) findViewById(R.id.tambahalamat);
        tambah.setOnClickListener(this);

    }

    public void settolbar() {
        getSupportActionBar().setTitle("Tambah Alamat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void tambah() {
        //alamat penerima
        final String pnrma_o = pnrma.getText().toString();
        final String almt_o = almt.getText().toString();
        final String provinsi_o = provinsi.getText().toString();
        final String notelp_o = notelp.getText().toString();

        //for get id user
        HashMap<String, String> user = session.getUserDetail();
        final String id_user = user.get(userSharedPreference.KEY_ID);

        if (((pnrma_o.length()) <= 0 || (almt_o.length()) <= 0 || (notelp_o.length()) <= 0 || provinsi_o.equals(""))) {
            notif("Masih ada field yang kosong.", 1);
        }else if(notelp_o.length()<12){
            notif("Nomor kurang lengkap", 1);
        }else if (notelp_o.startsWith("628")) {
            notif("Nomor telpon kurang lengkap.", 1);
        }else if (!notelp_o.startsWith("08")) {
            notif("Nomor telpon tidak di kenal.", 1);
        }
        else {
            //////////////////////////////////////////SEND DATA///////////////////////////////////////////
            API api = new API();
            api.newAddress(this, id_user, pnrma_o, almt_o, provinsi_o, notelp_o);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tambahalamat:
                tambah();
                //Toast.makeText(Input_alamat.this, "asss", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        pnrma.setText(null);
        almt.setText(null);
        notelp.setText(null);
        provinsi.setText(null);
    }

    public void notif(String pesan, int code) {
        switch (code) {
            case 0:
                builder.setTitle("Notification.");
                builder.setMessage(pesan);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
            case 2:
                builder2.setTitle(pesan);
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog2 = builder2.create();
                alertDialog2.show();

                break;
        }
    }
}
