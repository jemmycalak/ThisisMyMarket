package com.example.jemmycalak.thisismymarket.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jemmycalak.thisismymarket.Model.object_product;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.formatNominal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jemmy Calak on 10/27/2017.
 */

public class ConfirmOrderProductAdapter extends RecyclerView.Adapter<ConfirmOrderProductAdapter.myHolder> {

    private List<object_product> dataProduct = new ArrayList<>();
    private Context context;

    public ConfirmOrderProductAdapter(Context context, List<object_product> dataProduct){
        this.context = context;
        this.dataProduct = dataProduct;
    }

    @Override
    public ConfirmOrderProductAdapter.myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_confirmasi_detail_pesanan_items, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(ConfirmOrderProductAdapter.myHolder holder, int position) {

        final object_product ob = dataProduct.get(position);
        Log.d("Data product confirm","=====>"+ob.getNama());

        formatNominal formatNominal = new formatNominal();
        holder.nm_barang.setText(ob.getNama());
        holder.harga.setText("Rp. "+formatNominal.formatNumber(Integer.parseInt(ob.getHrg()))+",-");
        holder.jmlh.setText("Jumlah :"+String.valueOf(ob.getJmlh()));
        Picasso.with(context).load(ob.getImgUrl()).placeholder(R.drawable.placeholder).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return dataProduct.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView nm_barang, harga, jmlh;

        public myHolder(View view) {
            super(view);

            image = (ImageView)view.findViewById(R.id.imageConfirm);
            nm_barang = (TextView)view.findViewById(R.id.namaConfirm);
            harga = (TextView)view.findViewById(R.id.hargaConfirm);
            jmlh = (TextView)view.findViewById(R.id.jumlahConfirm);
        }
    }
}
