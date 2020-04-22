package com.cleaningschedule.network;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cleaningschedule.domain.Occupant;

import lombok.extern.log4j.Log4j2;

@FeignClient(name="accomodation-service", url="localhost:8000", fallback = AccomodationServiceProxyFallback.class)
public interface AccomodationServiceProxy {
	@GetMapping("/room/rooms-occupants/{rooms}/{buildingName}")
	List<Occupant> getRoomsOccupants(@PathVariable List<Integer> rooms, @PathVariable String buildingName);
	
	@GetMapping("/room/occupant/{room}/{buildingName}")
	@CrossOrigin
	Occupant getRoomOccupant(@PathVariable int room, @PathVariable String buildingName);
}