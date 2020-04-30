package com.cleaningschedule.integrationtests;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import com.cleaningschedule.domain.Schedule;
import com.cleaningschedule.repository.ScheduleRepository;
import com.github.tomakehurst.wiremock.WireMockServer;

import lombok.extern.log4j.Log4j2;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
@TestInstance(Lifecycle.PER_CLASS)
@Log4j2
public class CleaningScheduleIntegrationTest {
	private final String buildingName = "Kungshamra71";
	private final String corridor = "F3L";

	private final int roomNumber = 1322;
	private final String occupant = "Gibson Chikafa";
	private final int week = 1;
	
	private final String response = "{\"buildingName\":\"Kungshamra71\","
			+ "\"corridor\":\"F3L\",\"roomNumber\":1322,"
			+ "\"occupantName\":\"Gibson Chikafa\"}";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ScheduleRepository repository;
	
	private WireMockServer wireMockServer;
	
	@BeforeAll
	public void init() {
		//Configuration for wiremock server
		wireMockServer = new WireMockServer(8000);
        wireMockServer.start();
        setupStub();
	}
	
	public void setupStub() {
	    wireMockServer.stubFor(get(urlEqualTo("/room/occupant/"+roomNumber+"/"+buildingName))
	           .willReturn(aResponse().withHeader("Content-Type", "application/json;charset=UTF-8")
	           .withStatus(200)
	           .withBody(response)));
	}
	
	@AfterEach
    public void teardown () {
        wireMockServer.stop();
    }
	
	@Test
	void gatewayIntegrationTest() throws Exception{
		
		final String link = "/rooom/occupant/" + roomNumber + "/" + buildingName;
		
//		this.mockMvc.perform(MockMvcRequestBuilders
//				.get(link)
//				.accept(MediaType.APPLICATION_JSON_UTF8))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.[0].roomNumber", is(roomNumber)))
//				.andExpect(jsonPath("$.[0].occupantName", is(occupant)));
	}
	
	@Test
	void persitenceIntegrationTest() throws Exception{
		Schedule defaultSchedule = new Schedule(week, buildingName, corridor, roomNumber);
		defaultSchedule.setOccupantName(occupant);
		
		repository.save(defaultSchedule);
		
		final String link = "/schedule/" + buildingName + "/" + corridor;
		
//		this.mockMvc.perform(MockMvcRequestBuilders
//				.get(link)
//				.accept(MediaType.APPLICATION_JSON_UTF8))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.[0].id", is(defaultSchedule.getId())))
//				.andExpect(jsonPath("$.[0].buildingName", is(defaultSchedule.getBuildingName())))
//				.andExpect(jsonPath("$.[0].week", is(defaultSchedule.getWeek())))
//				.andExpect(jsonPath("$.[0].corridor", is(defaultSchedule.getCorridor())))
//				.andExpect(jsonPath("$.[0].occupantName", is(defaultSchedule.getOccupantName())));
	}
}
