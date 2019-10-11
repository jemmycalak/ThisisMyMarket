package com.jemmy.calak.camart.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.util.formatNominal;
import com.jemmy.calak.camart.util.userSharedPreference;
import com.jemmy.calak.camart.util.SQLite;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DetailProduct extends AppCompatActivity {

    private SQLite db;
    private Context c;
    private TextView titel, desc, hrga, brt, clr, stocks;
    private Button order, cart;
    private ImageView img;
    private String imageUrls, namas, descs, clrs, id_product,brts,hrgs, stock;
    private int id_user, jmlh =1;

    //session management user to get id_user
    private userSharedPreference session;

    private AlertDialog.Builder builder;
    private Snackbar snackbar;
    private RelativeLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laptop);
        session = new userSharedPreference(getApplicationContext());
        db= new SQLite(this);
        //for alert
        builder= new AlertDialog.Builder(DetailProduct.this);
        coordinatorLayout = (RelativeLayout)findViewById(R.id.coorDetail);
        //setoolbar
        setToolbar();

        showdata();

        order=(Button)findViewById(R.id.order);
        cart=(Button)findViewById(R.id.addcart);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();

            }
        });

        //untuk set tombol order di detailactivity.java
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order();
            }
        });

    }

    private void Order() {
        //check user sudah Login atau belum
        if(session.checkLogin()) {
            notif("Opps anda belum login");
            snackbar.setActionTextColor(Color.CYAN);
            snackbar.setAction("LOGIN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DetailProduct.this, Login.class));
                }
            });
        }else {
            if(callAddCart()){
                startActivity(new Intent(DetailProduct.this, Keranjang.class));
            }else{
                startActivity(new Intent(DetailProduct.this, Keranjang.class));
            }
        }
    }

    private void getUserId(){
        //to get id_user
        HashMap<String, String> hashMap = session.getUserDetail();
        id_user = Integer.valueOf(hashMap.get(session.KEY_ID));
    }

    private void addToCart() {
        if(session.checkLogin()){
            notif("Opps anda belum login");
            snackbar.setActionTextColor(Color.CYAN);
            snackbar.setAction("LOGIN", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DetailProduct.this, Login.class));
                }
            });
        }else{
            if (callAddCart()){
                notif("Jumlah keranjang bertambah");
            }else{
                notif("Berhasil menambahkan keranjang");
            }
        }
    }

    private Boolean callAddCart() {
        Boolean stts=true;
        getUserId();
        Cursor cursor = db.checkStockDataById(id_user, id_product);
        if(cursor.moveToNext()){
            int db_qty = db.getQtyData(id_user, id_product);
            Log.d("qwty",""+db_qty);
            if(db.updateJmlhData(id_user, id_product, jmlh+db_qty)){
                stts = true;
            }
        }else{
            if(db.insert(id_user, id_product, jmlh, hrgs, namas, imageUrls, clrs)){
                stts = false;
            }
        }
        return stts;
    }

    public void setToolbar(){
        getSupportActionBar().setTitle("Detail Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void showdata(){
        img=(ImageView)findViewById(R.id.img2);

        titel=(TextView)findViewById(R.id.title);
        desc=(TextView)findViewById(R.id.descript);
        hrga=(TextView)findViewById(R.id.hrg);
        brt=(TextView)findViewById(R.id.berat);
        clr=(TextView)findViewById(R.id.color);
        stocks = (TextView)findViewById(R.id.stock);

        Intent i=getIntent();
        id_product=i.getExtras().getString("id_k"); //ambil id dari put extras
        imageUrls=i.getExtras().getString("image_k");
        namas=i.getExtras().getString("nama_k");
        descs=i.getExtras().getString("desc_k");
        hrgs=i.getExtras().getString("hrg_k");
        brts=i.getExtras().getString("brt_k");
        clrs=i.getExtras().getString("clr_k");
        stock = i.getExtras().getString("stock");

        String brat=String.valueOf(brts); //konversi double to String (Harus di konversi dulu)
        String hrgas=String.valueOf(hrgs);

        formatNominal formatNominal = new formatNominal();

        //letakan data
        titel.setText(namas.toUpperCase());
        desc.setText(": "+descs);
        hrga.setText(": Rp."+formatNominal.formatNumber(Integer.parseInt(hrgas))+",-");
        brt.setText(": "+brat);
        clr.setText(": "+clrs);
        stocks.setText(": "+stock.toUpperCase());

        //untuk gambarnya
        //picasso_client.downloadImage(this, imageUrls, img);
        Picasso.with(c).load(imageUrls).into(img);
    }

    public void notif(String pesan){
        snackbar = Snackbar.make(coordinatorLayout, pesan, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView sbText = (TextView)sbView.findViewById(android.support.design.R.id.snackbar_text);
        sbText.setTextColor(Color.WHITE);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
