package com.example.restorant.model;

public class Basket {
    private String product;
    private int amount;
    private int price;
    private int totalPrice;


    public Basket() {

    }



    public Basket(String product, int amount, int price) {
        this.product = product;
        this.amount = amount;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalPrice() {
        return amount*price;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "basketProduct='" + product + '\'' +
                ", choosedAmount=" + amount +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
