package com.example.weather_info.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.weather_info.entity.Pincode;
import com.example.weather_info.repository.PincodeRepository;

@Repository
public class PincodeDao {
	
	@Autowired
	private PincodeRepository pincodeRepository;

	public Optional<Pincode> findPincodeById(Integer pincode) {
		return pincodeRepository.findById(pincode);
	}

	public void savePincodeLocation(Pincode pincodeLocation) {
		pincodeRepository.save(pincodeLocation);
	}
	
	 

}
