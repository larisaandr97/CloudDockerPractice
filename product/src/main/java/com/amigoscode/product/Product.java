package com.amigoscode.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Product {
    //    @SequenceGenerator(name = "product_id_sequence", sequenceName = "product_id_sequence")
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_sequence")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private double rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ProductCategory productCategory;

    @Lob
    private Byte[] image;

    public Product() {
    }

    public Product(String name, String description, double price, ProductCategory productCategory, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.productCategory = productCategory;
        this.stock = stock;
    }

    public Product(int id, String name, String description, double price, ProductCategory productCategory, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.productCategory = productCategory;
        this.stock = stock;
    }
}
