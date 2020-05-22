package com.cleaningschedule.integrationtests;

import com.cleaningschedule.domain.Schedule;
import com.cleaningschedule.repository.ScheduleRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("persistenceintegrationtest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log4j2
public class CleaningSchedulePersistenceIntegrationTest {
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

    @Test
    void persistenceIntegrationTest() throws Exception{
        Schedule defaultSchedule = new Schedule(week, buildingName, corridor, roomNumber);
        defaultSchedule.setOccupantName(occupant);

        repository.save(defaultSchedule);

        final String link = "/schedule/" + buildingName + "/" + corridor;

		this.mockMvc.perform(MockMvcRequestBuilders
				.get(link)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is((int)defaultSchedule.getId())))
				.andExpect(jsonPath("$.[0].buildingName", is(defaultSchedule.getBuildingName())))
				.andExpect(jsonPath("$.[0].week", is(defaultSchedule.getWeek())))
				.andExpect(jsonPath("$.[0].corridor", is(defaultSchedule.getCorridor())));
    }
}
