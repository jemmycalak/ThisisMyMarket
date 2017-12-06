package com.example.jemmycalak.thisismymarket.util;

import java.util.Random;

public class Random_payment {
    int hasil;

    public int angka(){

        Random random = new Random();
        hasil = random.nextInt(999);
        return hasil;
    }
}