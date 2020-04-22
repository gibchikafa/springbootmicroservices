package com.cleaningschedule.network;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cleaningschedule.domain.Occupant;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AccomodationServiceProxyFallback implements AccomodationServiceProxy {
	@Override
	public List<Occupant> getRoomsOccupants(List<Integer> rooms, String buildingName) {
		log.info("Failed to get rooms occupants");

		return new ArrayList<Occupant>();
	}

	@Override
	public Occupant getRoomOccupant(int room, String buildingName) {
		log.info("Failed to get room occupant");
		return new Occupant();
	}
}