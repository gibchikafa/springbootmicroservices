package com.cleaningschedule.presentation;

import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cleaningschedule.domain.Schedule;
import com.cleaningschedule.network.AccomodationServiceProxyFallback;
import com.cleaningschedule.service.ScheduleService;

@Log4j2
@RestController
public class ScheduleController {
	private final ScheduleService scheduleService;
	
	public ScheduleController(ScheduleService scheduleRepository) {
		this.scheduleService = scheduleRepository;
	}
	
	@GetMapping("/schedule/week/{week}/{buildingName}/{collidor}")
	public ResponseEntity<?> getWeekCleaners(@PathVariable int week, @PathVariable String buildingName, @PathVariable String collidor){
		List<Schedule> weekCleaners = this.scheduleService.getCleanersForWeek(week, buildingName, collidor);
		
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(weekCleaners);
	}
	
	@GetMapping("/schedule/{buildingName}/{corridor}")
	public ResponseEntity<?> getWholeSchedule(@PathVariable String buildingName, @PathVariable String corridor){
		List<Schedule> schedule = this.scheduleService.getAllSchedule(buildingName, corridor);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(schedule);
	}
}
