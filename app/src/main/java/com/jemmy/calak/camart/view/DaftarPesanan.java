package com.jemmy.calak.camart.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jemmy.calak.camart.Adapter.DaftarPesananAdapter;
//import com.example.jemmycalak.thisismymarket.Adapter.OrderItemAdapter;
import com.jemmy.calak.camart.Config;
import com.jemmy.calak.camart.Model.object_order;
import com.jemmy.calak.camart.Model.object_product;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.util.formatNominal;
import com.jemmy.calak.camart.util.userSharedPreference;
import com.jemmy.calak.camart.util.API;
import com.jemmy.calak.camart.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DaftarPesanan extends AppCompatActivity {

    private AppCompatButton logout;
    private TextView emailTV, namaTV, jkTV, notelpTV, alamatTV;
    private String email, nm, jk, notelp;
    private CoordinatorLayout coordinatorLayout;
    private final String url = Config.app_url_api + "products/orderdetail";

    private ArrayList<object_order> arraysectionDataOrder;

    private RecyclerView recyclerView;
    private object_order or;
    //session userManagerment
    private userSharedPreference session;
    private String id_user;
    private AlertDialog.Builder builder;
    private API api;
    private DaftarPesananAdapter orderAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

        //session management
        session = new userSharedPreference(getApplicationContext());
        api = new API();

        arraysectionDataOrder = new ArrayList<object_order>();
        init();
        getDataUser();
        getDataOrder();
        initToolbar();

    }

    private void init() {
        //init RecycleView
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_pn);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.act_pesan);
        builder = new AlertDialog.Builder(DaftarPesanan.this);
    }

    private void getDataOrder() {

        api.startLoad(DaftarPesanan.this, "Please wait..");

        //parameter endpoint
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id_user", id_user);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Status", "====>" + response);
                try {
                    String code;
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    code = jsonObject.getString("status");

                    if (code.equals("false")) {
                        snackbar("Pesanan anda masih kosong.", 0);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);

                            Log.d("dataJson", "=====>" + jo);

                            int id_order = Integer.valueOf(jo.getString("id_order"));
                            String no_invoice = jo.getString("no_invoice");
                            String shipping = jo.getString("shipping");
                            String stts_order = String.valueOf(jo.getString("stts_order"));
                            String total = jo.getString("total_order");
                            String tgl_order = jo.getString("tgl_order");
                            String isVisible = jo.getString("isVisible");

                            String payment_to = jo.getString("payment_to");
                            String norek = jo.getString("payment_to_rek");


                            String penerima = jo.getString("penerima");
                            String province = jo.getString("province");
                            String almt_penerima = jo.getString("almt_penerima");
                            String notelp_penerima = jo.getString("notelp_penerima");

                            // insert to object_order
                            or = new object_order();
                            or.setId_order(id_order);
                            or.setNo_invoice(no_invoice);
                            or.setShipping(shipping);
                            or.setStatus_order(stts_order);
                            or.setTotal(total);
                            or.setTgl_order(tgl_order);
                            or.setIsVisible(isVisible);
                            Log.d("data order ", ": " + or.getId_order());

//                        insert to object_bank
                            or.setBank(payment_to);
                            or.setNorek(norek);

//                        insert to object_address
//                        or = new object_address();
                            or.setNm_penerima(penerima);
                            or.setProvince(province);
                            or.setAlmt(almt_penerima);
                            or.setNotelp(notelp_penerima);


                            ArrayList<object_product> arrayProduct = new ArrayList<object_product>();
                            JSONArray jsonArray1 = jo.getJSONArray("order_detail");
                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jo1 = jsonArray1.getJSONObject(j);
                                JSONObject attribute = jo1.getJSONObject("attributes");
                                Log.d("data attribute ", ": " + attribute);

                                JSONObject jsonObject1 = attribute.getJSONObject("rel_product");
                                JSONObject data = jsonObject1.getJSONObject("data");
                                JSONObject data1 = data.getJSONObject("attributes");

                                String product_id = data.getString("id");
                                String product_nm = data1.getString("product_nm");
                                String product_color = data1.getString("product_color");
                                String product_img = data1.getString("product_img");
                                String product_price = data1.getString("product_price");
                                String jml_product = attribute.getString("jml_product");

                                formatNominal formatNominal = new formatNominal();

                                //insert to object_product
                                object_product op = new object_product();
                                op.setId(product_id);
                                op.setNama(product_nm);
                                op.setHrg(product_price);
                                op.setColor(product_color);
                                op.setImgUrl(product_img);
                                op.setJmlh(jml_product);

                                arrayProduct.add(op);
                            }
                            or.setArraysectionProduct(arrayProduct);
                            arraysectionDataOrder.add(or);
                        }
                        orderAdapter = new DaftarPesananAdapter(getApplicationContext(), DaftarPesanan.this, arraysectionDataOrder, id_user);
                        recyclerView.setAdapter(orderAdapter);
                    }

                    api.stopLoad(DaftarPesanan.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                    api.stopLoad(DaftarPesanan.this);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                api.stopLoad(DaftarPesanan.this);
            }
        });

        VolleySingleton.getmInstance(DaftarPesanan.this).addToRequestque(jsonObjectRequest);

    }

    public void snackbar(String pesan, int code) {
        switch (code) {
            case 0:
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, pesan, Snackbar.LENGTH_INDEFINITE)
                        .setAction("BACK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onBackPressed();
                            }
                        });
                //change textColor Button
                snackbar.setActionTextColor(Color.CYAN);
                //change messageColor snackbar
                View sbView = snackbar.getView();
                TextView sbText = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                sbText.setTextColor(Color.WHITE);
                snackbar.show();
                break;
            case 1:
                Snackbar snackbar1 = Snackbar
                        .make(coordinatorLayout, pesan, Snackbar.LENGTH_LONG);
                //change textColor Button
                snackbar1.setActionTextColor(Color.CYAN);
                //change messageColor snackbar
                View sbView1 = snackbar1.getView();
                TextView sbText1 = (TextView) sbView1.findViewById(android.support.design.R.id.snackbar_text);
                sbText1.setTextColor(Color.RED);
                snackbar1.show();
                break;
        }

    }

    private Boolean isVisible(String isVisible) {
        Boolean isvisible;
        if (isVisible.equals("true")) {
            isvisible = true;
        } else {
            isvisible = false;
        }

        return isvisible;
    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Daftar Pesanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getDataUser() {

        //check user sudah Login atau belum
        if (session.checkLogin()){
            finish();
        }else{
            // get user data from session
            HashMap<String, String> hashMap = session.getUserDetail();
            id_user = hashMap.get(userSharedPreference.KEY_ID);
        }
    }

    public void notif(String pesan, int code, final String noinvoice) {
        switch (code) {
            case 0:
                builder.setMessage(pesan);
                builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        api.batalPesan(DaftarPesanan.this, noinvoice, id_user);
                    }
                });
                builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }

    public void refresh() {
        getDataOrder();
        orderAdapter.refreshAdapter(arraysectionDataOrder);
        snackbar("Pesanan di batalkan", 1);
    }


}
