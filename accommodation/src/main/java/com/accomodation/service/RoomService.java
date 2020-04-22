package com.accomodation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accomodation.domain.Room;
import com.accomodation.domain.RoomId;
import com.accomodation.repository.RoomRepository;

@Service
public class RoomService {
	private final RoomRepository repository;
	
	public RoomService(RoomRepository repository) {
		this.repository = repository;
	}
	
	public Room getRoomOccupant(int roomNumber, String buildingName) {
		return this.repository.findByRoomNumberAndBuildingName(roomNumber, buildingName);
	}
	
	public List<Room> getRoomsOccupants(List<RoomId> rooms){
		return this.repository.findAllById(rooms);
	}
	
	public Room addRoom(Room room) {
		return this.repository.save(room);
	}
	
	public List<Room> getCorridorRooms(String buildingName, String corridor){
		return this.repository.findByBuildingNameAndCorridor(buildingName, corridor);
	}
}
