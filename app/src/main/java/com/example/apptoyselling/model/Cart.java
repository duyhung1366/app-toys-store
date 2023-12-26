package com.example.apptoyselling.model;

public class Cart {
    private int idCart;
    private String imgCart;
    private String nameCart;
    private float priceCart;
    private String desCart;
    private String originCart;
    private int numberOrder;
    private float sumPrice;

    public Cart(int idCart, String imgCart, String nameCart, float priceCart, String desCart, String originCart, int numberOrder, float sumPrice) {
        this.idCart = idCart;
        this.imgCart = imgCart;
        this.nameCart = nameCart;
        this.priceCart = priceCart;
        this.desCart = desCart;
        this.originCart = originCart;
        this.numberOrder = numberOrder;
        this.sumPrice = sumPrice;
    }

    public Cart() {
    }

    public int getIdCart() {
        return idCart;
    }

    public void setIdCart(int idCart) {
        this.idCart = idCart;
    }

    public String getNameCart() {
        return nameCart;
    }

    public void setNameCart(String nameCart) {
        this.nameCart = nameCart;
    }

    public String getImgCart() {
        return imgCart;
    }

    public void setImgCart(String imgCart) {
        this.imgCart = imgCart;
    }

    public float getPriceCart() {
        return priceCart;
    }

    public void setPriceCart(float priceCart) {
        this.priceCart = priceCart;
    }

    public String getDesCart() {
        return desCart;
    }

    public void setDesCart(String desCart) {
        this.desCart = desCart;
    }

    public String getOriginCart() {
        return originCart;
    }

    public void setOriginCart(String originCart) {
        this.originCart = originCart;
    }

    public int getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(int numberOrder) {
        this.numberOrder = numberOrder;
    }

    public float getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(float sumPrice) {
        this.sumPrice = sumPrice;
    }
}
