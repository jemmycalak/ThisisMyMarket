package com.jemmy.calak.camart.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jemmy.calak.camart.Model.object_address;
import com.jemmy.calak.camart.Model.object_bank;
import com.jemmy.calak.camart.Model.object_product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jemmy Calak on 8/10/2017.
 */

public class SQLite extends SQLiteOpenHelper {

    //version database
    private static final int DATABASE_VERSION = 1;
    //name of database
    private static final String DATABASE_NAME = "camartdb.db";
    //name table
    private static final String TABLE_PRODUCT ="product";
    private static final String TABLE_PAYMENT = "payment";
    private static final String TABLE_ADDRESS = "address";
    //field database from table product
    private final String KEY_ID = "id_p";
    private final String KEY_ID_USER = "id_user";
    private final String KEY_ID_PRODUCT = "id_product";
    private final String KEY_NAME_PRODUCT = "nm_product";
    private final String KEY_PRICE_PRODUCT = "price_product";
    private final String KEY_IMAGE_PRODUCT = "image_product";
    private final String KEY_COLOR = "color";
    private final String KEY_QTY = "qty";
    //field payment
    private final String KEY_ID_BANK = "id_bank";
    private final String KEY_BANK = "bank";
    private final String KEY_ATAS_NAMA = "atas_nama";
    private final String KEY_NOREK = "norek";
    //field address
    private final String KEY_ID_ADDRESS = "id_address";
    private final String KEY_PENERIMA = "penerima";
    private final String KEY_ADDRESS="address";
    private final String KEY_PROVINCE = "province";
    private final String KEY_NOTELP = "notelp";

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PRODUCT_TABLE = "CREATE TABLE "+ TABLE_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ID_USER + " INTEGER,"
                + KEY_ID_PRODUCT + " INTEGER,"
                + KEY_NAME_PRODUCT + " TEXT,"
                + KEY_PRICE_PRODUCT + " TEXT, "
                + KEY_IMAGE_PRODUCT + " TEXT, "
                + KEY_COLOR + " TEXT,"
                + KEY_QTY + " INTEGER"
                +");";
        String CREATE_PAYMENT_TABLE = "CREATE TABLE "+TABLE_PAYMENT+ "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ID_USER + " INTEGER,"
                + KEY_ID_BANK + " INTEGER,"
                + KEY_BANK + " TEXT,"
                + KEY_NOREK + " TEXT,"
                + KEY_ATAS_NAMA + " TEXT"
                +");";
        String CREATE_ADDRESS_TABLE = "CREATE TABLE "+TABLE_ADDRESS+ "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ID_USER + " INTEGER,"
                + KEY_ID_ADDRESS + " INTEGER,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_PROVINCE + " TEXT,"
                + KEY_PENERIMA + " TEXT,"
                + KEY_NOTELP + " TEXT"
                +");";

