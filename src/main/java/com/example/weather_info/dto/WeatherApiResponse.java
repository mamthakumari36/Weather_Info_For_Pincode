package com.example.weather_info.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherApiResponse {
	
	private List<Weather> weather;
    private Main main;
    private String name;
    
}
