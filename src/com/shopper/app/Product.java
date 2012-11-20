package com.shopper.app;

/**
 * Created with IntelliJ IDEA.
 * User: oleksandr.lezvinskyi
 * Date: 11/20/12
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Product {
    private String productName;
    private String amount;
    private String price;
    private String totalPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }



    public Product(String productName, String price, String amount, String totalPrice) {
        this.totalPrice = totalPrice;
        this.price = price;
        this.amount = amount;
        this.productName = productName;
    }




}
