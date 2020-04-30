package com.cleaningschedule.unittests;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cleaningschedule.domain.Occupant;
import com.cleaningschedule.domain.Schedule;
import com.cleaningschedule.network.AccomodationServiceProxy;
import com.cleaningschedule.repository.ScheduleRepository;
import com.cleaningschedule.service.ScheduleService;


@ExtendWith(MockitoExtension.class)
public class ScheduleServiceUnitTest {
	private static final String buildingName = "Kungshamra71";
	private static final String corridor = "F3L";
	
	@Mock
	private ScheduleRepository scheduleRepository;
	
	@Mock
	private AccomodationServiceProxy proxy;
	
	@InjectMocks
	private ScheduleService scheduleService;
	
	private static Schedule defaultSchedule;
	private static final int roomNumber = 1322;
	private static final String occupant = "Gibson Chikafa";
	private static final int week = 1;
	private static final long id = 1L;
	
	@BeforeAll
	public static void setUp() {		
		defaultSchedule = new Schedule(week, buildingName, corridor, roomNumber);
		defaultSchedule.setId(id);
		defaultSchedule.setOccupantName(occupant);
	}
	
	@Test
	void getCleanersForWeekTest() {
		List<Schedule> cleanersDefault = Arrays.asList(defaultSchedule);
		
		Mockito
			.when(this.scheduleRepository.findByWeekAndBuildingNameAndCorridor(week, buildingName, corridor))
			.thenReturn(cleanersDefault);
		
		Mockito
			.when(this.proxy.getRoomOccupant(roomNumber, buildingName))
			.thenReturn(new Occupant(occupant, roomNumber));
		
		List<Schedule> fromService = this.scheduleService.getCleanersForWeek(week, buildingName, corridor);
		
		assertThat(fromService)
			.isNotNull()
			.hasSize(1)
			.isEqualTo(cleanersDefault);
	}
	
	@Test
	void getRoomScheduleTest() {
		List<Schedule> roomSchedule = Arrays.asList(defaultSchedule);
		Mockito
			.when(this.scheduleRepository
					.findByRoomNumberAndBuildingNameAndCorridor(roomNumber, buildingName, corridor))
			.thenReturn(roomSchedule);
		
		List<Schedule> fromService = this.scheduleService.getRoomSchedule(roomNumber, buildingName, corridor);
		
		assertThat(fromService)
			.isNotNull()
			.hasSize(1)
			.isEqualTo(roomSchedule);
	}
	
	@Test
	void getAllScheduleTest() {
		List<Schedule> wholeSchedule = Arrays.asList(defaultSchedule);
		
		Mockito
			.when(this.scheduleRepository.findByBuildingNameAndCorridor(buildingName, corridor))
			.thenReturn(wholeSchedule);
		
		Mockito
			.when(this.proxy.getRoomOccupant(roomNumber, buildingName))
			.thenReturn(new Occupant(occupant, roomNumber));
		
		List<Schedule> fromService = this.scheduleService.getAllSchedule(buildingName, corridor);
		
		assertThat(fromService)
			.isNotNull()
			.hasSize(1)
			.isEqualTo(fromService);	
	}
}
