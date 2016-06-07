package com.android.argb.exchangerate;

public class Currency {

    private float value;
    private String currencyName;
    private String currencyCode;

    public Currency(float value, String currencyName, String currencyCode) {
        this.value = value;
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
