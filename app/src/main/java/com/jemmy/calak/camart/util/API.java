package com.jemmy.calak.camart.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jemmy.calak.camart.Config;
import com.jemmy.calak.camart.Model.object_product;
import com.jemmy.calak.camart.view.ConfirmasiDetailPesanan;
import com.jemmy.calak.camart.view.DaftarPesanan;
import com.jemmy.calak.camart.view.Input_alamat;
import com.jemmy.calak.camart.view.ProfileActivity;
import com.jemmy.calak.camart.view.UploadBuktiPembayaran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jemmy Calak on 10/21/2017.
 */

public class API {

    private final static String url_api = Config.app_url_api;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder, builder1, builder2;

    public void startLoad(final Context context, String pesan) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage(pesan);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();

    }

    public void stopLoad(final Context context) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void newAddress(final Activity activity, String id_user, String nm_penerima, String almt, String propinsi, String notelp) {
        startLoad(activity, "Please wait..");
        String url = Config.app_url_api + "users/new_address";
        //request body
        Map<String, String> params = new HashMap<String, String>();
        params.put("id_user", id_user);
        params.put("nama", nm_penerima);
        params.put("alamat", almt);
        params.put("provinsi", propinsi);
        params.put("notelp", notelp);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    String code = jsonObject.getString("success");
                    String msg = jsonObject.getString("msg");
                    if (code.equals("true")) {
                        ((Input_alamat) activity).notif("Berhasil menambahkan alamat", 2);
                    } else {
                        ((Input_alamat) activity).notif(msg, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    stopLoad(activity);
                }

                Log.d("response ", "server ==>" + response);
                stopLoad(activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error connection ==>", "new address");
                stopLoad(activity);
            }
        })
//            {
//                ////////////////////////////////////////REQUEST HEADER///////////////////////////////////////////
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    return super.getHeaders();
//                }
//            }
                ;

        VolleySingleton.getmInstance(activity).addToRequestque(jsonObjectRequest);
    }

    public void sendDataOrder(String id_user, String id_bank, String shipping, String mtotal, String id_address, List<object_product> item, final Activity activity) {
        startLoad(activity, "Please wait..");

        //membuat array parameter
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        for(int i=0; i<item.size(); i++){
            try {
                jsonObject = new JSONObject();
                jsonObject.put("id_product", item.get(i).getId());
                jsonObject.put("jumlah", item.get(i).getJmlh());

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //setelah di buat array di buat menjadi object lagi
        //belum di gunain
        JSONObject jsonObject1 = new JSONObject();
        String paramObject = null;
        try {
            jsonObject1.put("product",jsonArray);
            paramObject = String.valueOf(jsonObject1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put("id_user", id_user);
        params.put("id_bank", id_bank);
        params.put("shiping", shipping);
        params.put("total", mtotal);
        params.put("id_address", id_address);
        params.put("product", jsonArray.toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_api + "products/order", new JSONObject(params),
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response order","===>"+response);
                try {
                    JSONObject jsonObjects = new JSONObject(String.valueOf(response));
                    String code = jsonObjects.getString("status");
                    if(code.equals("true")){
                        ((ConfirmasiDetailPesanan)activity).removeSqlite();
                        ((ConfirmasiDetailPesanan)activity).goFinish();
                    }else{
                        ((ConfirmasiDetailPesanan)activity).notif("Oops pengunjung sedang penuh..", 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ((ConfirmasiDetailPesanan)activity).notif("Oops pengunjung sedang penuh..", 0);
                }
                stopLoad(activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", "=====> DaftarPesanan" + error);
                stopLoad(activity);
                ((ConfirmasiDetailPesanan)activity).notif("Oops pengunjung sedang penuh..", 0);
            }
        });
        VolleySingleton.getmInstance(activity).addToRequestque(jsonObjectRequest);
    }

    public void batalPesan(final Activity activity, String noinvoice, String id_user) {
        startLoad(activity, "Please wait...");
        Map<String, String> params = new HashMap<>();
        params.put("no_invoice", noinvoice);
        params.put("id_user", id_user);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_api + "products/cancleorder", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response cancle order","====>"+response);

                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response ));
                    String code = jsonObject.getString("success");
                    if(code.equals("true")){
                        ((DaftarPesanan)activity).refresh();
                    }else{
                        ((DaftarPesanan)activity).snackbar("Gagal membatalkan pesanan.", 1);
                    }
                } catch (JSONException e) {
                    ((DaftarPesanan)activity).snackbar("Gagal membatalkan pesanan.", 1);
                    Log.d("Eror batal pesanan", "===>"+e);
                }
                stopLoad(activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorCanclePesanan","====>"+error);
                ((DaftarPesanan)activity).snackbar("Your connection is bad.", 1);
                stopLoad(activity);
            }
        });

        VolleySingleton.getmInstance(activity).addToRequestque(jsonObjectRequest);
    }

    public void updateProfile(final String idUser, final String mname, final String memail, final String mnotelp, final String mjk, String lastPassword, String newPassword, final Activity activity) {

        startLoad(activity, "Please wait..");
        Map<String, String> params = new HashMap<>();
        params.put("idUser", idUser);
        params.put("name", mname);
        params.put("email", memail);
        params.put("notelp", mnotelp);
        params.put("jk", mjk);
        params.put("lastPassword", lastPassword);
        params.put("newPassword", newPassword);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_api + "users/update", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Response update profile","===>"+response);
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    String code = jsonObject.getString("success");
                    if(code.equals("false")){
                        String msg = jsonObject.getString("msg");
                        ((ProfileActivity)activity).notif(msg, 0);
                    }else{
                        String msg = jsonObject.getString("msg");
                        ((ProfileActivity)activity).notif(msg, 2);
                        ((ProfileActivity)activity).updateSharedPreferebce(idUser, mname, memail,mnotelp, mjk);
                    }

                } catch (JSONException e) {
                    stopLoad(activity);
                    ((ProfileActivity)activity).notif("Gagal update profile", 0);
                }

                stopLoad(activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopLoad(activity);
                Log.d("error volley", "===>"+error);
                Toast.makeText(activity, "Your connection so bad.", Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getmInstance(activity).addToRequestque(jsonObjectRequest);
    }

    public void uploadBuktiPembayaran(String id_user, String id_order, String imageString, final Activity activity) {
        startLoad(activity, "Please wait..");
        String url = Config.app_url_api+"uploadBuktiPembayaran";
        Log.d("image","==>"+imageString);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_user", id_user);
            jsonObject.put("id_order", id_order);
            jsonObject.put("image", imageString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("respons","upload===>"+response);

                try {
                    JSONObject jsonObject1 = new JSONObject(String.valueOf(response));
                    String code = jsonObject1.getString("status");
                    if(code.equals("true")){
                        notif(activity, jsonObject1.getString("msg").toUpperCase(), 0);
                    }else{
                        notif(activity, jsonObject1.getString("msg").toUpperCase(), 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                stopLoad(activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopLoad(activity);
                notif(activity, "Please check your connection.", 0);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        VolleySingleton.getmInstance(activity).addToRequestque(jsonObjectRequest);

    }

    public void notif(Context context, String pesan, int code){
        builder = new AlertDialog.Builder(context);
        builder1 = new AlertDialog.Builder(context);
        builder2 = new AlertDialog.Builder(context);
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

                    }
                });
                builder1.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                break;
            case 2:

                break;
        }

    }
}
