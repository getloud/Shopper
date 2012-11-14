package com.shopper.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;

public class HomeActivity extends Activity
{
    // The main object for scanning barcodes.
    private ScanditSDKBarcodePicker mBarcodePicker;
    Button startShopping = null;
    Button history = null;
    Button settings = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startShopping=(Button)findViewById(R.id.startSh);
        history=(Button)findViewById(R.id.historySh);
        settings=(Button)findViewById(R.id.settings);


        startShopping.setOnClickListener(onSave);
     //   RelativeLayout rootView = new RelativeLayout(this);


//		Create the buttons to start scanner examples.
     //   createCroppedOverlayButton(rootView);
//
     //   setContentView(rootView);
    }

    private View.OnClickListener onSave=new View.OnClickListener() {
        Intent intent;

        @Override
        public void onClick(View view) {
            intent = new Intent(HomeActivity.this, BarcodeScannerActivity.class);
            startActivity(intent);
        }
    };



}
