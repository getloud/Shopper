package com.shopper.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: oleksandr.lezvinskyi
 * Date: 11/15/12
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBShopper  extends SQLiteOpenHelper {

    int[] productID = { 1, 2, 3, 4 };
    String[] productName = { "Lipton tea", "растишка", "Twix", "Nutella" };
    String[] barcodeNumber = { "4016032213093", "4016032213094", "4016032213095", "4016032213096" };
    float[] price = {8.5f, 3.0f, 5.3f, 3.60f };
    String[] measurmentUnit = {"item","item","item","item"};
    int[] discount = {0, 0, 0, 0};
    final String LOG_TAG = "myLogs";

    public DBShopper(Context context) {
        //конструктор суперкласса
         super(context, "DBShopper", null, 1);
      }



    @Override
    public void onCreate(SQLiteDatabase db) {
               Log.d(LOG_TAG, "--- onCreate database ---");
// создаем таблицу с полями
        ContentValues cv = new ContentValues();


// создаем таблицу должностей
        db.execSQL("create table products ("
                + "productID integer primary key,"
                + "productName text,"
                + "barcodeNumber integer"
                + "price float"
                + "measurmentUnit text,"
                + "discount integer"
                + ");");
// заполняем ее
        for (int i = 0; i < productID.length; i++) {
            cv.clear();
            cv.put("productID", productID[i]);
            cv.put("productName", productName[i]);
            cv.put("barcodeNumber", barcodeNumber[i]);
            cv.put("price", price[i]);
            cv.put("measurmentUnit", measurmentUnit[i]);
            cv.put("discount", discount[i]);
            db.insert("products", null, cv);
        }
    }
//// создаем таблицу людей
//        db.execSQL("create table people ("
//                + "id integer primary key autoincrement,"
//                + "name text,"
//                + "posid integer"
//                + ");");
//// заполняем ее
//        for (int i = 0; i < people_name.length; i++) {
//            cv.clear();
//            cv.put("name", people_name[i]);
//            cv.put("posid", people_posid[i]);
//            db.insert("people", null, cv);
//        }
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

