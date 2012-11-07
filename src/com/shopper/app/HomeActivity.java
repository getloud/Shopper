package com.shopper.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.Toast;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

public class HomeActivity extends Activity implements OnClickListener, ScanditSDKListener {
    Button startSh = null;
    Button historySh = null;
    Button settings = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        startSh = (Button) findViewById(R.id.startSh);
        historySh = (Button) findViewById(R.id.historySh);
        settings = (Button) findViewById(R.id.settings);


        startSh.setOnClickListener(this);
        historySh.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.startSh:
                Toast.makeText(this, "Start shopping", Toast.LENGTH_LONG).show();
                break;
            case R.id.historySh:
                Toast.makeText(this, "Get history", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "Change settings", Toast.LENGTH_LONG).show();
        }

    }




}