        //excute table
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_PAYMENT_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop older table when table is existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ADDRESS);
        //create table again
        onCreate(db);
    }

    //query add item product when button cart is pressed/clicked
    public boolean insert(int id_user, String id, int jmlh,  String price, String nm, String image, String color){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value= new ContentValues();
        value.put(KEY_ID_USER, id_user);
        value.put(KEY_ID_PRODUCT, id);
        value.put(KEY_NAME_PRODUCT, nm);
        value.put(KEY_PRICE_PRODUCT, price);
        value.put(KEY_IMAGE_PRODUCT, image);
        value.put(KEY_QTY, jmlh);
        value.put(KEY_COLOR, color);

        //inserting row
        long result = db.insert(TABLE_PRODUCT, null, value);
        if(result == -1){
            return false;
        }else{
            return true;
        }
//        db.close();
    }

    public Cursor checkStockData(int id_user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from "+TABLE_PRODUCT +" where "+KEY_ID_USER+"= '"+id_user+"' ",null);
        return cur;
    }

    public Cursor checkStockDataById(int id_user, String id_product){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from "+TABLE_PRODUCT+" where "+KEY_ID_USER+" = '"+id_user+"' and "+KEY_ID_PRODUCT+"= '"+id_product+"' ", null);
        return cur;
    }

    public int getQtyData(int id_user, String id_product){
        int qty = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_PRODUCT+" where "+KEY_ID_USER+"='"+id_user+"' and "+KEY_ID_PRODUCT+"= '"+id_product+"' ", null);
        if(cursor.moveToNext()){
            qty = Integer.parseInt(cursor.getString(7));
        }
        return qty;
    }

    public boolean updateJmlhData(int id_user, String id_product, int jmlh){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QTY, jmlh);
        long result = db.update(TABLE_PRODUCT, values,  KEY_ID_USER+"= '"+id_user+"' AND "+KEY_ID_PRODUCT+"= '"+id_product+"' ", null);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public List<object_product> getAllDataCartById(int id_user){
        List<object_product> listProduct = new ArrayList<object_product>();
        String sql = "select * from "+TABLE_PRODUCT+" where "+KEY_ID_USER+"="+id_user;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                object_product ob = new object_product();
                ob.setId(cursor.getString(2));
                ob.setNama(cursor.getString(3));
                ob.setHrg(cursor.getString(4));
                ob.setImgUrl(cursor.getString(5));
                ob.setColor(cursor.getString(6));
                ob.setJmlh(cursor.getString(7));

                //add to list
                listProduct.add(ob);
            }while(cursor.moveToNext());
        }
        return listProduct;
    }

    public Boolean deleteDataCart(int id_user, String id_product){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_PRODUCT, KEY_ID_USER+"= '"+id_user+"' and "+KEY_ID_PRODUCT+"= '"+id_product+"' ", null);
        if(result == -1){ //jika tidak terdelete
            return false;
        }else{
            return true;
        }
    }

    public Boolean insertAddres(int id_user, int id_addres, String penerima, String alamat, String province, String notelp){
        Log.d("Data sqlite ","====>"+id_user+" "+penerima+" "+alamat+" "+ province+" "+ notelp);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_ADDRESS, id_addres);
        values.put(KEY_ID_USER, id_user);
        values.put(KEY_ADDRESS, alamat);
        values.put(KEY_PROVINCE, province);
        values.put(KEY_NOTELP, notelp);
        values.put(KEY_PENERIMA, penerima);
        long result = db.insert(TABLE_ADDRESS, null, values);
        if(result == -1){
            //jika tidak jalan
            Log.d("Error ===>","insert alamat");
            return false;
        }else{

            Log.d("Success ===>","insert alamat");
            return true;
        }
    }

    public Boolean updateAddress(int id_user, int id_address, String penerima, String alamat, String province, String notelp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_ADDRESS, id_address);
        values.put(KEY_ADDRESS, alamat);
        values.put(KEY_PROVINCE, province);
        values.put(KEY_NOTELP, notelp);
        values.put(KEY_PENERIMA, penerima);
        long result = db.update(TABLE_ADDRESS, values, KEY_ID_USER+" = '"+id_user+"' ", null);
        if(result == -1){
            //jika tidak jalan
            Log.d("Error ===>","update alamat");
            return false;
        }else{
            Log.d("Success ===>","update alamat");
            return true;
        }
    }

    public Cursor checkAddres(int id_user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_ADDRESS +" where "+KEY_ID_USER+" = '"+id_user+"'",null);
        return cursor;
    }
    public List<object_address> getAddress(int id_user){
        List<object_address> listAddress = new ArrayList<object_address>();
        String sql = "select * from "+TABLE_ADDRESS+ " where "+KEY_ID_USER+"= '"+id_user+"' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                object_address ob = new object_address();
                ob.setId(Integer.valueOf(cursor.getString(2)));
                ob.setAddress(cursor.getString(3));
                ob.setProvinsi(cursor.getString(4));
                ob.setName(cursor.getString(5));
                ob.setNotelp(cursor.getString(6));

                listAddress.add(ob);
            }while (cursor.moveToNext());
        }

        return listAddress;
    }

    public Cursor checkBank(int id_user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_PAYMENT +" where "+KEY_ID_USER+"= '"+id_user+"' ", null);
        return cursor;
    }

    public Boolean insertBank(int id_user, int id_bank, String bank, String norek, String atasnama){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_USER, id_user);
        values.put(KEY_ID_BANK, id_bank);
        values.put(KEY_BANK, bank);
        values.put(KEY_NOREK, norek);
        values.put(KEY_ATAS_NAMA, atasnama);
        long result = db.insert(TABLE_PAYMENT, null, values);
        if(result == -1){
            //jika tidak jalan
            Log.d("Error ===>","insert bank");
            return false;
        }else{
            Log.d("Success ===>","insert bank");
            return true;
        }
    }

    public Boolean updateBank(int id_user, int id_bank, String bank, String norek, String atasnama){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_BANK, id_bank);
        values.put(KEY_BANK, bank);
        values.put(KEY_NOREK, norek);
        values.put(KEY_ATAS_NAMA, atasnama);

        long result = db.update(TABLE_PAYMENT, values, KEY_ID_USER+"= '"+id_user+"' ", null);
        if(result == -1){
            //jika tidak jalan
            Log.d("Error ===>","update bank");
            return false;
        }else{
            Log.d("Success ===>","update bank");
            return true;
        }
    }

    public List<object_bank> getBank(String id_user){
        List<object_bank> listBank = new ArrayList<object_bank>();
        String sql = "select * from "+TABLE_PAYMENT+" where "+KEY_ID_USER+"= '"+id_user+"' ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do {
                object_bank ob = new object_bank();
                ob.setId(Integer.valueOf(cursor.getString(2)));
                ob.setBank(cursor.getString(3));
                ob.setNorek(cursor.getString(4));
                ob.setAtasnama(cursor.getString(5));

                listBank.add(ob);
            }while(cursor.moveToNext());
        }

        return listBank;
    }

    public Boolean deleteAllProduct(int id_user){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_PRODUCT, KEY_ID_USER+"= '"+id_user+"' ", null);
        if(result == -1){
            Log.d("Delete product","====>faild");
            return false;
        }else{
            Log.d("Delete product","====>success");
            return true;
        }
    }
    public Boolean deleteAllAddress(int id_user){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_ADDRESS, KEY_ID_USER+"= '"+id_user+"' ", null);
        if(result == -1){
            Log.d("Delete product","====>faild");
            return false;
        }else{
            Log.d("Delete product","====>success");
            return true;
        }
    }

    public Boolean deleteAllBank(int id_user){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_PAYMENT, KEY_ID_USER+" = '"+id_user+"' ", null);
        if(result == -1){
            Log.d("Delete product","====>faild");
            return false;
        }else{
            Log.d("Delete product","====>success");
            return true;
        }
    }

}
