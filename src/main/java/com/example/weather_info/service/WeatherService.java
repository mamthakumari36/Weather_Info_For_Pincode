package com.example.weather_info.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.weather_info.dao.PincodeDao;
import com.example.weather_info.dao.WeatherInfoDao;
import com.example.weather_info.dto.GeocodingApiResponse;
import com.example.weather_info.dto.WeatherApiResponse;
import com.example.weather_info.entity.Pincode;
import com.example.weather_info.entity.WeatherInfo;

@Service
public class WeatherService {
	
	@Autowired
	private WeatherInfoDao weatherInfoDao;

	@Autowired
	private PincodeDao pincodeDao;
	
	

	private static final String API_KEY = "288e1cfe4e1781f3fd7ec9b5c278ec4a";

//	method to get lattitude and longitude
	public Pincode getPincode(Integer pincode) throws Exception {
		
		String url = "https://api.openweathermap.org/geo/1.0/zip?zip=" + pincode + ",in&appid=" + API_KEY;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<GeocodingApiResponse> response = restTemplate.getForEntity(url, GeocodingApiResponse.class);
		double latitude = 0, longitude = 0;
		if (response.getStatusCode().is2xxSuccessful()) {
			latitude = response.getBody().getLat();
			longitude = response.getBody().getLon();
		} else
			throw new Exception(response.getStatusCode().toString());

		return new Pincode(pincode, latitude, longitude);
	}

	public WeatherApiResponse getWeatherApiResponse(double latitude, double longitude, LocalDate date)throws Exception {

		long unixTimestamp = date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
		String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid="
				+ API_KEY + "&dt=" + unixTimestamp;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<WeatherApiResponse> response = restTemplate.getForEntity(url, WeatherApiResponse.class);
		if (response.getStatusCode().isError()) {
			throw new RuntimeException("There is error in the weather api response...");
		}
		return response.getBody();
	}
	

	// this method is to get weather information;
	public WeatherInfo getWeatherInfo(Integer pincode, LocalDate date) throws Exception {

		Optional<WeatherInfo> optionalWeatherInfo = weatherInfoDao.findByPincodeAndDate(pincode, date);
		if (optionalWeatherInfo.isPresent())
			return optionalWeatherInfo.get();

		double latitude;
		double longitude;
		
		Optional<Pincode> optionalPincode = null;
		try {
			optionalPincode = pincodeDao.findPincodeById(pincode);
		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}

		if (optionalPincode.isPresent()) {
			latitude = optionalPincode.get().getLatitude();
			longitude = optionalPincode.get().getLongitude();
		}

		else {
//			call getPincode() to get lattitude and longitude
			Pincode pincodeLocation = getPincode(pincode);
			latitude = pincodeLocation.getLatitude();
			longitude = pincodeLocation.getLongitude();
			pincodeDao.savePincodeLocation(pincodeLocation);
		}

		WeatherApiResponse weatherApiResponse = getWeatherApiResponse(latitude, longitude, date);

		// this will convert apiresponse to required info;
		WeatherInfo weatherInfo = new WeatherInfo();
		if (weatherApiResponse != null) {
			weatherInfo.setPincode(pincode);
			weatherInfo.setDate(date);
			weatherInfo.setTemperature(weatherApiResponse.getMain().getTemp());
			weatherInfo.setDescription(weatherApiResponse.getWeather().get(0).getDescription());
			weatherInfo.setHumidity(weatherApiResponse.getMain().getHumidity());
			weatherInfo.setPressure(weatherApiResponse.getMain().getPressure());
			weatherInfo.setPlace(weatherApiResponse.getName());
			weatherInfoDao.saveWeatherInfo(weatherInfo);
		}
		return weatherInfo;
	}

}
