package com.example.weather_info.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeocodingApiResponse {
	private String zip;
	private double lat;
	private double lon;
	private transient String country;

}
