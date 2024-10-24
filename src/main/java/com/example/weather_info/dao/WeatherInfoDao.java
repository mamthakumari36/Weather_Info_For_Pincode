package com.example.weather_info.dao;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.weather_info.entity.WeatherInfo;
import com.example.weather_info.repository.WeatherInfoRepository;

@Repository
public class WeatherInfoDao {

	@Autowired
	private WeatherInfoRepository weatherInfoRepository;

	public Optional<WeatherInfo> findByPincodeAndDate(Integer pincode, LocalDate date) {
		return weatherInfoRepository.findByPincodeAndDate(pincode, date);
	}

	public void saveWeatherInfo(WeatherInfo weatherInfo) {
		 weatherInfoRepository.save(weatherInfo);
	}
}
