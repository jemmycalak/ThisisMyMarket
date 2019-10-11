package com.jemmy.calak.camart.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.jemmy.calak.camart.Model.object_bank;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.view.Payment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jemmy Calak on 9/1/2017.
 */

public class PaymentAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<object_bank> arrayBank = new ArrayList<>();
    private LayoutInflater inflater;
    private Activity activity;
    private ImageView imageView;
    private CheckBox checkBox;
    private Boolean vis;
    private int selected_position = -1, id_bank;

    public PaymentAdapter(Activity activity, Context c, ArrayList<object_bank> arrayBank) {
        this.c = c;
        this.activity = activity;
        this.arrayBank = arrayBank;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return arrayBank.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayBank.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_payment_item, viewGroup, false);
        }

        imageView = (ImageView) view.findViewById(R.id.img_bank);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox_bank);

        final object_bank objectBank = (object_bank) this.getItem(position);

        Picasso.with(c).load(objectBank.getImg_bank()).placeholder(R.drawable.placeholder).into(imageView);
        checkBox.setText(
                "Bank " + objectBank.getBank() + " (Manual Checking)\n"
                + "No.Rek : " + objectBank.getNorek() + "\n"
                + "AN : "+objectBank.getAtasnam()
        );

        //ini untuk memilih salah satu checkbox
        if (selected_position == position) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    //setposition checked
                    selected_position = position;
                    //getId bank
                    ((Payment) activity).setVisible(vis=true);
                    ((Payment)activity).saveData(objectBank.getId(), objectBank.getBank(), objectBank.getAtasnam(), objectBank.getNorek());
                } else {
                    selected_position = -1;
                    ((Payment)activity).setVisible(vis=false);
                }
                //refresh adapter checkboxk
                notifyDataSetChanged();

            }
        });

        return view;
    }
}
