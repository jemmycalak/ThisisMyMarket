package com.jemmy.calak.camart.Model;

/**
 * Created by Jemmy Calak on 9/1/2017.
 */

public class object_bank {
    int id;
    String bank, img_bank, atasnama,norek;

    public object_bank(){

    }

    public void setAtasnama(String atasnama){
        this.atasnama = atasnama;
    }

    public String getAtasnam(){
        return atasnama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getImg_bank() {
        return img_bank;
    }

    public void setImg_bank(String img_bank) {
        this.img_bank = img_bank;
    }
}
