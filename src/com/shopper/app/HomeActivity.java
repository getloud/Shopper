package com.shopper.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends Activity implements View.OnClickListener {
    // The main object for scanning barcodes.
    Button startShopping = null;
    Button history = null;
    Button settings = null;
    PopupWindow mpopup;
    Intent intentCartTab;
    Intent intentHistory;
    DBShopper dbShopper;
    SQLiteDatabase db;
    String cartID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startShopping = (Button) findViewById(R.id.startSh);
        history = (Button) findViewById(R.id.historySh);
        settings = (Button) findViewById(R.id.settings);


        startShopping.setOnClickListener(this);
        history.setOnClickListener(this);
    }

    // private View.OnClickListener createCart=new View.OnClickListener() {


    @Override
    public void onClick(View view) {
        dbShopper = new DBShopper(HomeActivity.this);
        db = dbShopper.getWritableDatabase();
        switch (view.getId()) {

            case R.id.startSh:

                Cursor dateCursor = null;

                String selection = "endDate is null";
                String column = "cartID";
                String[] columns = new String[]{column};
                dateCursor = db.query("carts", columns, selection, null, null, null, null);

                if (dateCursor.moveToFirst()) {
                    int idColIndex = dateCursor.getColumnIndex("cartID");
                    cartID = dateCursor.getString(idColIndex);
//
//                      intentCartTab = new Intent(HomeActivity.this, CartTabsActivity.class);
//                      intentCartTab.putExtra("cartID", cartID);
//                      startActivity(intentCartTab);

                    final View popUpWinExistCart = getLayoutInflater().inflate(R.layout.cart_exist_layout, null); // inflating popup layout
                    mpopup = new PopupWindow(popUpWinExistCart, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true); //Creation of popup
                    mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                    mpopup.setClippingEnabled(true);
                    mpopup.showAtLocation(popUpWinExistCart, Gravity.CENTER, 0, 0);    // Displaying popup


                    Button btnAdd = (Button) popUpWinExistCart.findViewById(R.id.continueSh);
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            intentCartTab = new Intent(HomeActivity.this, CartTabsActivity.class);
                            intentCartTab.putExtra("cartID", cartID);
                            startActivity(intentCartTab);
                            mpopup.dismiss();
                        }
                    });

                    Button btnCancel = (Button) popUpWinExistCart.findViewById(R.id.finishSh);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            Date date = new Date();

                            ContentValues cv = new ContentValues();

                            cv.put("endDate", dateFormat.format(date));
                            db.update("carts", cv, "cartID == ?", new String[]{cartID});


                            ProductsListActivity.tMoney = 0;
                            ProductsListActivity.restMoney = 0;
                            dbShopper.close();
                            mpopup.dismiss();
                        }
                    });


                } else

                {

                    final View popUpView = getLayoutInflater().inflate(R.layout.create_cart_window, null); // inflating popup layout
                    mpopup = new PopupWindow(popUpView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true); //Creation of popup
                    mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                    mpopup.setClippingEnabled(true);
                    mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);    // Displaying popup


                    Button btnAdd = (Button) popUpView.findViewById(R.id.save);
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText cartName = (EditText) popUpView.findViewById(R.id.cartName);
                            EditText money = (EditText) popUpView.findViewById(R.id.money);

                            String name = cartName.getText().toString();
                            double cash = Double.parseDouble(money.getText().toString());
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            Date date = new Date();

                            dbShopper = new DBShopper(HomeActivity.this);
                            db = dbShopper.getWritableDatabase();

                            ContentValues cv = new ContentValues();


                            cv.put("cartName", name);
                            cv.put("money", cash);
                            cv.put("startDate", dateFormat.format(date));

                            long rowID = db.insert("carts", null, cv);
                            Log.d("my_logs", "row inserted, ID = " + rowID);

                            dbShopper.close();

                            intentCartTab = new Intent(HomeActivity.this, CartTabsActivity.class);
                            intentCartTab.putExtra("cartID", String.valueOf(rowID));
                            startActivity(intentCartTab);
                            mpopup.dismiss();
                        }
                    });

                    Button btnCancel = (Button) popUpView.findViewById(R.id.cancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mpopup.dismiss();
                        }
                    });
                }
                break;
            case R.id.historySh:
                intentHistory = new Intent(HomeActivity.this, CartsListActivity.class);
                startActivity(intentHistory);
                break;

        }
    }
    // };


}
