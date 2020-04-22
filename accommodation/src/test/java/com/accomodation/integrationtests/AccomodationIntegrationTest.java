package com.accomodation.integrationtests;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.accomodation.domain.Room;
import com.accomodation.domain.RoomId;
import com.accomodation.repository.RoomRepository;

import lombok.extern.log4j.Log4j2;

//@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Log4j2
public class AccomodationIntegrationTest {
	private final String buildingName = "Kungshamra71";
	private final String corridor = "F3L";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private RoomRepository repository;

	@Test
	public void getRoomOccupant() throws Exception {
		final int roomNumber = 1322;
		final String occupant = "Gibson Chikafa";
		
		Room defaultRoom = new Room(roomNumber, buildingName, corridor, occupant);
		
		this.repository.save(defaultRoom);
		
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
	public void getRoomsOccupantTest() throws Exception{
		List<Room> defaultRooms = new ArrayList<>();
		
		List<Integer> rooms = new ArrayList<>();
		

		List<RoomId> roomIds = new ArrayList<>();
		roomIds.add(new RoomId(1324, buildingName));
		roomIds.add(new RoomId(1325, buildingName));
		
		roomIds.stream().forEach(roomId 
				-> {
					defaultRooms.add(new Room(roomId.getRoomNumber(), roomId.getBuildingName(), corridor, "John Doe"));
					rooms.add(roomId.getRoomNumber());
				});
		
		this.repository.saveAll(defaultRooms);
		
		List<Room> queryRooms = this.repository.findAllById(roomIds);
		
		final String link = "/room/rooms-occupants/" + rooms + "/" + buildingName;
		
		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(queryRooms.size())))
				.andExpect(jsonPath("$[*].roomNumber", is(rooms)));
	}
}
