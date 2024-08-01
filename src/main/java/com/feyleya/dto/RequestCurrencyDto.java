package com.feyleya.dto;

public class RequestCurrencyDto {
    private String name;
    private String code;
    private String sign;

    public RequestCurrencyDto() {
    }

    public RequestCurrencyDto(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }
}
