package com.example.forum.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.concurrent.locks.Condition;

@Entity
@Getter
@Setter
@Table(name = "products")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private double price;
    private String size;
    private String brand;
    private String listingDate;
    private String imageUrl;

    @ManyToOne
    private User seller;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Enumerated(EnumType.STRING)
    private Status status;

    private enum Condition{
        LIKE_NEW,
        SLIGHTLY_USED,
        GENTLY_WORN,
        WELL_WORN
    }


    public enum Status{
        ACTIVE,
        SOLD
    }

}