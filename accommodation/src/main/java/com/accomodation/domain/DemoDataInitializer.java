package com.accomodation.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.accomodation.repository.RoomRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@Profile("!test")
public class DemoDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

	private final RoomRepository roomRepository;
	
	private final String buildingName = "Kungshamra71";
	private final String corridor = "C3L";
	
	public DemoDataInitializer(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Map<Integer, String> roomOccupants = new HashMap<Integer, String>();
		roomOccupants.put(1323, "John Smith");
		roomOccupants.put(1322, "John Doe");
		roomOccupants.put(1321, "Sam Johns");
		roomOccupants.put(1320, "David Ericson");
		roomOccupants.put(1319, "Thom Davies");
		
		roomOccupants.forEach((roomNumber, roomOccupant) -> {
			this.roomRepository.save(new Room(roomNumber, buildingName, corridor, roomOccupant));
		});
		
		this.roomRepository.findAll().forEach(room -> log.info(room));
	}
}
