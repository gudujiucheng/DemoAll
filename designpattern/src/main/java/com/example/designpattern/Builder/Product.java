package com.example.designpattern.Builder;

public class Product {
    private String name;
    private String price;

    private Product(String name, String price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public static class ProductBuilder {

        private String name;
        private String price;

        public ProductBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder setPrice(String price) {
            this.price = price;
            return this;
        }

        public Product createProduct() {
            return new Product(name, price);
        }
    }


}
