package com.example.designpattern.Builder;


public class BuilderDesign {
    public static void main(String[] args) {
        Product product =  new Product.ProductBuilder()
        .setName("xxxx")
        .setPrice("2222")
        .createProduct();
        System.out.print(product.toString());

    }



}
