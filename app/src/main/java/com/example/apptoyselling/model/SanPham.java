package com.example.apptoyselling.model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private int id;
    private String tenSP;
    private String hinhAnh;
    private String moTa;
    private float giaTien;
    private String thuongHieu;
    private String type;

    public SanPham(int id, String tenSP, String hinhAnh, String moTa, float giaTien, String thuongHieu, String type) {
        this.id = id;
        this.tenSP = tenSP;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.giaTien = giaTien;
        this.thuongHieu = thuongHieu;
        this.type = type;
    }

    public SanPham() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public float getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(float giaTien) {
        this.giaTien = giaTien;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
