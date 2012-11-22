package com.shopper.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lezvinskyi
 * Date: 17.11.12
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class CartsListActivity extends Activity {
    Cursor c = null;
    DBShopper dbShopper;
    List<Cart> model = new ArrayList<Cart>();
    CartAdapter adapter = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list);

        ListView list = (ListView) findViewById(R.id.carts);

        adapter = new CartAdapter();
        list.setAdapter(adapter);

        dbShopper = new DBShopper(this);
        SQLiteDatabase db = dbShopper.getWritableDatabase();
        String[] columns = new String[]{"cartID", "cartName", "startDate", "endDate"};
        c = db.query("carts", columns, null, null, null, null, "startDate " + " DESC");

        if (c.moveToFirst()) {
            int cartIDColIndex = c.getColumnIndex("cartID");
            int cartNameColIndex = c.getColumnIndex("cartName");
            int startDateColIndex = c.getColumnIndex("startDate");
            int finishDateColIndex = c.getColumnIndex("endDate");
            do {
                String cartID = c.getString(cartIDColIndex);
                String name = c.getString(cartNameColIndex);
                String sDate = c.getString(startDateColIndex);
                String fDate = c.getString(finishDateColIndex);
                Cart cart = new Cart(cartID, name, sDate, fDate);
                adapter.add(cart);

            } while (c.moveToNext());
        }
        dbShopper.close();

        list.setOnItemClickListener(onListClick);


    }

    private AdapterView.OnItemClickListener onListClick = new
            AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position,
                                        long id) {
                    Cart c = model.get(position);

                    if (c.getFinishDate().isEmpty()){}
                    else
                    {
                        Intent intent = new Intent(CartsListActivity.this, ProductsListActivity.class);
                        intent.putExtra("cartID", String.valueOf(c.getCartID()));
                        startActivity(intent);
                    }
                }
            };

    class CartAdapter extends ArrayAdapter<Cart> {
        CartAdapter() {
            super(CartsListActivity.this, R.layout.cart_row, model);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;
            CartHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.cart_row, parent, false);
                holder = new CartHolder(row);
                row.setTag(holder);
            } else {
                holder = (CartHolder) row.getTag();
            }
            holder.populateFrom(model.get(position));
            return (row);
        }
    }

    static class CartHolder {
        private TextView name = null;
        private TextView startDate = null;
        private TextView finishDate = null;
        private ImageView icon = null;

        CartHolder(View row) {
            name = (TextView) row.findViewById(R.id.cartNameRow);
            startDate = (TextView) row.findViewById(R.id.startShoppingRow);
            finishDate = (TextView) row.findViewById(R.id.finishShoppingRow);
            icon = (ImageView) row.findViewById(R.id.icon);
        }

        void populateFrom(Cart cr) {
            name.setText(cr.getCartName());
            startDate.setText(cr.getStartDate());
            finishDate.setText(cr.getFinishDate());
            icon.setImageResource(R.drawable.ball_green);
        }
    }
}