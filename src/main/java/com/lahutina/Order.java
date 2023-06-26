package com.lahutina;

import com.lahutina.products.Product;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class Order {
    private User user;
    private List<Product> products;

    private Order(User user, List<Product> products) {
        this.user = user;
        this.products = products;
    }

    public static Order createOrder(User user, List<Product> products) {
        return new Order(user, products);
    }
}
