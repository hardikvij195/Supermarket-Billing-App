package com.hvtechnologies.supermarketbillingsystem;

public class ViewItemsClass {


    String Name , Price , Id ;

    public ViewItemsClass(String name, String price, String id) {

        Name = name;
        Price = price;
        Id = id;

    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
