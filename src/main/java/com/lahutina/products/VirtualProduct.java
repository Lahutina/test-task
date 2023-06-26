package com.lahutina.products;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class VirtualProduct extends Product {
    private String code;
    private LocalDate expirationDate;
    public VirtualProduct(String name, double price, String code, LocalDate expirationDate) {
        super(name, price);
        this.code = code;
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "VirtualProduct{" +
                super.toString() +
                "code='" + code + '\'' +
                ", expirationDate=" + expirationDate +
                "} ";
    }
}