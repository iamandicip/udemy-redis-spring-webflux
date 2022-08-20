package com.ic.redis.spring.geo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ic.redis.spring.geo.dto.Restaurant;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class RestaurantUtil {
    public static List<Restaurant> getRestaurants() {
        ObjectMapper mapper = new ObjectMapper();
//        InputStream is = RestaurantUtil.class.getResourceAsStream("./restaurant.json");
        try {
            File file = ResourceUtils.getFile("classpath:restaurant.json");
            try (InputStream is = new FileInputStream(file)) {
                System.out.println("available: " + is.available());
                return mapper.readValue(is, new TypeReference<>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
