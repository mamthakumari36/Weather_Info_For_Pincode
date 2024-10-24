package com.example.weather_info.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.weather_info.entity.WeatherInfo;
import com.example.weather_info.responsestructure.ResponseStructure;
import com.example.weather_info.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class WeatherController {

	@Autowired
	WeatherService weatherService;

	@Operation(summary = "To get weather information", description = "This API will accept pincode and date from client and based on the given input, output will be given. If for the given input data is not found then an exception will be thrown")
	@GetMapping("/weather")
	public ResponseEntity<?> getWeatherInfo(@RequestParam Integer pincode, @RequestParam LocalDate for_date) {

		WeatherInfo weatherInfo = null;

		try {
			weatherInfo = weatherService.getWeatherInfo(pincode, for_date);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(ResponseStructure.builder().status(HttpStatus.NOT_FOUND.value()).message("No Data Found")
							.body("Data Not Found For The Given Pincode...").build());
		}
		return ResponseEntity.status(HttpStatus.OK).body(ResponseStructure.builder().status(HttpStatus.OK.value())
				.message("Weather Information Fetched Successfully..").body(weatherInfo).build());
	}
}
