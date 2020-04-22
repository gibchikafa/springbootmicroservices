package com.accomodation.unittests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import com.accomodation.domain.Room;
import com.accomodation.domain.RoomId;
import com.accomodation.repository.RoomRepository;
import com.accomodation.service.RoomService;

@ExtendWith(MockitoExtension.class)
@Import({RoomService.class})
public class RoomServiceUnitTest {
	private final String buildingName = "Kungshamra71";
	private final String corridor = "F3L";
	
	@Mock
	RoomRepository repository;
	
	@InjectMocks
	private RoomService roomService;
	
	@Test
	public void getRoomOccupantTest() {
		final int roomNumber = 1322;
		final String occupant = "Gibson Chikafa";
		
		Room defaultRoom = new Room(roomNumber, buildingName, corridor, occupant);
		
		Mockito
			.when(this.repository.findByRoomNumberAndBuildingName(roomNumber, buildingName))
			.thenReturn(defaultRoom);
		
		Room room = this.roomService.getRoomOccupant(roomNumber, buildingName);
		
		assertThat(room)
			.isNotNull()
			.hasFieldOrPropertyWithValue("roomNumber", roomNumber)
			.hasFieldOrPropertyWithValue("buildingName", buildingName)
			.hasFieldOrPropertyWithValue("corridor", corridor)
			.hasFieldOrPropertyWithValue("occupantName", occupant);
	
	}

	@Test
	public void getRoomsOccupantsTest() {
		List<Room> defaultRooms = new ArrayList<>();
		
		List<RoomId> roomIds = new ArrayList<>();
		roomIds.add(new RoomId(1322, buildingName));
		roomIds.add(new RoomId(1323, buildingName));
		
		roomIds.stream().forEach(roomId 
				-> defaultRooms.add(new Room(roomId.getRoomNumber(), roomId.getBuildingName(), corridor, "John Doe")));
		
		Mockito
			.when(this.repository.findAllById(roomIds))
			.thenReturn(defaultRooms);
		
		List<Room> rooms = this.roomService.getRoomsOccupants(roomIds);
		
		assertThat(rooms)
			.isNotEmpty()
			.isEqualTo(defaultRooms);
	}
	
	@Test
	public void addRoomTest() {
		final int roomNumber = 1322;
		final String occupant = "Gibson Chikafa";
		
		Room defaultRoom = new Room(roomNumber, buildingName, corridor, occupant);
		
		Mockito
			.when(this.repository.save(defaultRoom))
			.thenReturn(defaultRoom);
		
		Room room = this.roomService.addRoom(defaultRoom);
		
		assertThat(room)
			.isNotNull()
			.hasFieldOrPropertyWithValue("roomNumber", roomNumber)
			.hasFieldOrPropertyWithValue("buildingName", buildingName)
			.hasFieldOrPropertyWithValue("corridor", corridor)
			.hasFieldOrPropertyWithValue("occupantName", occupant);
	}
	
	@Test
	public void getCorridorRoomsTest() {
		List<Room> defaultRooms = new ArrayList<>();
		
		List<RoomId> roomIds = new ArrayList<>();
		roomIds.add(new RoomId(1322, buildingName));
		roomIds.add(new RoomId(1323, buildingName));
		
		roomIds.stream().forEach(roomId 
				-> defaultRooms.add(new Room(roomId.getRoomNumber(), roomId.getBuildingName(), corridor, "John Doe")));
		
		Mockito
			.when(this.repository.findByBuildingNameAndCorridor(buildingName, corridor))
			.thenReturn(defaultRooms);
		
		List<Room> rooms = this.roomService.getCorridorRooms(buildingName, corridor);
		
		assertThat(rooms)
			.isNotEmpty()
			.isEqualTo(defaultRooms);
	}
}
