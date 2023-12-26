package com.example.apptoyselling.model;

public class DonHang {
    private String idDH;
    private String nameDH;
    private String phoneDH;
    private String diachiDH;
    private float priceDH;
    private String statusDH;
    private String date;
    private int id;
    private String payment;

    public DonHang(String idDH, String nameDH, String phoneDH, String diachiDH, float priceDH, String statusDH, String date,int id,String payment) {
        this.idDH = idDH;
        this.nameDH = nameDH;
        this.phoneDH = phoneDH;
        this.diachiDH = diachiDH;
        this.priceDH = priceDH;
        this.statusDH = statusDH;
        this.date = date;
        this.id = id;
        this.payment = payment;
    }

    public DonHang() {
    }

    public String getIdDH() {
        return idDH;
    }

    public void setIdDH(String idDH) {
        this.idDH = idDH;
    }

    public String getNameDH() {
        return nameDH;
    }

    public void setNameDH(String nameDH) {
        this.nameDH = nameDH;
    }

    public String getPhoneDH() {
        return phoneDH;
    }

    public void setPhoneDH(String phoneDH) {
        this.phoneDH = phoneDH;
    }

    public String getDiachiDH() {
        return diachiDH;
    }

    public void setDiachiDH(String diachiDH) {
        this.diachiDH = diachiDH;
    }

    public float getPriceDH() {
        return priceDH;
    }

    public void setPriceDH(float priceDH) {
        this.priceDH = priceDH;
    }

    public String getStatusDH() {
        return statusDH;
    }

    public void setStatusDH(String statusDH) {
        this.statusDH = statusDH;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
