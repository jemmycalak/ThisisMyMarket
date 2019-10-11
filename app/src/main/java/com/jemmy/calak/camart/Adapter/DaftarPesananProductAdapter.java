package com.jemmy.calak.camart.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jemmy.calak.camart.Model.object_product;
import com.jemmy.calak.camart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jemmy Calak on 10/6/2017.
 */



public class DaftarPesananProductAdapter extends RecyclerView.Adapter<DaftarPesananProductAdapter.MyHolder> {

    private ArrayList<object_product> arraypPoduct;
    private Context c;


    public DaftarPesananProductAdapter(Context c, ArrayList<object_product> dataProduct) {
        this.c= c;
        this.arraypPoduct = dataProduct;
    }

    @Override
    public DaftarPesananProductAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pesanan_items, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(DaftarPesananProductAdapter.MyHolder holder, int position) {

        final object_product ob = arraypPoduct.get(position);

        Log.d("data product -- ",": "+ob.getNama());

        String jmlh = String.valueOf(ob.getJmlh());
        String hrga = String.valueOf(ob.getHrg());

        holder.jml_product.setText(jmlh);
        holder.nm_product.setText(ob.getNama());
        holder.hrg_product.setText(hrga);

        Picasso.with(c).load(arraypPoduct.get(position).getImgUrl()).placeholder(R.drawable.placeholder).into(holder.img);

        Log.d("data item product ",":"+ob.getNama());


    }

    @Override
    public int getItemCount() {
        return (null !=arraypPoduct ? arraypPoduct.size() : 0);
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        TextView nm_product, hrg_product, jml_product;
        ImageView img;

        public MyHolder(View itemView){
            super(itemView);

            nm_product = (TextView)itemView.findViewById(R.id.nm_psn);
            hrg_product = (TextView)itemView.findViewById(R.id.hrg_psn);
            jml_product = (TextView)itemView.findViewById(R.id.jmlh_psn);

            img = (ImageView)itemView.findViewById(R.id.img_psn);

        }
    }

}
