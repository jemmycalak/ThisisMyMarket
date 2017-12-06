package com.example.jemmycalak.thisismymarket.util;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class formatNominal {
    public String formatNumber(int harga) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(""); // Don't use null.
        formatter.setDecimalFormatSymbols(symbols);
        String value = formatter.format(harga);
        Log.d("Format nominal","===>"+value);
        return value;
    }
}