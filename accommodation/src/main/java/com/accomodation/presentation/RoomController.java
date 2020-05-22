package com.accomodation.presentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.accomodation.domain.Room;
import com.accomodation.domain.RoomId;
import com.accomodation.service.RoomService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class RoomController {
	@Autowired
	private Environment environment;
	
	private RoomService roomService;
	
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}
	
	@GetMapping("/room/occupant/{roomNumber}/{buildingName}")
	public ResponseEntity<?> getRoomOccupant(@PathVariable int roomNumber, @PathVariable String buildingName) {
		Room room =  this.roomService.getRoomOccupant(roomNumber, buildingName);
		log.info(room);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(room);
	}
	
	@GetMapping("/room/rooms-occupants/{rooms}/{buildingName}")
	public ResponseEntity<?> getRoomsOccupant(@PathVariable String rooms, @PathVariable String buildingName){
		 rooms = rooms.replace("[","");
		 rooms = rooms.replace("]","");
		 
		 List<RoomId> roomIds = new ArrayList<>();
		 
		 Arrays.asList(rooms.split(",")).stream().forEach(id -> {
			 roomIds.add(new RoomId(Integer.parseInt(id.trim()), buildingName));
		 });

		 List<Room> occupants = this.roomService.getRoomsOccupants(roomIds); 	
		 
		 return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(occupants);
	}
}
