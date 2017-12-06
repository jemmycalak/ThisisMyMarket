package com.jemmy.calak.camart.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jemmy.calak.camart.Model.object_order;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.util.formatNominal;
import com.jemmy.calak.camart.view.DaftarPesanan;
import com.jemmy.calak.camart.view.UploadBuktiPembayaran;

import java.util.ArrayList;

/**
 * Created by Jemmy Calak on 9/28/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {

    private Context c;
    private ArrayList<object_order> orderArray;
    private LayoutInflater  inflater;
//    animation countDownTime
    private Boolean isUp=false;
    private Activity activity;
    private String id_user;


    public OrderAdapter(Context c, Activity activity, ArrayList<object_order> arraysectionOrder, String id_user){
        this.id_user = id_user;
        this.c = c;
        this.activity = activity;
        this.orderArray = arraysectionOrder;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public OrderAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pesanan_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.MyHolder holder, final int position) {

        //        get data from object
        object_order or = orderArray.get(position);

        //buat arraylist untuk ambil data product
        ArrayList dataProduct = or.getArraysectionProduct();

//        kirim data ke adapter produt
        OrderProductAdapter product = new OrderProductAdapter(c, dataProduct);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false));
        holder.recyclerView.setAdapter(product);

//        Log.d("data item Product ",": "+or.getId_order());

        String isVisibleButon = or.getStatus_order();
        if(isVisibleButon.equals("dikirim") || isVisibleButon.equals("diterima") || isVisibleButon.equals("dibatalkan") ){
            isVisibleButton(holder);
        }
        formatNominal formatNominal = new formatNominal();

        holder.id_order.setText(String.valueOf(or.getId_order()));

        holder.no_invoice.setText(or.getNo_invoice());
        holder.stts_order.setText(or.getStatus_order());
        holder.total.setText(formatNominal.formatNumber(Integer.parseInt(or.getTotal())));
        holder.tgl_order.setText(or.getTgl_order());
        holder.shiping.setText(formatNominal.formatNumber(Integer.parseInt(or.getShipping())));

        holder.nm_penerima.setText(or.getNm_penerima());
        holder.almt_penerima.setText(or.getAlmt());
        holder.notelp_penerima.setText(or.getNotelp());
        holder.showMore.setText("NO. INVOICE : "+or.getNo_invoice());

//        setVisibleLayout Gone first
        holder.dasboard.setVisibility(View.GONE);
        holder.showMore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isUp){
                    holder.dasboard.setVisibility(View.GONE);
                }else{
                    holder.dasboard.setVisibility(View.VISIBLE);
                }
                isUp=!isUp;

                return false;
            }
        });

        holder.btl_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DaftarPesanan)activity).notif("Apakah anda ingin membatalkan pesanan ?", 0, orderArray.get(position).getNo_invoice());
            }
        });
        holder.upload_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, UploadBuktiPembayaran.class);
                intent.putExtra("id_order", String.valueOf(orderArray.get(position).getId_order()));
                intent.putExtra("id_user", id_user);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderArray.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView id_order, no_invoice, nm_penerima, almt_penerima, notelp_penerima, stts_order, total, tgl_order, shiping, showMore;
        private RecyclerView recyclerView;
        private LinearLayout dasboard;
        private RelativeLayout layBatal;
        private Button btl_pesan, upload_pesan;

        public MyHolder(View itemView){
            super(itemView);
            inisilisation(itemView);
        }

        private void inisilisation(View view) {
            id_order = (TextView)view.findViewById(R.id.id_order);
            no_invoice = (TextView)view.findViewById(R.id.no_invoice);
            stts_order = (TextView)view.findViewById(R.id.status_psn);
            total = (TextView)view.findViewById(R.id.total);
            tgl_order = (TextView)view.findViewById(R.id.tgl);
            shiping = (TextView)view.findViewById(R.id.biayakirim);
            showMore = (TextView)view.findViewById(R.id.showMore);

            nm_penerima = (TextView)view.findViewById(R.id.nm_penerima);
            almt_penerima = (TextView)view.findViewById(R.id.almt_penerima);
            notelp_penerima = (TextView)view.findViewById(R.id.notelp_penerima);

            layBatal = (RelativeLayout) view.findViewById(R.id.layoutBatal);
            recyclerView = (RecyclerView)view.findViewById(R.id.recycleView_product);
            dasboard = (LinearLayout)view.findViewById(R.id.dashboard);
            btl_pesan = (Button)view.findViewById(R.id.bt_order);
            upload_pesan = (Button)view.findViewById(R.id.bt_upload);

        }
    }

    private void isVisibleButton(MyHolder holder){
        holder.layBatal.setVisibility(View.GONE);
        holder.layBatal.setBackgroundColor(Color.WHITE);
    }


    public void refreshAdapter(ArrayList<object_order> orderArray){
        this.orderArray.clear();
        this.orderArray.addAll(orderArray);
        this.orderArray = orderArray;

        notifyDataSetChanged();
    }


}
