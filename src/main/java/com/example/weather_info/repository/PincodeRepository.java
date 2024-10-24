package com.example.weather_info.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.weather_info.entity.Pincode;

@Repository
public interface PincodeRepository extends JpaRepository<Pincode, Integer>
{

//	Optional<Pincode> findById(Integer pincode);

}
