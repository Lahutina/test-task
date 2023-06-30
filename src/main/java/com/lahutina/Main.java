package com.lahutina;

import com.lahutina.products.Product;
import com.lahutina.products.ProductFactory;
import com.lahutina.products.RealProduct;
import com.lahutina.products.VirtualProductCodeManager;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        //TODO Create User class with method createUser
        // User class fields: name, age;
        // Notice that we can only create user with createUser method without using constructor or builder
        User user1 = User.createUser("Alice", 32);
        User user2 = User.createUser("Bob", 19);
        User user3 = User.createUser("Charlie", 20);
        User user4 = User.createUser("John", 27);

        //TODO Create factory that can create a product for a specific type: Real or Virtual
        // Product class fields: name, price
        // Product Real class additional fields: size, weight
        // Product Virtual class additional fields: code, expiration date

        Product realProduct1 = ProductFactory.createRealProduct("Product A", 20.50, 10, 25);
        Product realProduct2 = ProductFactory.createRealProduct("Product B", 50, 6, 17);

        Product virtualProduct1 = ProductFactory.createVirtualProduct("Product C", 100, "xxx", LocalDate.of(2023, 5, 12));
        Product virtualProduct2 = ProductFactory.createVirtualProduct("Product D", 81.25, "yyy",  LocalDate.of(2024, 6, 20));


        //TODO Create Order class with method createOrder
        // Order class fields: User, List<Price>
        // Notice that we can only create order with createOrder method without using constructor or builder
        List<Order> orders = new ArrayList<>() {{
            add(Order.createOrder(user1, List.of(realProduct1, virtualProduct1, virtualProduct2)));
            add(Order.createOrder(user2, List.of(realProduct1, realProduct2)));
            add(Order.createOrder(user3, List.of(realProduct1, virtualProduct2)));
            add(Order.createOrder(user4, List.of(virtualProduct1, virtualProduct2, realProduct1, realProduct2)));
        }};

        //TODO 1). Create singleton class which will check the code is used already or not
        // Singleton class should have the possibility to mark code as used and check if code used
        // Example:
        // singletonClass.useCode("xxx")
        // boolean isCodeUsed = virtualProductCodeManager.isCodeUsed("xxx") --> true;
        // boolean isCodeUsed = virtualProductCodeManager.isCodeUsed("yyy") --> false;
        System.out.println("1. Create singleton class VirtualProductCodeManager \n");
        VirtualProductCodeManager singletonClass = VirtualProductCodeManager.getInstance();

        String usedCode = "xxx"; // code that will be used
        String notUsedCode = "yyy"; // code that will be not used
        singletonClass.useCode(usedCode);  // use code
        System.out.println("Code " + usedCode + " was used \n");

        boolean isCodeUsed = singletonClass.isCodeUsed(usedCode); // check if first code used
        System.out.println("Is code " + usedCode + " used: " + isCodeUsed + "\n");
        isCodeUsed = singletonClass.isCodeUsed(notUsedCode); // check if second code used
        System.out.println("Is code " + notUsedCode + " used: " + isCodeUsed + "\n");

        //TODO 2). Create a functionality to get the most expensive ordered product
        Product mostExpensive = getMostExpensiveProduct(orders);
        System.out.println("2. Most expensive product: " + mostExpensive + "\n");

        //TODO 3). Create a functionality to get the most popular product(product bought by most users) among users
        Product mostPopular = getMostPopularProduct(orders);
        System.out.println("3. Most popular product: " + mostPopular + "\n");

        //TODO 4). Create a functionality to get average age of users who bought realProduct2
        double averageAge = calculateAverageAge(realProduct2, orders);
        System.out.println("4. Average age is: " + averageAge + "\n");

        //TODO 5). Create a functionality to return map with products as keys and a list of users
        // who ordered each product as values
        Map<Product, List<User>> productUserMap = getProductUserMap(orders);
        System.out.println("5. Map with products as keys and list of users as value \n");
        productUserMap.forEach((key, value) -> System.out.println("key: " + key + " " + "value: " +  value + "\n"));

        //TODO 6). Create a functionality to sort/group entities:
        // a) Sort Products by price
        // b) Sort Orders by user age in descending order
        List<Product> productsByPrice = sortProductsByPrice(List.of(realProduct1, realProduct2, virtualProduct1, virtualProduct2));
        System.out.println("6. a) List of products sorted by price: " + productsByPrice + "\n");
        List<Order> ordersByUserAgeDesc = sortOrdersByUserAgeDesc(orders);
        System.out.println("6. b) List of orders sorted by user agge in descending order: " + ordersByUserAgeDesc + "\n");

        //TODO 7). Calculate the total weight of each order
        Map<Order, Integer> result = calculateWeightOfEachOrder(orders);
        System.out.println("7. Calculate the total weight of each order \n");
        result.forEach((key, value) -> System.out.println("order: " + key + " " + "total weight: " +  value + "\n"));
    }

    private static Product getMostExpensiveProduct(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getProducts().stream())
                .max(Comparator.comparingDouble(Product::getPrice))
                .orElse(null);
    }

    private static Product getMostPopularProduct(List<Order> orders) {
        Map<Product, Integer> productCount = new HashMap<>();

        for (Order order : orders) {
            List<Product> products = order.getProducts();
            for (Product product : products) {
                productCount.put(product, productCount.getOrDefault(product, 0) + 1);
            }
        }

        return productCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static double calculateAverageAge(Product product, List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getProducts().contains(product))
                .mapToInt(order -> order.getUser().getAge())
                .average()
                .orElse(0.0);
    }

    private static Map<Product, List<User>> getProductUserMap(List<Order> orders) {
        Map<Product, List<User>> productUserMap = new HashMap<>();

        for (Order order : orders) {
            User user = order.getUser();
            List<Product> products = order.getProducts();

            for (Product product : products) {
                if (!productUserMap.containsKey(product)) {
                    productUserMap.put(product, new ArrayList<>());
                }
                productUserMap.get(product).add(user);
            }
        }

        return productUserMap;
    }

    private static List<Product> sortProductsByPrice(List<Product> products) {
        List<Product> sortedByPrice = new ArrayList<>(List.copyOf(products));
        sortedByPrice.sort(Comparator.comparingDouble(Product::getPrice));
        return sortedByPrice;
    }

    private static List<Order> sortOrdersByUserAgeDesc(List<Order> orders) {
        List<Order> sortedByUserAgeDesc = new ArrayList<>(List.copyOf(orders));
        sortedByUserAgeDesc.sort(Comparator.comparingInt(order -> order.getUser().getAge()));
        Collections.reverse(sortedByUserAgeDesc);
        return sortedByUserAgeDesc;
    }

    private static Map<Order, Integer> calculateWeightOfEachOrder(List<Order> orders) {
        Map<Order, Integer> weightOfOrders = new HashMap<>();

        for (Order order : orders) {
            int totalWeight = 0;
            for (Product product : order.getProducts()) {
                if (product instanceof RealProduct) {
                    totalWeight += ((RealProduct) product).getWeight();
                }
            }
            weightOfOrders.put(order, totalWeight);
        }

        return weightOfOrders;
    }
}