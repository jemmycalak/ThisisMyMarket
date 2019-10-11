package com.jemmy.calak.camart.Model;

import java.util.ArrayList;

/**
 * Created by Jemmy Calak on 9/27/2017.
 */

public class object_order {

    int id_order;
    String no_invoice, tgl_order, status_order, almt, province, nm_penerima, bank, notelp, isVisible, shipping, total, norek;
    ArrayList<object_product> arraysectionProduct;

    public object_order(){

    }

    public void setIsVisible(String isVisible){
        this.isVisible=isVisible;
    }

    public String getIsVisible(){
        return isVisible;
    }

    public void setId_order(int id_order){
        this.id_order = id_order;
    }

    public int getId_order(){
        return id_order;
    }

    public void setShipping(String shipping){
        this.shipping = shipping;
    }

    public String getShipping(){
        return shipping;
    }

    public void setTotal(String total){
        this.total = total;
    }

    public String getTotal(){
        return total;
    }

    public void setStatus_order(String status_order){
        this.status_order = status_order;
    }

    public String getStatus_order(){
        return status_order;
    }

    public void setNo_invoice(String no_invoice){
        this.no_invoice = no_invoice;
    }

    public String getNo_invoice(){
        return no_invoice;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public void setTgl_order(String tgl_order){
        this.tgl_order = tgl_order;
    }

    public String getTgl_order(){
        return tgl_order;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getAlmt() {
        return almt;
    }

    public void setAlmt(String almt) {
        this.almt = almt;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNm_penerima() {
        return nm_penerima;
    }

    public void setNm_penerima(String nm_penerima) {
        this.nm_penerima = nm_penerima;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public ArrayList<object_product> getArraysectionProduct(){
        return arraysectionProduct;
    }

    public void setArraysectionProduct(ArrayList<object_product> arraysectionProduct){
        this.arraysectionProduct = arraysectionProduct;
    }
}
