package com.example.jemmycalak.thisismymarket.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jemmycalak.thisismymarket.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        init();
    }

    private void init() {
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
