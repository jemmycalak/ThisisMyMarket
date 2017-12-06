package com.example.jemmycalak.thisismymarket.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jemmycalak.thisismymarket.Model.object_product;
import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.fragment.FragmentCategory;
import com.example.jemmycalak.thisismymarket.interfacesComunicator.Comunicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jemmy Calak on 9/22/2017.
 */

public class CategoryAdapter extends BaseAdapter  {

    ArrayList<object_product> object_category;
    Context c;
    LayoutInflater inflater;
    Fragment fragment;



    public CategoryAdapter(Context c, FragmentCategory fragmentCategory, ArrayList<object_product> object_category){
        this.c = c;
        this.object_category = object_category;
        this.fragment = fragmentCategory;

        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return object_category.size();
    }

    @Override
    public Object getItem(int position) {
        return object_category.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_category_item, viewGroup, false);
        }

        CircleImageView img = (CircleImageView) view.findViewById(R.id.img_category);
        TextView textView = (TextView)view.findViewById(R.id.nm_category);

        final object_product ob = (object_product) this.getItem(position);

        textView.setText(ob.getCategory());

        Picasso.with(c)
                .load(ob.getImgCat())
                .placeholder(R.drawable.placeholder)
                .into(img);

        return view;
    }

}
