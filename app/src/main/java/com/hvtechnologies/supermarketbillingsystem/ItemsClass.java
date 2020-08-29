package com.hvtechnologies.supermarketbillingsystem;

public class ItemsClass {


    String Name , Id , Qty , MarketName , MarketId ;


    public ItemsClass(String name, String id, String qty, String marketName, String marketId) {
        Name = name;
        Id = id;
        Qty = qty;
        MarketName = marketName;
        MarketId = marketId;
    }


    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public String getMarketId() {
        return MarketId;
    }

    public void setMarketId(String marketId) {
        MarketId = marketId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }
}
