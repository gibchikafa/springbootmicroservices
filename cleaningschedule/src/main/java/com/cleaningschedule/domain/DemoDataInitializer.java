package com.cleaningschedule.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


import com.cleaningschedule.repository.ScheduleRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@Profile({"default"})
public class DemoDataInitializer implements ApplicationListener<ApplicationReadyEvent>{
	@Autowired
	private ScheduleRepository scheduleRepository;
	private final String buildingName = "Kungshamra71";
	private final String corridor = "C3L";
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		int[] rooms = new int[]{319, 1320,1321, 1322, 1323};
		
		int i = 1;
		while(i <= 20) {
			int room = rooms[i % rooms.length];
			
			this.scheduleRepository.save(new Schedule(i,buildingName,corridor,room));
			i++;
		}
		
		this.scheduleRepository.findAll().forEach(s -> log.info(s));
	}

}
