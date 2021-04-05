package com.example.restorant.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Table {
    private String no;
    private String userid;
    private Boolean tableStatus;



    private HashMap<String, Integer> basketProductList =
            new HashMap<String, Integer>();

    public Table() {
        // basketProductList = new HashMap();
    }

    public Table(String no, String userid,Boolean tableStatus) {
        this.no = no;
        this.userid = userid;
        this.tableStatus = tableStatus;
    //    this.testStr = tt;
    //  this.basketProductList = hm;
    }

    public HashMap<String, Integer> getBasketProductList()
    {
        return basketProductList;
    }

    public void setBasketProductList(HashMap<String, Integer> basketProductList)
    {
        this.basketProductList = basketProductList;
    }

    public String getNo()
    {
        return no;
    }

    public void setNo(String no)
    {
        this.no = no;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public Boolean getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(Boolean tableStatus) {
        this.tableStatus = tableStatus;
    }

    // TEST SCENARIO:
    public void addProductList ()
    {
        basketProductList.put("product1",20);
        basketProductList.put("product2",25);
        basketProductList.put("product3",30);
    }

    public void getProductList ()
    {
        Iterator hmIterator = basketProductList.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)hmIterator.next();
            int marks = ((int)mapElement.getValue() + 10);
            System.out.println("Key : "+mapElement.getKey() + " Value : " + marks);
        }
    }

    @Override
    public String toString()
    {
        return "Table{" +
                "no='" + no + '\'' +
                ", userid='" + userid + '\'' +
                ", basketProductList=" + basketProductList +
                '}';
    }
}
