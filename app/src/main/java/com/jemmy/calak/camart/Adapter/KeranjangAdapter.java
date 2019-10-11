package com.jemmy.calak.camart.Adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jemmy.calak.camart.Model.object_product;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.util.formatNominal;
import com.jemmy.calak.camart.util.userSharedPreference;
import com.jemmy.calak.camart.util.SQLite;
import com.jemmy.calak.camart.view.Keranjang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jemmy Calak on 6/9/2017.
 */

public class KeranjangAdapter extends BaseAdapter {

    private Context c;
    private List<object_product> arrayProduct = new ArrayList<>();
    private LayoutInflater inflate;

    private int value, id_user;
    private userSharedPreference session;
    private SQLite db;
    private int jml_product = 0, total;
    private String nm_product, img_product, color_product, id_product, price_product;
    private Activity activity;
    private Cursor cursor;

    public KeranjangAdapter(Activity activity, Context c, List<object_product> arrayProduct) {
        this.c = c;
        this.activity = activity;
        this.arrayProduct = arrayProduct;
        inflate = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.activity_keranjang_item, parent, false);
        }
        holder = new ViewHolder();

        //call id from xml
        holder.image = (ImageView) convertView.findViewById(R.id.image);
        holder.nm = (TextView) convertView.findViewById(R.id.nm_brg);
        holder.harga = (TextView) convertView.findViewById(R.id.harga);
        holder.color = (TextView) convertView.findViewById(R.id.color_keranjang);

        holder.decress = (Button) convertView.findViewById(R.id.decress_btn);
        holder.incress = (Button) convertView.findViewById(R.id.incress_btn);
        holder.del = (Button) convertView.findViewById(R.id.delete);
        holder.qty = (TextView) convertView.findViewById(R.id.qwt);


        final object_product objects = (object_product) this.getItem(position);
        db = new SQLite(c);
        getUserId();


        id_product = objects.getId();
        nm_product = objects.getNama();
        price_product = objects.getHrg();
        img_product = objects.getImgUrl();
        color_product = objects.getColor();

        formatNominal formatNominal = new formatNominal();

        holder.nm.setText(objects.getNama());
        holder.harga.setText("Rp. " + formatNominal.formatNumber(Integer.parseInt(objects.getHrg())) + ",-");
        holder.color.setText(objects.getColor());
        Picasso.with(c).load(objects.getImgUrl()).placeholder(R.drawable.placeholder).into(holder.image);
        holder.qty.setText(String.valueOf(objects.getJmlh()));

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.deleteDataCart(id_user, arrayProduct.get(position).getId())) {

                    arrayProduct.remove(position);
                    notifyDataSetChanged();   ///untuk refressh listAdapter
                    refresh();
                    if (arrayProduct.isEmpty()) {
                        ((Keranjang) activity).setVisibleCheckout();
                        ((Keranjang) activity).snackbar();
                        ((Keranjang) activity).setVisibleTotal(0);
                    }

                } else {
                    Toast.makeText(c, "Wasn't deleted.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //pengurangan
        holder.decress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int db_qty = db.getQtyData(id_user, arrayProduct.get(position).getId());
                if (db_qty != 1) {
                    db_qty -= 1;
                    holder.qty.setText(String.valueOf(db_qty));
                    if (db.updateJmlhData(id_user, arrayProduct.get(position).getId(), db_qty)) {
                        refresh();
                    }
                }

            }
        });
        //penambahan
        holder.incress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int db_qty = db.getQtyData(id_user, arrayProduct.get(position).getId()) + 1;
                holder.qty.setText(String.valueOf(db_qty));
                if (db.updateJmlhData(id_user, arrayProduct.get(position).getId(), db_qty)) {
                    refresh();
                }
            }
        });

        return convertView;
    }

    private void refresh() {
        try {
            List<object_product> object = db.getAllDataCartById(id_user);
            for (object_product ob1 : object) {
                total += Integer.valueOf(ob1.getJmlh()) * Integer.valueOf(ob1.getHrg());
            }

            //to refresh total belanja
            ((Keranjang) activity).updateTotal(total);
            total = 0;
        } catch (Exception e) {
            Log.d("Error refresh", "" + e);
        }
    }

    private static class ViewHolder {
        private ImageView image;
        private TextView nm, harga, color, qty;
        private Button decress;
        private Button incress;
        private Button del;

    }

    private void getUserId() {
        session = new userSharedPreference(c);
        HashMap<String, String> hashMap = session.getUserDetail();
        id_user = Integer.valueOf(hashMap.get(session.KEY_ID));

    }

}
