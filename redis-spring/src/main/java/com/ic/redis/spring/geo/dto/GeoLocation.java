package com.ic.redis.spring.geo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GeoLocation {
    private double latitude;
    private double longitude;
}
