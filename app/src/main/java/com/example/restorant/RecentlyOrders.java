package com.example.restorant;

import com.example.restorant.model.Basket;

import java.util.List;

public class RecentlyOrders {
    private String date;
    private List<Basket> basket;
    private String table;
    private int totalPrice;

    public RecentlyOrders() {
    }

    public RecentlyOrders(String date, List<Basket> basket, String table, int totalPrice) {
        this.date = date;
        this.basket = basket;
        this.table = table;
        this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Basket> getBasket() {
        return basket;
    }

    public void setBasket(List<Basket> basket) {
        this.basket = basket;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
