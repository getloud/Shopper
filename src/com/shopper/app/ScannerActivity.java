package com.shopper.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: oleksandr.lezvinskyi
 * Date: 11/20/12
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScannerActivity extends Activity implements ScanditSDKListener, View.OnClickListener {

    private ScanditSDKBarcodePicker mBarcodePicker = null;
    EditText productBarcode = null;
    EditText productName = null;
    EditText productPrice = null;
    EditText amount = null;
    Cursor c = null;
    Button addProduct = null;
    Button finishShop = null;
    String cartID = "";
    String productID = "";

    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    private static final String sScanditSdkAppKey = String.valueOf(R.string.app_key);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //screen always turn on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.scanner_layout);
        RelativeLayout rlay = (RelativeLayout) findViewById(R.id.barcodepicker);
        productBarcode = (EditText) findViewById(R.id.barcode);
        productName = (EditText) findViewById(R.id.productName);
        productPrice = (EditText) findViewById(R.id.price);
        amount = (EditText) findViewById(R.id.amount);
        addProduct = (Button) findViewById(R.id.addProduct);
        finishShop = (Button) findViewById(R.id.finishShopping);


        mBarcodePicker = new ScanditSDKBarcodePicker(ScannerActivity.this, sScanditSdkAppKey);
        // Register listener, in order to be notified about relevant events
        // (e.g. a recognized bar code).
        mBarcodePicker.getOverlayView().addListener(ScannerActivity.this);
        rlay.addView(mBarcodePicker);
        mBarcodePicker.setScanningHotSpot(0.5f, 0.3f);
        mBarcodePicker.getOverlayView().setInfoBannerY(0.1f);
        mBarcodePicker.startScanning();

        addProduct.setOnClickListener(this);
        finishShop.setOnClickListener(this);

        Intent intent = getIntent();
        cartID = intent.getStringExtra("cartID");
    }

    @Override
    public void onClick(View view) {
        DBShopper dbShopper = new DBShopper(this);
        SQLiteDatabase db = dbShopper.getWritableDatabase();
        switch (view.getId()) {
            case R.id.addProduct:

                String selection = "cartID == ? and productID == ?";
                String column = "productID, orderID, amount";
                String[] columns = new String[]{column};
                String[] selectionArgs = new String[]{cartID, productID};
                c = db.query("orders", columns, selection, selectionArgs, null, null, null);

                if (c.moveToFirst()) {

                    int idPrColIndex = c.getColumnIndex("productID");
                    int idOrColIndex = c.getColumnIndex("orderID");
                    int amColIndex = c.getColumnIndex("amount");

                    do {

                        String prdID = c.getString(idPrColIndex);
                        String orID = c.getString(idOrColIndex);
                        int am = Integer.parseInt(c.getString(amColIndex));

                        ContentValues contentV = new ContentValues();
                        contentV.put("amount", am + Integer.parseInt(amount.getText().toString()));
                        contentV.put("totalPrice", (am + Integer.parseInt(amount.getText().toString())) * Double.parseDouble(productPrice.getText().toString()));
                        int updCount = db.update("orders", contentV, "orderID = ? and productID == ?", new String[]{orID, prdID});
                       productBarcode.setText("");
                        productName.setText("");
                        productPrice.setText("");
                        amount.setText("");


                    } while (c.moveToNext());


                } else {
                    ContentValues cv = new ContentValues();

                    int productAmount = Integer.parseInt(amount.getText().toString());
                    double totalPrice = ((double) (productAmount) * Double.parseDouble(productPrice.getText().toString()));

                    cv.put("cartID", cartID);
                    cv.put("productID", productID);
                    cv.put("amount", productAmount);
                    cv.put("totalPrice", totalPrice);
                    db.insert("orders", null, cv);

                    productBarcode.setText("");
                    productName.setText("");
                    productPrice.setText("");
                    amount.setText("");

                }
                break;
            case R.id.finishShopping:
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                Date date = new Date();

                ContentValues cv = new ContentValues();

                cv.put("endDate", dateFormat.format(date));
                db.update("carts", cv, "cartID == ?", new String[]{cartID});

//                TextView chkout = (TextView)findViewById(R.id.checkOut);
//                TextView rest = (TextView)findViewById(R.id.rest);
//                chkout.setText("0");
//                rest.setText("0");
                ProductsListActivity.tMoney = 0;
                ProductsListActivity.restMoney = 0;
                dbShopper.close();
                finish();

        }
        dbShopper.close();
    }

    @Override
    public void didCancel() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

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
     * Called when a barcode has been decoded successfully.
     *
     * @param barcode   Scanned barcode content.
     * @param symbology Scanned barcode symbology.
     */
    public void didScanBarcode(String barcode, String symbology) {
        DBShopper dbShopper = new DBShopper(this);
        SQLiteDatabase db = dbShopper.getWritableDatabase();

        productBarcode.setText(barcode);
        String selection = "barcodeNumber == ?";
        String column = "productID, productName, price";
        String[] columns = new String[]{column};
        String[] selectionArgs = new String[]{barcode};
        c = db.query("products", columns, selection, selectionArgs, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("productID");
            int nameColIndex = c.getColumnIndex("productName");
            int priceColIndex = c.getColumnIndex("price");
            do {

                productID = c.getString(idColIndex);
                productName.setText(c.getString(nameColIndex));
                productPrice.setText(c.getString(priceColIndex));
                amount.setText("");


            } while (c.moveToNext());
            ProductsListActivity.tMoney = 0;
            ProductsListActivity.restMoney = 0;
        } else {
            productName.setText("NO prod");
        }

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
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }
}
