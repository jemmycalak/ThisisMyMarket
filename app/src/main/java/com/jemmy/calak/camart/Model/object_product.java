package com.jemmy.calak.camart.Model;

/**
 * Created by Jemmy Calak on 12/6/2016.
 * object untuk laptop layout, activity_keranjang layout
 */

public class object_product {

    String nama, desc, imgUrl, color, category, imgCat,brt,id, hrg, jmlh, stock;

    public object_product(){
//        buat contruktor kosong untuk di panggil manual atau di isi manual
    }

    public object_product(String id, String name, String hrga, String imgUrl, String descrip, String brt, String clr, String stock) {
        this.nama = name;
        this.imgUrl = imgUrl;
        this.hrg = hrga;
        this.desc = descrip;
        this.id = id;
        this.brt = brt;
        this.color = clr;
        this.stock = stock;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHrg() {
        return hrg;
    }

    public void setHrg(String hrg) {
        this.hrg = hrg;
    }

    public String getJmlh() {
        return jmlh;
    }

    public void setJmlh(String jmlh) {
        this.jmlh = jmlh;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgCat() {
        return imgCat;
    }

    public void setImgCat(String imgCat) {
        this.imgCat = imgCat;
    }

    public String getBrt() {
        return brt;
    }

    public void setBrt(String brt) {
        this.brt = brt;
    }
}