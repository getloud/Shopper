package com.shopper.app;

/**
 * Created with IntelliJ IDEA.
 * User: Lezvinskyi
 * Date: 17.11.12
 * Time: 15:53
 * To change this template use File | Settings | File Templates.
 */
public class Cart {
    private String cartName;
    private String date;

    public Cart(String cartName, String date) {
        this.cartName = cartName;
        this.date = date;
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
