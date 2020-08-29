package com.hvtechnologies.supermarketbillingsystem;

public class ItemsQtyRequired {

    String Name , Id , Price , MarketName , MarketId ;

    double Qty ;

    public ItemsQtyRequired(String name, String id, String price, String marketName, String marketId, double qty) {
        Name = name;
        Id = id;
        Price = price;
        MarketName = marketName;
        MarketId = marketId;
        Qty = qty;
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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
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

    public double getQty() {
        return Qty;
    }

    public void setQty(double qty) {
        Qty = qty;
    }
}
