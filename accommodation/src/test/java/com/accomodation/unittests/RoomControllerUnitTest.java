package com.accomodation.unittests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.accomodation.domain.Room;
import com.accomodation.domain.RoomId;
import com.accomodation.presentation.RoomController;
import com.accomodation.service.RoomService;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoomController.class)
@Log4j2
public class RoomControllerUnitTest {
	private final String buildingName = "Kungshamra71";
	private final String corridor = "F3L";
	
	@MockBean
	private RoomService roomService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getRoomOccupant() throws Exception {
		final int roomNumber = 1322;
		final String occupant = "Gibson Chikafa";
		
		Room defaultRoom = new Room(roomNumber, buildingName, corridor, occupant);
		
		Mockito
			.when(this.roomService.getRoomOccupant(roomNumber, buildingName))
			.thenReturn(defaultRoom);
		
		final String link = "/room/occupant/" + defaultRoom.getRoomNumber() + "/" + defaultRoom.getBuildingName();
		
		this.mockMvc.perform( MockMvcRequestBuilders
				  .get(link)
				  .accept(MediaType.APPLICATION_JSON_UTF8))
				  .andDo(print())
				  .andExpect(status().isOk())
				  .andExpect(jsonPath("$.roomNumber", is(defaultRoom.getRoomNumber())))
				  .andExpect(jsonPath("$.buildingName", is(defaultRoom.getBuildingName())))
				  .andExpect(jsonPath("$.corridor", is(defaultRoom.getCorridor())))
				  .andExpect(jsonPath("$.occupantName", is(defaultRoom.getOccupantName())));
	}
	
	@Test
	public void getRoomsOccupantTest() throws Exception {
		List<Room> defaultRooms = new ArrayList<>();
		
		List<Integer> rooms = new ArrayList<>();
	
		List<RoomId> roomIds = Arrays.asList(new RoomId(1322, buildingName), new RoomId(1323, buildingName));

		roomIds.stream().forEach(roomId 
				-> {
					defaultRooms.add(new Room(roomId.getRoomNumber(), roomId.getBuildingName(), corridor, "John Doe"));
					rooms.add(roomId.getRoomNumber());
				});
		
		Mockito
			.when(this.roomService.getRoomsOccupants(roomIds))
			.thenReturn(defaultRooms);
		
		final String link = "/room/rooms-occupants/" + rooms + "/" + buildingName;
		
		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(defaultRooms.size())))
				.andExpect(jsonPath("$[*].roomNumber", is(rooms)));
	}
	
}
