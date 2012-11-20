package com.shopper.app;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * Created with IntelliJ IDEA.
 * User: oleksandr.lezvinskyi
 * Date: 11/20/12
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class CartTabsActivity extends TabActivity {
    TabHost tabHost;
    String cartID;
    Intent intent;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_tabwidget);

        intent = getIntent();
        cartID = intent.getStringExtra("cartID");


        Intent intent1 = new Intent(this, ScannerActivity.class);
        intent1.putExtra("cartID", cartID);

       Intent intent2 =  new Intent(this,ProductsListActivity.class);
       intent2.putExtra("cartID", cartID);

        /** TabHost will have Tabs */
        tabHost = (TabHost)findViewById(android.R.id.tabhost);

        /** TabSpec used to create a new tab.
         * By using TabSpec only we can able to setContent to the tab.
         * By using TabSpec setIndicator() we can set name to tab. */

        /** tid1 is firstTabSpec Id. Its used to access outside. */
        TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
        TabSpec secondTabSpec = tabHost.newTabSpec("tid2");

        /** TabSpec setIndicator() is used to set name for the tab. */
        /** TabSpec setContent() is used to set content for a particular tab. */
        firstTabSpec.setIndicator("Scanner", getResources().getDrawable(R.drawable.list)).setContent(intent1);
        secondTabSpec.setIndicator("Products", getResources().getDrawable(R.drawable.restaurant)).setContent(intent2);

        /** Add tabSpec to the TabHost to display. */
        tabHost.addTab(firstTabSpec);
        tabHost.addTab(secondTabSpec);

        getTabHost().setCurrentTab(0);
    }
}