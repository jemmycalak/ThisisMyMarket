package com.jemmy.calak.camart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jemmy.calak.camart.Adapter.ProductAdapter;
import com.jemmy.calak.camart.Config;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.Model.object_product;
import com.jemmy.calak.camart.interfacesComunicator.Comunicator;
import com.jemmy.calak.camart.util.API;
import com.jemmy.calak.camart.util.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProduct extends Fragment{

    private String url = Config.app_url_api + "products/products";
    private GridView gridView;

    private ArrayList<object_product> arrayProduk = new ArrayList<>();
    private ArrayList<object_product> arrayProdukFilter = new ArrayList<>();
    private object_product ob;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SQLite db;
    private Context context;
    private AlertDialog.Builder builder, builder1, builder2;
    private API api;
    private ProductAdapter gridViewAdapter;
    private Activity activity;

    //requaired searchBox
    private Comunicator comunicatorOne;

    public FragmentProduct(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_laptop_gridview, container, false);
        init(view);
        getData();

        return view;
    }

    public void init(View view) {
        db = new SQLite(getContext());
        api = new API();
        gridView = (GridView) view.findViewById(R.id.gv);
        gridView.setTextFilterEnabled(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swip);

        builder = new AlertDialog.Builder(getContext());
        builder1 = new AlertDialog.Builder(getContext());
        builder2 = new AlertDialog.Builder(getContext());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayProduk.clear();
                arrayProdukFilter.clear();
                getData();
            }
        });
    }

    public void getData() {

        api.startLoad(getContext(), "Please wait..");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Respon", ":" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));  ///get data from response
                            String code = jsonObject.getString("status");
                            if (code.equals("false")) {
                                notif("Tidak dapat mengambil data.", 0);
                            } else {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                                JSONArray jsonArray = jsonObject1.getJSONArray("data");                ///masukan data ke Array
                                if(jsonArray.length() > 0){
                                    for (int i = 0; i < jsonArray.length(); i++) {                  //looping arraynya

                                        JSONObject jo = jsonArray.getJSONObject(i);              //karena di dalam array ada object lagi maka di buat object JSONObject lagi
                                        int id = Integer.valueOf(jo.getString("id"));

                                        JSONObject jo2 = jo.getJSONObject("attributes");
                                        String name = jo2.getString("product_nm");
                                        int hrg = Integer.valueOf(jo2.getString("product_price"));
                                        String descrip = jo2.getString("product_desc");
                                        double brt = Double.valueOf(jo2.getDouble("product_weight"));
                                        String clr = jo2.getString("product_color");
                                        String imageUrl = jo2.getString("product_img");
                                        String stock = jo2.getString("product_stock");
                                        JSONObject jsonObject2 = jo2.getJSONObject("rel_category");
                                        JSONObject jsonObject3 = jsonObject2.getJSONObject("data");
                                        JSONObject jsonObject4 = jsonObject3.getJSONObject("attributes");
                                        String category = jsonObject4.getString("nm_cat");

                                        ob = new object_product(String.valueOf(id), name, String.valueOf(hrg), imageUrl, descrip, String.valueOf(brt), clr, stock);

                                        ob.setId(String.valueOf(id));
                                        ob.setNama(name);
                                        ob.setDesc(descrip);
                                        ob.setHrg(String.valueOf(hrg));
                                        ob.setImgUrl(imageUrl);
                                        ob.setBrt(String.valueOf(brt));
                                        ob.setColor(clr);
                                        ob.setCategory(category);
                                        ob.setStock(stock);


                                        arrayProduk.add(ob);
                                        arrayProdukFilter.add(ob);

                                        //requaired searchBox
                                        comunicatorOne.sendListData(String.valueOf(id), name, String.valueOf(hrg), imageUrl, descrip, String.valueOf(brt), clr, stock);
                                    }
                                    gridViewAdapter = new ProductAdapter(getContext(), getActivity(), arrayProduk);
                                    gridView.setAdapter(gridViewAdapter);
                                }else{
                                    notif("Data masih kosong.", 0);
                                }
                            }
                            api.stopLoad(getContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            api.stopLoad(getContext());
                            notif("Tidak dapat mengambil data.", 0);
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                api.stopLoad(getContext());
//                notif("Your connection is bad.", 0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Adding our request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    public void notif(String pesan, int code) {
        switch (code) {
            case 0:
                builder.setMessage(pesan);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case 1:
                builder1.setMessage(pesan);
                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                break;
        }

    }

    //requaired searchBox
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getContext();
        comunicatorOne = (Comunicator)context;
    }

    public void filterData(String name){
//        Toast.makeText(getContext(), "filter from category=>"+name, Toast.LENGTH_LONG).show();
        Log.d("data ","===>filter from category=>"+name);
        filterDatabyCategory(name);
    }

    public void filterDatabyCategory(String category){

        gridViewAdapter.getFilter().filter(category.toString());
//        category = category.toLowerCase();
////        arrayProduk.clear();
//        for(int i=0; i<arrayProdukFilter.size(); i++){
//            Log.d("result filter","==>"+arrayProdukFilter.get(i).getCategory());
//            if(category.contains(arrayProduk.get(i).getCategory().toLowerCase())){
//                arrayProduk.add(arrayProdukFilter.get(i));
//            }
//        }
//        gridViewAdapter = new ProductAdapter(getContext(), getActivity(), arrayProduk);
//        gridView.setAdapter(gridViewAdapter);

    }

    public void refreshProduct(){
        gridViewAdapter.refreshtoNormalproduct();
    }

}

