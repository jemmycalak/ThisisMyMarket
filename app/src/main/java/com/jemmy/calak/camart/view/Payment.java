package com.example.jemmycalak.thisismymarket.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jemmycalak.thisismymarket.Adapter.PaymentAdapter;
import com.example.jemmycalak.thisismymarket.Config;
import com.example.jemmycalak.thisismymarket.Model.object_bank;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.userSharedPreference;
import com.example.jemmycalak.thisismymarket.util.SQLite;
import com.example.jemmycalak.thisismymarket.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Payment extends AppCompatActivity implements View.OnClickListener{

    private CheckBox checkBox;
    private Button konfirm;
    private String pembayaran;

//    String url = "http://192.168.43.117/db_m_market_localhost/action/order.php";
    private String url =Config.app_url_api+"banks";

    private AlertDialog.Builder builder;
    private ArrayList<object_bank> arrayBank = new ArrayList<>();
    private object_bank objectBank;
    private ListView listbank;
    private int id_user;
    //bject_user ob = new object_user();

    private userSharedPreference session;
    private SQLite db;
    private Boolean visible=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        builder = new AlertDialog.Builder(Payment.this);
        db = new SQLite(getApplicationContext());

        //for sharedPreference
        session= new userSharedPreference(getApplicationContext());

        init();
        getData();
        settolbar();
    }

    private void init() {
        HashMap<String, String> hashMap = session.getUserDetail();
        id_user = Integer.valueOf(hashMap.get(session.KEY_ID));

        listbank = (ListView)findViewById(R.id.list_bank);
        konfirm=(Button)findViewById(R.id.konfirm);
        konfirm.setOnClickListener(this);
        setVisible(visible);
    }

    public void getData(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("data bank","====>"+response);
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        Log.d("data bank","====>"+jo);
                        Log.d("data bank","====>"+jo.getString("norek"));

                        int id_bank     = Integer.valueOf(jo.getString("id"));
                        String nm_bank  = jo.getString("nm_bank");
                        String img_bank = jo.getString("img_bank");
                        String nork     = jo.getString("norek");
                        String atasnama = jo.getString("atasnama");

                        objectBank  = new object_bank();
                        objectBank.setId(id_bank);
                        objectBank.setBank(nm_bank);
                        objectBank.setNorek(nork);
                        objectBank.setImg_bank(img_bank);
                        objectBank.setAtasnama(atasnama);

                        arrayBank.add(objectBank);

                    }
                    PaymentAdapter bankAdapter = new PaymentAdapter(Payment.this, getApplicationContext(), arrayBank);
                    listbank.setAdapter(bankAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Payment.this, "Trouble request data.", Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getmInstance(Payment.this).addToRequestque(jsonObjectRequest);
    }
    public void settolbar(){
        getSupportActionBar().setTitle("Pilih Pembayaran");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.konfirm:
                //Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Payment.this, ConfirmasiDetailPesanan.class));
                break;
        }
    }

    public void saveData( int id_bank, String nm_bank, String atasnama, String norek){
        try{

            Cursor cursor = db.checkBank(id_user);
            if(cursor.getCount() > 0){
                db.updateBank(id_user, id_bank, nm_bank, norek, atasnama);
            }else{
                db.insertBank(id_user, id_bank, nm_bank, norek, atasnama);
            }
        }catch(Exception e){
            Log.d("Error","save bank ====> "+e);
        }
    }

    public void displayAlert(String notif){
        builder.setMessage(notif);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setVisible(Boolean vis){
        visible = vis;
        try{
            if(visible){
                konfirm.setClickable(visible);
                konfirm.setBackgroundColor(Color.parseColor("#b71c1c"));
            }else{
                konfirm.setClickable(false);
                konfirm.setBackgroundColor(Color.parseColor("#c0bbbc"));
            }
        }catch (Exception e){
            Log.d("Error visible checkbox","");
        }
    }

}
