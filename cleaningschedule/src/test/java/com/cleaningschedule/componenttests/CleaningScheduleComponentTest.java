package com.cleaningschedule.componenttests;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cleaningschedule.domain.Occupant;
import com.cleaningschedule.domain.Schedule;
import com.cleaningschedule.network.AccomodationServiceProxy;
import com.cleaningschedule.repository.ScheduleRepository;
import com.github.tomakehurst.wiremock.WireMockServer;

import lombok.extern.log4j.Log4j2;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"componenttest"})
@TestInstance(Lifecycle.PER_CLASS)
@Log4j2
public class CleaningScheduleComponentTest {
	private final String buildingName = "Kungshamra71";
	private final String corridor = "F3L";

	private final int roomNumber = 1322;
	private final String occupant = "Gibson Chikafa";
	private final int week = 1;
	
	private Schedule defaultSchedule;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ScheduleRepository repository;
	
	@MockBean
	private AccomodationServiceProxy proxy;
	
	private WireMockServer wireMockServer;
	
	
	@BeforeAll
	public void init() {
		defaultSchedule = new Schedule(week, buildingName, corridor, roomNumber);
		defaultSchedule.setId(1L);
		defaultSchedule.setOccupantName(occupant);
		repository.save(defaultSchedule);
	}
	
	@Test
	void getWeekCleanersTest() throws Exception{
		final String link = "/schedule/week/"+week+"/"+buildingName+"/"+corridor;
		
		Mockito
			.when(this.proxy.getRoomOccupant(defaultSchedule.getRoomNumber(), defaultSchedule.getBuildingName()))
			.thenReturn(new Occupant(defaultSchedule.getOccupantName(), defaultSchedule.getRoomNumber()));
		
		log.info(this.repository.findAll());
		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is((int)defaultSchedule.getId())))
				.andExpect(jsonPath("$.[0].buildingName", is(defaultSchedule.getBuildingName())))
				.andExpect(jsonPath("$.[0].week", is(defaultSchedule.getWeek())))
				.andExpect(jsonPath("$.[0].corridor", is(defaultSchedule.getCorridor())))
				.andExpect(jsonPath("$.[0].occupantName", is(defaultSchedule.getOccupantName())));
	}
	
	
	@Test
	void getWholeScheduleTest() throws Exception{
		final String link = "/schedule/" + buildingName + "/" + corridor;

		Mockito
		.when(this.proxy.getRoomOccupant(defaultSchedule.getRoomNumber(), defaultSchedule.getBuildingName()))
		.thenReturn(new Occupant(defaultSchedule.getOccupantName(), defaultSchedule.getRoomNumber()));
	
		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is((int)defaultSchedule.getId())))
				.andExpect(jsonPath("$.[0].buildingName", is(defaultSchedule.getBuildingName())))
				.andExpect(jsonPath("$.[0].week", is(defaultSchedule.getWeek())))
				.andExpect(jsonPath("$.[0].corridor", is(defaultSchedule.getCorridor())))
				.andExpect(jsonPath("$.[0].occupantName", is(defaultSchedule.getOccupantName())));
	}
}
