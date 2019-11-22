package com.dsglyy.common;

/**
 * Created by Lgaren on 2017/9/24.
 */
public enum DSGEnum {

    DSG1(0,"DSGEnum"),
    DSG2(2,"DSGEnum"),
    DSG3(3,"DSGEnum");

    private int id;
    private String price;

    private DSGEnum(int id, String price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameField() {
        return price;
    }

    public void setNameField(String price) {
        this.price = price;
    }
}
