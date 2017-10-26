package com.example.jemmycalak.thisismymarket.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jemmycalak.thisismymarket.Model.object_product;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.util.DebugLog;
import com.hrules.horizontalnumberpicker.HorizontalNumberPicker;
import com.hrules.horizontalnumberpicker.HorizontalNumberPickerListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jemmy Calak on 6/9/2017.
 */

public class ListKeranjang extends BaseAdapter implements HorizontalNumberPickerListener
{

    Context c;
    ArrayList<object_product> arrayProduct = new ArrayList<>();
    LayoutInflater inflate;

    HorizontalNumberPicker picker;
    int value;

    public ListKeranjang(Context c, ArrayList<object_product> arrayProduct) {
        this.c =c;
        this.arrayProduct =arrayProduct;
        inflate =(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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



        if(convertView==null){
            convertView=inflate.inflate(R.layout.keranjang_item, parent, false);
//            convertView=inflate.inflate(R.layout.keranjang, parent, false);
        }

        //call id from xml
        ImageView image=(ImageView)convertView.findViewById(R.id.image);
        TextView nm = (TextView)convertView.findViewById(R.id.nm_brg);
        TextView harga = (TextView)convertView.findViewById(R.id.harga);
//        final TextView qwty = (TextView)convertView.findViewById(R.id.qwt);

//        Button decress = (Button)convertView.findViewById(R.id.decress_btn);
//        Button incress = (Button)convertView.findViewById(R.id.incress_btn);
        Button del = (Button)convertView.findViewById(R.id.delete);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, ""+value, Toast.LENGTH_SHORT).show();
            }
        });

//        picker = (HorizontalNumberPicker)convertView.findViewById(R.id.numberpicker);
//        picker.setMinValue(1);
//        picker.setMaxValue(5);
//        picker.setListener(this);

        final object_product objects = (object_product) this.getItem(position);

//        decress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        incress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String values= arrayProduct.get(position).getNama() +1;
//                if(arrayProduct.get(position).getNama().equals(values)){
//                    int id=arrayProduct.get(position).getId();
//                }
//
//                qwty.setText("QTY :"+String.valueOf(values));
//            }
//        });

        nm.setText(objects.getNama());
        harga.setText("Rp. "+",-");
        Picasso.with(c).load(objects.getImgUrl()).into(image);

        //Toast.makeText(applicationContext, "data : "+objects.getId(), Toast.LENGTH_SHORT).show();

        return convertView;
    }

    @Override
    public void onHorizontalNumberPickerChanged(HorizontalNumberPicker horizontalNumberPicker, int valuse) {
        switch (horizontalNumberPicker.getId()) {
            case R.id.numberpicker:
                DebugLog.d("horizontal_number_picker1 current asd:" + valuse);
                value=valuse;
                break;
        }
    }
}
