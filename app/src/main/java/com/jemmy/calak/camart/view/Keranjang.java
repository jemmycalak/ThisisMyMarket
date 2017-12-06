package com.example.jemmycalak.thisismymarket.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jemmycalak.thisismymarket.Adapter.KeranjangAdapter;

import com.example.jemmycalak.thisismymarket.Config;
import com.example.jemmycalak.thisismymarket.Model.object_product;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.formatNominal;
import com.example.jemmycalak.thisismymarket.util.userSharedPreference;
import com.example.jemmycalak.thisismymarket.util.SQLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Keranjang extends AppCompatActivity {
    private Button checkout;
    private int value1; //for take values of numberpicker
    private String url = Config.app_url_api + "products/cart";
    private RelativeLayout coordinatorLayout;


    private ListView lv;
    private object_product ob;
    private List<object_product> arrayKeranjang = new ArrayList<>();
    private userSharedPreference userSharedPreference;
    private KeranjangAdapter adapter;
    private int id_user, total_keranjang;

    private SQLite db;
    private TextView textTotal;
    private formatNominal formatNominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        db = new SQLite(getApplicationContext());
        formatNominal = new formatNominal();
        init();

        //get userId
        userSharedPreference = new userSharedPreference(getApplicationContext());
        HashMap<String, String> hashMap = userSharedPreference.getUserDetail();
        id_user = Integer.valueOf(hashMap.get(userSharedPreference.KEY_ID));

        //settoolbat
        setToolbar();
        checkout = (Button) findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Keranjang.this, Pengiriman.class));
            }
        });

        getData();
    }

    private void init() {
        coordinatorLayout = (RelativeLayout) findViewById(R.id.coordinator_keranjang);
        lv = (ListView) findViewById(R.id.lv);
        textTotal = (TextView)findViewById(R.id.text_total);

    }


    public void setToolbar() {
        getSupportActionBar().setTitle("Keranjang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void snackbar() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Keranjang anda masih kosong", Snackbar.LENGTH_INDEFINITE)
                .setAction("BACK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
        //change textColor button
        snackbar.setActionTextColor(Color.CYAN);
        //change MessageColor snackbar
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void setVisibleCheckout() {
        checkout.setVisibility(View.INVISIBLE);
    }

    private void getData() {

        try{
            Cursor cursor = db.checkStockData(id_user);
            if(cursor.moveToNext()) {
                List<object_product> dataDb = db.getAllDataCartById(id_user);

                Log.d("data sqlite",":"+dataDb);
                //to get total keranjang
                for(object_product ob1 : dataDb){
                    total_keranjang += Integer.valueOf(ob1.getJmlh()) * Integer.valueOf(ob1.getHrg());
                }
                //start adapter
                adapter = new KeranjangAdapter(this, getApplicationContext(), dataDb);
                lv.setAdapter(adapter);

            }else{
                snackbar();
                setVisibleCheckout();
                setTotal(0);
            }
            //set total
            setTotal(total_keranjang);

        }catch(Exception e){
            Log.d("Error get data.","");
        }
    }

    public void setTotal(int total_keranjang) {
        if(total_keranjang == 0 ){
            textTotal.setVisibility(View.INVISIBLE);
        }else {
            textTotal.setText("Total : "+formatNominal.formatNumber(total_keranjang));
        }
    }

    public void updateTotal(int total) {
        try{
            String tot = String.valueOf(total);
            textTotal.setText("Total : "+formatNominal.formatNumber(Integer.parseInt(tot)));
            total_keranjang = total;
        }catch(Exception e){
            Log.d("Error updateTotal",""+e);
        }
    }

}
