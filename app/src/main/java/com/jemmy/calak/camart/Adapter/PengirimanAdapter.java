package com.jemmy.calak.camart.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.CheckBox;

import com.jemmy.calak.camart.Model.object_address;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.view.Pengiriman;

import java.util.ArrayList;


/**
 * Created by Jemmy Calak on 7/9/2017.
 */

public class PengirimanAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<object_address> arrayAddress = new ArrayList<>();
    private LayoutInflater inflater;
    private CheckBox checkBox;
    private int selected_posotion = -1;
    private int id;
    private Activity activity;
    private Boolean vis;


    public PengirimanAdapter(Activity activity, Context c, ArrayList<object_address> arrayProduct) {
        this.activity = activity;
        this.c = c;
        this.arrayAddress = arrayProduct;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return arrayAddress.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayAddress.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_pengiriman_item, parent, false);

        }

        //get data from object
        final object_address object = (object_address) this.getItem(position);

        checkBox = (CheckBox) convertView.findViewById(R.id.alamat);
        //setText
        checkBox.setText(
                        "Penerima : "+object.getName() + " \n"
                        +"Address : "+object.getAddress() + " \n"
                        +"Provinsi : "+object.getProvinsi() + " \n"
                        +"No.Telp :"+object.getNotelp());


        ///this methodd for select one checkbox
        if (selected_posotion == position) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    selected_posotion = position;

                    ((Pengiriman)activity).setVisibleLayout(true);
//                    send Data to Pengiriman activity
                    ((Pengiriman) activity).dataAddres(object.getId(),
                            object.getName(), object.getAddress(), object.getProvinsi(),
                            String.valueOf(object.getNotelp()));
                } else {
                    selected_posotion = -1;
                    ((Pengiriman)activity).setVisibleLayout(false);
                }
                //untuk otomatis refres listnya
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void refreshAdapter(ArrayList<object_address>arrayAddress) {
        this.arrayAddress.clear();
        this.arrayAddress.addAll(arrayAddress);
        this.arrayAddress = arrayAddress;
        //and call notifyDataSetChanged
        notifyDataSetChanged();
    }


}
