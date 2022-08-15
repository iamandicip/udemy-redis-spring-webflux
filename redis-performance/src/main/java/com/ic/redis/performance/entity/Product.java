package com.ic.redis.performance.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private Integer id;
    private String description;
    private double price;
}
