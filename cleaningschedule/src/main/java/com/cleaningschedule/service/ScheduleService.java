package com.cleaningschedule.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cleaningschedule.domain.Occupant;
import com.cleaningschedule.domain.Schedule;
import com.cleaningschedule.network.AccomodationServiceProxy;
import com.cleaningschedule.repository.ScheduleRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;
	
	private final AccomodationServiceProxy proxy;
	
	public ScheduleService(ScheduleRepository scheduleRepository, AccomodationServiceProxy proxy) {
		this.scheduleRepository = scheduleRepository;
		this.proxy = proxy;
	}
	
	public List<Schedule> getCleanersForWeek(int week, String buildingName, String collidor) {
		log.info(week + " " +buildingName + " " + collidor);
		List<Schedule> schedule  = this.scheduleRepository.findByWeekAndBuildingNameAndCorridor(week, buildingName, collidor);
		log.info(schedule);
		return putOccupantsInSchedule(schedule, buildingName);
	}
	
	public List<Schedule> getRoomSchedule(int roomNumber, String buildingName, String collidor){
		return this.scheduleRepository.findByRoomNumberAndBuildingNameAndCorridor(roomNumber, buildingName, collidor);
	}
	
	public List<Schedule> getAllSchedule(String buildingName, String collidor){
		List<Schedule> schedule = scheduleRepository.findByBuildingNameAndCorridor(buildingName, collidor);

		return putOccupantsInSchedule(schedule, buildingName);
	}
	
	private List<Schedule> putOccupantsInSchedule(List<Schedule> schedule, String buildingName){
		List<Integer> rooms = schedule.stream().map(s -> s.getRoomNumber()).collect(Collectors.toList());
		return schedule.stream().map(s -> {
			Occupant occupant = proxy.getRoomOccupant(s.getRoomNumber(), buildingName);
			if(occupant != null) {
				s.setOccupantName(occupant.getOccupantName());
			}
			return s;
		}).collect(Collectors.toList());
	}
}
