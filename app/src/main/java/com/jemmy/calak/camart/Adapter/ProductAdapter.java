package com.example.jemmycalak.thisismymarket.Adapter;

import android.app.Activity;
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
import com.example.jemmycalak.thisismymarket.util.formatNominal;
import com.example.jemmycalak.thisismymarket.view.DetailProduct;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Jemmy Calak on 5/16/2017.
 */

public class ProductAdapter extends BaseAdapter implements Filterable{

    //requairet search data
    ArrayList<object_product> filterList;
    ItemFilter itemFilter;

    private Context c;
    private ArrayList<object_product> object;
    private LayoutInflater inflater;
    private Activity activity;

    //buat contruktor

    public ProductAdapter(Context c, Activity activity, ArrayList<object_product> objects) {
        this.c = c;
        this.activity = activity;
        this.object = objects;
        //requairet search data
        this.filterList = objects;

        inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        formatNominal formatNominal = new formatNominal();


        //panggil id item_laptop_gridviewridview.xml

        ImageView imga=(ImageView)convertView.findViewById(R.id.img);
        TextView nm=(TextView)convertView.findViewById(R.id.t1);
        TextView hrg=(TextView)convertView.findViewById(R.id.t2);

        //set data to fragment_laptop_item_gridview_gridview.xml
        final object_product objects=(object_product) this.getItem(position);

        nm.setText(objects.getNama());
        hrg.setText("Rp. "+formatNominal.formatNumber(Integer.parseInt(objects.getHrg()))+",-");
        Picasso.with(c).load(objects.getImgUrl()).into(imga);


        //untuk detail product
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailActivity(objects.getId(), objects.getImgUrl(), objects.getNama(), objects.getDesc(), objects.getHrg(), objects.getColor(), objects.getBrt(), objects.getStock());
            }
        });
        return convertView;
    }

    //untuk data detail
    private void openDetailActivity(String id, String imageUrl, String nama, String desc, String hrg, String clr, String brt, String stock){
        Intent i=new Intent(c, DetailProduct.class);
        //kirim data ke detail.java
        i.putExtra("id_k", id);
        i.putExtra("image_k", imageUrl);
        i.putExtra("nama_k", nama);
        i.putExtra("desc_k", desc);
        i.putExtra("hrg_k", hrg);
        i.putExtra("clr_k", clr);
        i.putExtra("brt_k", brt);
        i.putExtra("stock", stock);

        Log.d("data",": "+id+" , "+imageUrl);

        //jalankan DetailProduct.class
        c.startActivity(i);

    }

//    requaired filter data
    @Override
    public Filter getFilter() {
        if(itemFilter == null){
            itemFilter = new ItemFilter();
        }
        return itemFilter;
    }

    public void refreshtoNormalproduct(){
        object = filterList;
        notifyDataSetChanged();

//        for(int i=0; i<filterList.size(); i++){
//            if(filterList.get(i).getCategory().toLowerCase().contains(charSequence)){
//                object_product newObject = new object_product();
//
//                Log.d("filter","==>"+filterList.get(i).getNama());
//
//                newObject.setNama(filterList.get(i).getNama());
//                newObject.setHrg(filterList.get(i).getHrg());
//                newObject.setImgUrl(filterList.get(i).getImgUrl());
//                newObject.setId(filterList.get(i).getId());
//                newObject.setCategory(filterList.get(i).getCategory());
//
//                newArrayList.add(newObject);
//            }
//        }
    }

    public class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            Log.d("data","data yg mau di filter==>"+charSequence.toString());

            FilterResults results = new FilterResults();
            if(charSequence != null && charSequence.length() >0){
                charSequence = charSequence.toString().toLowerCase();

                ArrayList<object_product> newArrayList = new ArrayList<object_product>();

                for(int i=0; i<filterList.size(); i++){
                    if(filterList.get(i).getCategory().toLowerCase().contains(charSequence)){
                        object_product newObject = new object_product();
//                        Log.d("filter","==>"+filterList.get(i).getNama());

                        newObject.setNama(filterList.get(i).getNama());
                        newObject.setHrg(filterList.get(i).getHrg());
                        newObject.setImgUrl(filterList.get(i).getImgUrl());
                        newObject.setId(filterList.get(i).getId());
                        newObject.setCategory(filterList.get(i).getCategory());
                        newObject.setDesc(filterList.get(i).getDesc());
                        newObject.setColor(filterList.get(i).getColor());
                        newObject.setBrt(filterList.get(i).getBrt());
                        newObject.setStock(filterList.get(i).getStock());

                        newArrayList.add(newObject);
                    }
                }
                results.count = newArrayList.size();
                results.values = newArrayList;
            }else{
                results.count = object.size();
                results.values = object;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            object = (ArrayList<object_product>) filterResults.values;
//            notifyDataSetChanged();
            if(filterResults.count == 0){
                notifyDataSetInvalidated();
            }else{
                object = (ArrayList<object_product>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }
}
