package com.shopper.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oleksandr.lezvinskyi
 * Date: 11/20/12
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductsListActivity extends Activity {
    Cursor c = null;
    DBShopper dbShopper;
    List<Product> model= new ArrayList<Product>();
    ProductAdapter adapter=null;
    String  cartID = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list);

        ListView list=(ListView)findViewById(R.id.products);

        adapter=new ProductAdapter();
        list.setAdapter(adapter);

        Intent intent = getIntent();
        cartID = intent.getStringExtra("cartID");

        dbShopper = new DBShopper(this);
        SQLiteDatabase db = dbShopper.getWritableDatabase();
       // String[] columns = new String[] {"productName","price"};

        String sqlQuery = "select PR.productName as productName, PR.price as productPrice, OD.amount as productAmount, OD.totalPrice as totalPrice "
                + "from products as PR "
                + "inner join orders as OD "
                + "on PR.productID = OD.productID "
                + "where OD.cartID == ?";

        c = db.rawQuery(sqlQuery, new String[] {cartID});
       // c = db.query("products", columns, null, null, null, null, "productID "+" DESC");
        if (c.moveToFirst()) {
            int productNameColIndex = c.getColumnIndex("productName");
            int priceColIndex = c.getColumnIndex("productPrice");
            int amountColIndex = c.getColumnIndex("productAmount");
            int totalPriceColIndex = c.getColumnIndex("totalPrice");
            do {
                String productName = c.getString(productNameColIndex) ;
                String productPrice = c.getString(priceColIndex) ;
                String productAmount = c.getString(amountColIndex) ;
                String totalPrice = c.getString(totalPriceColIndex);
                Product product = new Product(productName,productPrice,productAmount,totalPrice);
                adapter.add(product);

            } while (c.moveToNext());
        }
        dbShopper.close();





    }

    class ProductAdapter extends ArrayAdapter<Product> {
        ProductAdapter() {
            super(ProductsListActivity.this, R.layout.product_row, model);
        }
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row=convertView;
            ProductHolder holder=null;
            if (row==null) {
                LayoutInflater inflater=getLayoutInflater();
                row=inflater.inflate(R.layout.product_row, parent, false);
                holder=new ProductHolder(row);
                row.setTag(holder);
            }
            else {
                holder=(ProductHolder)row.getTag();
            }
            holder.populateFrom(model.get(position));
            return(row);
        }
    }

    static class ProductHolder {
        private TextView name=null;
        private TextView amount = null;
        private TextView price=null;
        private TextView hallPrice=null;

        ProductHolder(View row) {
            name=(TextView)row.findViewById(R.id.productNameRow);
            amount=(TextView)row.findViewById(R.id.productAmountRow);
            price=(TextView)row.findViewById(R.id.productPriceRow);
            hallPrice=(TextView)row.findViewById(R.id.productTotalPrRow);

        }
        void populateFrom(Product pr) {
            name.setText(pr.getProductName());
            amount.setText(pr.getAmount());
            price.setText(pr.getPrice());
            hallPrice.setText(pr.getTotalPrice());
        }
    }
}