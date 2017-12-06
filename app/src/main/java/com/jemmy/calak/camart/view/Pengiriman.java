package com.example.jemmycalak.thisismymarket.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jemmycalak.thisismymarket.Adapter.PengirimanAdapter;
import com.example.jemmycalak.thisismymarket.Config;
import com.example.jemmycalak.thisismymarket.Model.object_address;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.userSharedPreference;
import com.example.jemmycalak.thisismymarket.util.SQLite;
import com.example.jemmycalak.thisismymarket.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Pengiriman extends AppCompatActivity {
    private Button pembayaran;
    private TextView tambah;

    private final String url = Config.app_url_api+"users/address";
    private ListView list_alamat;
    private object_address object_addres;
    private ArrayList<object_address> arrayAddress = new ArrayList<>();
    private PengirimanAdapter addressAdapter;
    private userSharedPreference userSharedPreference;
    private int id_user;
    private int id_addrs;
    private SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengiriman);

        db = new SQLite(getApplicationContext());
        //get userId
        userSharedPreference = new userSharedPreference(getApplicationContext());
        HashMap<String, String> hashMap = userSharedPreference.getUserDetail();
        id_user = Integer.valueOf(hashMap.get(userSharedPreference.KEY_ID));
        init();
        setToolbar();
        getData();
        pembayaran.setClickable(false);
    }

    private void init() {
        pembayaran = (Button)findViewById(R.id.next);
        tambah = (TextView)findViewById(R.id.tmbh);
        list_alamat = (ListView)findViewById(R.id.list_alamat);
        setVisibleLayout(false);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Pengiriman.this, Input_alamat.class));
            }
        });
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Pengiriman.this, Payment.class));
            }
        });
    }

    private void getData() {

//        make a parameter request
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id_user", String.valueOf(id_user));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);

                        int id_address = Integer.valueOf(jo.getString("id"));
                        String alamat = jo.getString("alamat");
                        String penerima = jo.getString("nm_penerima");
                        String notelp   = jo.getString("notelp_penerima");
                        String province = jo.getString("province");

                        object_addres = new object_address();
                        object_addres.setId(id_address);
                        object_addres.setAddress(alamat);
                        object_addres.setName(penerima);
                        object_addres.setNotelp(notelp);
                        object_addres.setProvinsi(province);

                        arrayAddress.add(object_addres);
                    }

                    addressAdapter = new PengirimanAdapter(Pengiriman.this, getApplicationContext(), arrayAddress);
                    list_alamat.setAdapter(addressAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Pengiriman.this, "Trouble request data.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getmInstance(Pengiriman.this).addToRequestque(jsonObjectRequest);
    }

    public void setToolbar(){
        getSupportActionBar().setTitle("Tujuan Pengiriman");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setVisibleLayout(Boolean vis){
        try {
            if(vis) {
                pembayaran.setClickable(true);
                pembayaran.setBackgroundColor(Color.parseColor("#b71c1c"));
            } else {
                pembayaran.setClickable(false);
                pembayaran.setBackgroundColor(Color.parseColor("#c0bbbc"));
            }
        }catch(Exception e){
            Log.d("Error visible checkbox","");
        }
    }

    public void dataAddres(int id_address, String penerima, String almt, String provinse, String notelp){
        id_addrs = id_address;
        Log.d("Data address ","====>"+id_address+" "+penerima+" "+almt+" "+ provinse+" "+ notelp);

        try {
            Cursor cursor =db.checkAddres(id_user);
            if(cursor.getCount() > 0){
                db.updateAddress(id_user, id_address, penerima, almt, provinse, notelp);
            }else{
                db.insertAddres(id_user, id_address, penerima, almt, provinse, notelp);
            }

        }catch(Exception e){
            Log.d("Error address",""+e);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Log ===>","onRestart");
        setVisibleLayout(false);

        //refresh list when finished add new address
        getData();
        addressAdapter.refreshAdapter(arrayAddress);
    }

}
