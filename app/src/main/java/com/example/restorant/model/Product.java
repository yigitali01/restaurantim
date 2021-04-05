package com.example.restorant.model;

import com.google.firebase.auth.FirebaseAuth;

public class Product {
    private String productName;
    private int productAmount;
    private int productPrice;
    private String productID;
    private String userid;
    private int choosedAmount;

    public Product(){

    }

    public Product(String productName, int productAmount, int productPrice,int choosedAmount,String productID) {
        this.productName = productName;
        this.productAmount = productAmount;
        this.productPrice = productPrice;
        this.productID = productID;
        this.choosedAmount = choosedAmount;
    }

    public String getProductID() {
        return productID;
    }

    public int getChoosedAmount() {
        return choosedAmount;
    }

    public void setChoosedAmount(int choosedAmount) {
        this.choosedAmount = choosedAmount;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", productAmount=" + productAmount +
                ", productPrice=" + productPrice +
                ", productID='" + productID + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
