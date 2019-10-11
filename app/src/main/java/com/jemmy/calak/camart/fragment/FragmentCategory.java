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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jemmy.calak.camart.Adapter.CategoryAdapter;
import com.jemmy.calak.camart.Config;
import com.jemmy.calak.camart.Model.object_product;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.interfacesComunicator.Comunicator;
import com.jemmy.calak.camart.util.API;
import com.jemmy.calak.camart.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategory extends Fragment {

    Button b1, b2;

    //for going to second Fragment
    private static final String TAG = "Fragment1";
    final String url = Config.app_url_api + "products/category";
    private object_product ob;
    private ArrayList<object_product> arrayCategory = new ArrayList<>();
    private GridView gridView;
    private API api = new API();
    private Context context;
    private AlertDialog.Builder builder, builder1, builder2;
    private CategoryAdapter categoryAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    //required search data dan share data
    private Comunicator comunicator;

    public FragmentCategory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        init(view);
        getData();
        setOnclickCategory();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayCategory.clear();
                getData();
            }
        });

        return view;
    }

    private void init(View view) {
        gridView = (GridView) view.findViewById(R.id.gridcategory);
        builder = new AlertDialog.Builder(getContext());
        builder1 = new AlertDialog.Builder(getContext());
        builder2 = new AlertDialog.Builder(getContext());
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refreshCategory);
    }

    private void getData() {
        api.startLoad(getContext(), "Please wait..");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("category", ": " + response);

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    String code = jsonObject.getString("status");
                    if (code.equals("false")) {
                        notif("Tidak dapat mengambil data.", 0);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Log.d("data category", String.valueOf(jsonArray));

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                String id = jo.getString("id");
                                String nm_category = jo.getString("nm_cat");
                                String img_url = jo.getString("img_cat");

                                ob = new object_product();
                                ob.setId(id);
                                ob.setCategory(nm_category);
                                ob.setImgCat(img_url);
                                arrayCategory.add(ob);
                            }
                            categoryAdapter = new CategoryAdapter(getContext(), FragmentCategory.this, arrayCategory);
                            gridView.setAdapter(categoryAdapter);
                        }else{
                            notif("Tidak dapat mengambil data.", 0);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    notif("Tidak dapat mengambil data.", 0);
                }
                api.stopLoad(getContext());
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                api.stopLoad(getContext());
                notif("Your Connection is bad.", 0);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        VolleySingleton.getmInstance(getContext()).addToRequestque(jsonObjectRequest);
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

    //required search data dan share data
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        comunicator = (Comunicator) activity;
    }

    public void sendDatafilter(String name){
        comunicator.filterDataProduct(name);
    }

    public void setOnclickCategory(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                object_product model = (object_product)adapterView.getItemAtPosition(i);
                sendDatafilter(model.getCategory());
            }
        });
    }

}
