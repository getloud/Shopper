package com.shopper.app;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

/**
 * Created with IntelliJ IDEA.
 * User: oleksandr.lezvinskyi
 * Date: 11/13/12
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BarcodeScannerActivity extends Activity implements ScanditSDKListener {
    // The main object for scanning barcodes.

    private ScanditSDKBarcodePicker mBarcodePicker = null;
    TextView barcodeValue = null;
    EditText productName = null;
    DBShopper dbShopper;
    Cursor c;

    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    private static final String sScanditSdkAppKey = String.valueOf(R.string.app_key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //screen always turn on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.scanned_barcode_layout);
        RelativeLayout rlay  = (RelativeLayout) findViewById(R.id.barcodepicker);
       barcodeValue = (TextView) findViewById(R.id.barcode);
        productName = (EditText) findViewById(R.id.productName);

        mBarcodePicker = new ScanditSDKBarcodePicker(BarcodeScannerActivity.this, sScanditSdkAppKey);
        // Register listener, in order to be notified about relevant events
        // (e.g. a recognized bar code).
        mBarcodePicker.getOverlayView().addListener(BarcodeScannerActivity.this);
        rlay.addView(mBarcodePicker);
        mBarcodePicker.setScanningHotSpot(0.5f, 0.25f);
        mBarcodePicker.getOverlayView().setInfoBannerY(0.4f);
        mBarcodePicker.startScanning();

        dbShopper = new DBShopper(this);



    }


    /**
     * Creates a button that shows how to add a cropped version of the full
     * screen Scandit SDK scanner. The cropping is accomplished by overlaying
     * parts of the scanner with an opaque view.
     *
     *
     * @return Button that places a scanner on rootView.
     */
//     private Button createCroppedOverlayButton(final RelativeLayout rootView) {
//       Button button = new Button(this);
//        button.setText("Scan");
//       RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
//               ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        rParams.bottomMargin = 10;
//        rootView.addView(button, rParams);
//    button.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            RelativeLayout.LayoutParams rParams;
//
//   mBarcodePicker = new ScanditSDKBarcodePicker(BarcodeScannerActivity.this, sScanditSdkAppKey);
//
//            // Register listener, in order to be notified about relevant events
//            // (e.g. a recognized bar code).
//            mBarcodePicker.getOverlayView().addListener(HomeActivity.this);
//
//            rParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
//            rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            rParams.bottomMargin = 20;
//            rootView.addView(mBarcodePicker, rParams);


//
//
//            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//            Display display = wm.getDefaultDisplay();
//
//            TextView overlay = new TextView(HomeActivity.this);
//            rParams = new RelativeLayout.LayoutParams(
//                    ViewGroup.LayoutParams.FILL_PARENT, display.getHeight() / 2);
//            rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//            overlay.setBackgroundColor(0xFF000000);
//            rootView.addView(overlay, rParams);
//
//
//
//            overlay.setText("Touch to close");
//            overlay.setGravity(Gravity.CENTER);
//            overlay.setTextColor(0xFFFFFFFF);
//            overlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    rootView.removeView(v);
//                    rootView.removeView(mBarcodePicker);
//                    mBarcodePicker.stopScanning();
//                    mBarcodePicker.setScanningHotSpot(0.5f, 0.5f);
//                    mBarcodePicker = null;
//                }
//            });
//            mBarcodePicker.setScanningHotSpot(0.5f, 0.25f);
//            mBarcodePicker.getOverlayView().setInfoBannerY(0.4f);
//            mBarcodePicker.startScanning();
//        }
//    });
//     Must be able to run the portrait version for this button to work.
//    if (!ScanditSDKBarcodePicker.canRunPortraitPicker()) {
//        button.setEnabled(false);
//    }
//
//       return button;
//    }

    @Override
    protected void onPause() {
        // When the activity is in the background immediately stop the
        // scanning to save resources and free the camera.
        if (mBarcodePicker != null) {
            mBarcodePicker.stopScanning();
        }
//        LocationManager locationManager =
//            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.removeUpdates(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Once the activity is in the foreground again, restart scanning.
        if (mBarcodePicker != null) {
            mBarcodePicker.startScanning();
        }
        super.onResume();
    }

    /**
     *  Called when a barcode has been decoded successfully.
     *
     *  @param barcode Scanned barcode content.
     *  @param symbology Scanned barcode symbology.
     */
    public void didScanBarcode(String barcode, String symbology) {
        SQLiteDatabase  db = dbShopper.getWritableDatabase();
        String LOG_TAG = "my log!!!!!!!!!!!!";
        Log.d(LOG_TAG, "id = " + db.findEditTable("products") );

         c = db.query("products", null, null, null, null, null, null);
        Log.d(LOG_TAG, "id = " +c );
        if (c.getCount() == 0) {  productName.setText("NO");
            Log.d(LOG_TAG, "no content" );
        }
        barcodeValue.setText(barcode);
       String selection = "barcodeNumber == ?";
       String column = "productName" ;
        String[] columns = new String[] {column};
       String[] selectionArgs = new String[] {barcode};
        c = db.query("products", columns, selection, selectionArgs, null, null, null);
        String name ="";
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("productName");
            String name1 ="";
            do {
                      name= c.getString(nameColIndex) ;
                productName.setText(name);

            } while (c.moveToNext());
        } else
        {productName.setText("NO");     }

        dbShopper.close();




       // mBarcodePicker.stopScanning();

    }

    /**
     * Called when the user entered a bar code manually.
     *
     * @param entry The information entered by the user.
     */
    public void didManualSearch(String entry) {
        // Example code that would typically be used in a real-world app using
        // the Scandit SDK.
        /*
          // Access the current camera image.
          byte[] imageDataNV21Encoded = barcodePicker.getMostRecentCameraPreviewImage();
          int imageWidth = barcodePicker.getCameraPreviewImageWidth();
          int imageHeight = barcodePicker.getCameraPreviewImageHeight();

          // Stop recognition to save resources.
          mBarcodePicker.stopScanning();
          */
    }

    @Override
    public void didCancel() {
    }

    @Override
    public void onBackPressed() {
        if (mBarcodePicker != null) {
            mBarcodePicker.stopScanning();
        }
        finish();
    }

    /**
     * Starts to gather location data from the finest allowed location provider.
     * For this to work either one of the following permissions has to be set:
     * ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION.
     */
//	private void startLocationGathering() {
//        LocationManager locationManager =
//            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        String provider = locationManager.getBestProvider(criteria, true);
//        if (provider != null) {
//            locationManager.requestLocationUpdates(provider, 5, 5, this);
//        }
//	}
//
//	@Override
//	public void onLocationChanged(Location arg0) {
//        LocationManager locationManager =
//            (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.removeUpdates(this);
//	}

//	@Override
//	public void onProviderDisabled(String arg0) {}
//
//	@Override
//	public void onProviderEnabled(String arg0) {}
//
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
}
