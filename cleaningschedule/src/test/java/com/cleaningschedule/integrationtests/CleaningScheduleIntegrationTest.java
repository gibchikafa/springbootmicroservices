package com.cleaningschedule.integrationtests;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cleaningschedule.domain.Schedule;
import com.cleaningschedule.repository.ScheduleRepository;

import lombok.extern.log4j.Log4j2;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Log4j2
public class CleaningScheduleIntegrationTest {
	private static final String buildingName = "Kungshamra71";
	private static final String corridor = "F3L";

	private static final int roomNumber = 1322;
	private static final String occupant = "Gibson Chikafa";
	private static final int week = 1;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ScheduleRepository repository;
	

	@Test
	void getWeekCleanersTest() throws Exception{
		Schedule defaultSchedule = new Schedule(week, buildingName, corridor, roomNumber);
		defaultSchedule.setOccupantName(occupant);
		
		repository.save(defaultSchedule);
		final String link = "/schedule/week/"+week+"/"+buildingName+"/"+corridor;
		
		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").hasJsonPath())
				.andExpect(jsonPath("$.[0].buildingName", is(defaultSchedule.getBuildingName())))
				.andExpect(jsonPath("$.[0].week", is(defaultSchedule.getWeek())))
				.andExpect(jsonPath("$.[0].corridor", is(defaultSchedule.getCorridor())))
				.andExpect(jsonPath("$.[0].occupantName").hasJsonPath());
	}
	
	@Test
	void getWholeScheduleTest() throws Exception{
		Schedule defaultSchedule = new Schedule(week, buildingName, corridor, roomNumber);
		defaultSchedule.setOccupantName(occupant);
		
		repository.save(defaultSchedule);
		final String link = "/schedule/" + buildingName + "/" + corridor;
		
		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").hasJsonPath())
				.andExpect(jsonPath("$.[0].buildingName").hasJsonPath())
				.andExpect(jsonPath("$.[0].week").hasJsonPath())
				.andExpect(jsonPath("$.[0].corridor").hasJsonPath())
				.andExpect(jsonPath("$.[0].occupantName").hasJsonPath());
	}
	
}
