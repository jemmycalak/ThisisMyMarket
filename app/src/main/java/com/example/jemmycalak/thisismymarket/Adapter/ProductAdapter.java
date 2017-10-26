package com.example.jemmycalak.thisismymarket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jemmycalak.thisismymarket.R;
import com.example.jemmycalak.thisismymarket.Model.object_product;
import com.example.jemmycalak.thisismymarket.view.detail_laptop;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jemmy Calak on 5/16/2017.
 */

public class GridViewAdapter extends BaseAdapter{

//    ValueFilter valueFilter; //creat new class at below
    ArrayList<object_product> filterList;
    Context c;
    ArrayList<object_product> object;
    LayoutInflater inflater;

    //buat contruktor

    public GridViewAdapter(Context c, ArrayList<object_product> object) {
        this.c = c;
        this.object = object;
        this.filterList = object;

        inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GridViewAdapter() {
//        contruktor for searchView
    }

    @Override
    public int getCount() {
        return object.size();
    }

    @Override
    public Object getItem(int position) {
        return object.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if(convertView==null){
            convertView=inflater.inflate(R.layout.fragment_laptop_item_gridview, parent, false);
        }

        //panggil id item_laptop_gridviewridview.xml

        ImageView imga=(ImageView)convertView.findViewById(R.id.img);
        TextView nm=(TextView)convertView.findViewById(R.id.t1);
        TextView hrg=(TextView)convertView.findViewById(R.id.t2);

        //set data to fragment_laptop_item_gridview_gridview.xml
        final object_product objects=(object_product) this.getItem(position);

        nm.setText(objects.getNama());
        hrg.setText("Rp. "+objects.getHrg()+",-");
        Picasso.with(c).load(objects.getImgUrl()).into(imga);


        //untuk detail product
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailActivity(objects.getId(), objects.getImgUrl(), objects.getNama(), objects.getDesc(), objects.getHrg(), objects.getColor(), objects.getBrt());
            }
        });

        return convertView;
    }

    //untuk data detail
    private void openDetailActivity(Integer id, String imageUrl, String nama, String desc, Integer hrg, String clr, Double brt){
        Intent i=new Intent(c, detail_laptop.class);



        //kirim data ke detail.java
        i.putExtra("id_k", id);
        i.putExtra("image_k", imageUrl);
        i.putExtra("nama_k", nama);
        i.putExtra("desc_k", desc);
        i.putExtra("hrg_k", hrg);
        i.putExtra("clr_k", clr);
        i.putExtra("brt_k", brt);

        Log.d("data",": "+id+" , "+imageUrl);

        //jalankan detail_laptop.class
        c.startActivity(i);

    }




//    @Override
//    public Filter getFilter() {
//        if (valueFilter == null) {
//            valueFilter = new ValueFilter();
//        }
//        return valueFilter;
//    }
//
//    class ValueFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results = new FilterResults();
//
//            if (constraint != null && constraint.length() > 0) {
//                ArrayList<object_product> arrayFilter=new ArrayList<object_product>();
//                for (int i = 0; i < object.size(); i++) {
//                    if ((object.get(i).getNama().toUpperCase()).contains(constraint)) {
//                        object_product ob1 = new object_product(
//                                object.get(i).getId(),
//                                object.get(i).getHrg(),
//                                object.get(i).getBrt(),
//                                object.get(i).getNama(),
//                                object.get(i).getImgUrl(),
//                                object.get(i).getDesc());
//                        arrayFilter.add(ob1);
//                    }
//                }
//                results.count = arrayFilter.size();
//                results.values = arrayFilter;
//            } else {
//                results.count = object.size();
//                results.values = object;
//            }
//            return results;
//
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            filterList = (ArrayList<object_product>) results.values;
//            notifyDataSetChanged();
//        }
//    }

//    /*make this
//    ArrayList<object_product> arrayProduk;
//    Context context;
//    int resource;
//
//    public GridViewAdapter(Context context, int resource, ArrayList<object_product> arrayProduk){
//        super(context, resource, arrayProduk);
//        //Getting all the values
//        //make this
//        this.arrayProduk = arrayProduk;
//        this.context = context;
//        this.resource = resource;
//
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//
//        //Creating a linear layout
//        if(convertView == null){
//            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_laptop_item_gridview, parent, false);
//            //convertView = LayoutInflater.inflate(R.layout.fragment_laptop_item_gridview, null);
//        }
//
//        object_product objectProduct1 = getItem(position);
//
//        // NOTE: You would normally use the ViewHolder pattern here
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
//        TextView textView = (TextView) convertView.findViewById(R.id.t1);
//        TextView textView1 = (TextView) convertView.findViewById(R.id.t2);
//
//        //use picasso
//        Picasso.with(context).load(objectProduct1.getImgUrl()).into(imageView);
//        textView.setText(objectProduct1.getNama());
//        //textView1.setText(objectProduct1.getHrg());
//
//        return convertView;
//    }
//
//    */
}
