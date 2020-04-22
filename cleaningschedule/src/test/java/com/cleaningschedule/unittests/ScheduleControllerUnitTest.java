package com.cleaningschedule.unittests;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
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

import com.cleaningschedule.domain.Schedule;
import com.cleaningschedule.network.AccomodationServiceProxy;
import com.cleaningschedule.presentation.ScheduleController;
import com.cleaningschedule.service.ScheduleService;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ScheduleController.class)
@Log4j2
public class ScheduleControllerUnitTest {
	private static final String buildingName = "Kungshamra71";
	private static final String corridor = "F3L";
	private static Schedule defaultSchedule;
	private static final int roomNumber = 1322;
	private static final String occupant = "Gibson Chikafa";
	private static final int week = 1;
	private static final long id = 1L;
	
	@MockBean
	private ScheduleService scheduleService;
	
	@MockBean
	private AccomodationServiceProxy proxy;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeAll
	public static void init() {
		defaultSchedule = new Schedule(week, buildingName, corridor, roomNumber);
		defaultSchedule.setId(id);
		defaultSchedule.setOccupantName(occupant);
	}
	
	@Test
	void getWeekCleanersTest() throws Exception {
		List<Schedule> weekCleaners = Arrays.asList(defaultSchedule);
		
		Mockito
			.when(this.scheduleService.getCleanersForWeek(week, buildingName, corridor))
			.thenReturn(weekCleaners);
		
		
		final String link = "/schedule/week/"+week+"/"+buildingName+"/"+corridor;
		
		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(weekCleaners.size())))
				.andExpect(jsonPath("$.[0].id", is((int)weekCleaners.get(0).getId())));
	}
	
	@Test
	void  getWholeScheduleTest() throws Exception {
		List<Schedule> weekCleaners = new ArrayList<>();
		weekCleaners.add(defaultSchedule);
		
		List<Integer> scheduleIds = new ArrayList<>();
		scheduleIds.add((int)weekCleaners.get(0).getId());
		
		for(int i = 2; i < 10; i++) {
			Schedule s = new Schedule(week, buildingName, corridor, roomNumber);
			s.setOccupantName(occupant);
			s.setId(i);
			scheduleIds.add(i);
			weekCleaners.add(s);
		}
		
		Mockito
			.when(this.scheduleService.getAllSchedule(buildingName, corridor))
			.thenReturn(weekCleaners);
		
		final String link = "/schedule/" + buildingName + "/" + corridor;
		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(weekCleaners.size())))
				.andExpect(jsonPath("$.[*].id", is(scheduleIds)));
	}
}
