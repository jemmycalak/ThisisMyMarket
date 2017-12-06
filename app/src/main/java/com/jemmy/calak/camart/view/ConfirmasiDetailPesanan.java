package com.example.jemmycalak.thisismymarket.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jemmycalak.thisismymarket.Adapter.ConfirmOrderProductAdapter;
import com.example.jemmycalak.thisismymarket.Model.object_address;
import com.example.jemmycalak.thisismymarket.Model.object_bank;
import com.example.jemmycalak.thisismymarket.Model.object_product;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.Random_payment;
import com.example.jemmycalak.thisismymarket.util.formatNominal;
import com.example.jemmycalak.thisismymarket.util.userSharedPreference;
import com.example.jemmycalak.thisismymarket.util.API;
import com.example.jemmycalak.thisismymarket.util.SQLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfirmasiDetailPesanan extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView penerima, alamat, province, notelp, norek, atasnama, biayakirim, total;
    private Button btConfirm;
    private SQLite db;
    private List<object_product> dataProduct;
    private userSharedPreference session;
    private String id_user, id_bank, shipping, mtotal, id_address, mNorek, mAtasnama;
    private ArrayList id_barang= new ArrayList();
    private ArrayList jumlah = new ArrayList();
    private int totals;
    private API api;
    private AlertDialog.Builder builder, builder1, builder2;
    private formatNominal formatNominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmasi_detail_pesanan);
        api = new API();
        formatNominal = new formatNominal();

        db = new SQLite(getApplicationContext());
        session = new userSharedPreference(getApplicationContext());
        HashMap<String, String> hashMap = session.getUserDetail();
        id_user = hashMap.get(session.KEY_ID);

        init();
        setolbar();
        getDataBank();
        getDataPesanan();
        getDataAddress();
    }

    public void setolbar() {
        getSupportActionBar().setTitle("Konfirmasi Detail Pesanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        recyclerView = (RecyclerView)findViewById(R.id.recyclerConfirmasi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        penerima = (TextView)findViewById(R.id.penerimaConfirm);
        alamat = (TextView)findViewById(R.id.alamatConfirm);
        province = (TextView)findViewById(R.id.provinsiConfirm);
        notelp = (TextView)findViewById(R.id.notelpConfirm);
        norek = (TextView)findViewById(R.id.norekConfirm);
        atasnama = (TextView)findViewById(R.id.atasnamaConfirm);
        biayakirim = (TextView)findViewById(R.id.biayaKirimConfirm);
        total = (TextView)findViewById(R.id.totalConfirm);
        btConfirm = (Button)findViewById(R.id.btConfirm);
        builder = new AlertDialog.Builder(this);
        builder1 = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataOrder();
//                startActivity(new Intent(ConfirmasiDetailPesanan.this, finish.class));
            }
        });

    }

    public void notif(String pesan, int code){
        switch(code){
            case 0:
                builder.setMessage(pesan);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case 1:
                builder.setMessage(pesan);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        }
    }

    public void getDataBank(){
        List<object_bank> dataBank = db.getBank(id_user);

        for(object_bank ob : dataBank){
            atasnama.setText(": "+ob.getAtasnam());
            norek.setText(": "+ob.getBank()+" - "+ob.getNorek());
            id_bank = String.valueOf(ob.getId());
            mNorek= ob.getNorek();
            mAtasnama = ob.getAtasnam();
        }
    }

    public void getDataPesanan(){
        dataProduct = db.getAllDataCartById(Integer.parseInt(id_user));
        for(object_product ob : dataProduct){
            totals += Integer.valueOf(ob.getJmlh()) * Integer.valueOf(ob.getHrg());

            //add data to array
            Log.d("id_produck","=====>"+ob.getId());
            id_barang.add(ob.getId());
            jumlah.add(ob.getJmlh());
        }
        //get angka random
        Random_payment ra = new Random_payment();
        int angkarandom = ra.angka();

        int mbiayakirim = (int) (totals * 0.01);
        int hitung_total = totals + mbiayakirim + angkarandom;

        Log.d("angka random","===>"+angkarandom);

        mtotal = String.valueOf(hitung_total);
        shipping = String.valueOf(mbiayakirim);
        biayakirim.setText("Biaya kirim : Rp. "+formatNominal.formatNumber(Integer.parseInt(shipping))+",-");
        total.setText("Total : Rp. "+formatNominal.formatNumber(hitung_total)+",-");


        //set data product
        ConfirmOrderProductAdapter adapter = new ConfirmOrderProductAdapter(getApplicationContext(), dataProduct);
        recyclerView.setAdapter(adapter);
    }

    public void getDataAddress(){
        List<object_address> dataAddress = db.getAddress(Integer.valueOf(id_user));
        for(object_address ob : dataAddress){
            penerima.setText(": "+ob.getName());
            alamat.setText(": "+ob.getAddress());
            province.setText(": "+ob.getProvinsi());
            notelp.setText(": "+String.valueOf(ob.getNotelp()));
            id_address = String.valueOf(ob.getId());
        }
    }

    private void sendDataOrder(){

        for(int i=0; i<id_barang.size(); i++){
                Log.d("id_brang","====>"+id_barang.get(i));
        }

        api.sendDataOrder(id_user, id_bank, shipping, mtotal, id_address, dataProduct, this);
    }
    public void goFinish(){

        Intent i = new Intent(ConfirmasiDetailPesanan.this, finish.class);
        i.putExtra("norek", mNorek);
        i.putExtra("atasnama", mAtasnama);
        i.putExtra("nominal", mtotal);
        startActivity(i);
    }

    public void removeSqlite(){
        db.deleteAllProduct(Integer.parseInt(id_user));
        db.deleteAllAddress(Integer.parseInt(id_user));
        db.deleteAllBank(Integer.parseInt(id_user));
    }
}
