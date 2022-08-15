package com.ic.redis.spring.city.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class City {
    private String zip;
    private String city;
    private String stateName;
    private int temperature;
}
