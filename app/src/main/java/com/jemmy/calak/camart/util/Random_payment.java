package com.jemmy.calak.camart.util;

import java.util.Random;

public class Random_payment {
    int hasil;

    public int angka(){

        Random random = new Random();
        hasil = random.nextInt(999);
        return hasil;
    }
}