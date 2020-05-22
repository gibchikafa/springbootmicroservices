package com.cleaningschedule.integrationtests;

import com.cleaningschedule.repository.ScheduleRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("gatewayintegrationtest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log4j2
public class CleaningScheduleGateWayIntegrationTest {
    private final String buildingName = "Kungshamra71";
    private final String corridor = "C3L";

    private final String occupant = "Ghost Resident";

    @Autowired
    private MockMvc mockMvc;

    private WireMockServer wireMockServer;

    @BeforeAll
    public void init() {
        //Configuration for wiremock server
        wireMockServer = new WireMockServer(wireMockConfig().options().port(8000)
                .extensions(new DynamicTransformer()));
        this.wireMockServer.start();
        setupStub();
    }

    public void setupStub() {
        wireMockServer.stubFor(get(urlPathMatching("/room/occupant/[^/]+/[^/]+"))
                .willReturn(aResponse()
                        .withTransformers("dynamic-transformer")));
    }

    @AfterEach
    public void teardown () {
        this.wireMockServer.stop();
    }

    @Test
    void gatewayIntegrationTest() throws Exception{

        //final String link = "/room/occupant/" + roomNumber + "/" + buildingName;
        final String link = "/schedule/"+buildingName+"/"+corridor;
        this.mockMvc.perform(MockMvcRequestBuilders
                .get(link)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].occupantName", everyItem(is(occupant))));
    }
}