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
    private String startDate;

    public String getCartID() {
        return cartID;
    }

    public void setcartID(String cartID) {
        cartID = cartID;
    }

    private String cartID;

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    private String finishDate;

    public Cart( String cartID, String cartName, String startDate, String finishDate) {
        this.cartID = cartID;
        this.cartName = cartName;
        this.startDate = startDate;
        this.finishDate = finishDate;

    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
