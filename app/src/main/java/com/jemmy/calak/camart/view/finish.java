package com.jemmy.calak.camart.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jemmy.calak.camart.MainActivity;
import com.jemmy.calak.camart.R;
import com.jemmy.calak.camart.util.formatNominal;

public class finish extends AppCompatActivity {

    private Button fnsh;
    private Toolbar tolbar;
    private TextView norek, atasNama, nominal;
    private formatNominal formatNominal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish);
        formatNominal = new formatNominal();
        init();
        setData();
        fnsh=(Button)findViewById(R.id.finisBT);
        fnsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //after logout redirect user to login activity
                Intent i= new Intent(finish.this, MainActivity.class);

                //closing all activity
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //add new flag to start activity

                startActivity(i);
                finish();

            }
        });
    }

    private void setData() {
        Bundle data= getIntent().getExtras();
        String nomina = data.getString("nominal");
        norek.setText(": "+data.getString("norek"));
        atasNama.setText(": "+data.getString("atasnama"));
        nominal.setText(": Rp."+formatNominal.formatNumber(Integer.parseInt(nomina))+",-");
        Log.d("nominal",data.getString("nominal")+",-");
    }

    private void init() {
            norek = (TextView)findViewById(R.id.norekFinis);
            atasNama = (TextView)findViewById(R.id.atasnamaFinis);
            nominal = (TextView)findViewById(R.id.nominalFinis);

    }


    //ketika di tekan tombol back
    public void onBackPressed() {

        //after logout redirect user to login activity
        Intent i= new Intent(finish.this, MainActivity.class);

        //closing all activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //add new flag to start activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(i);
        finish();

    }
}
