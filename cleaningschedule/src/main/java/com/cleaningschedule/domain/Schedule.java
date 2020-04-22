package com.cleaningschedule.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
	private static final String SEQUENCE_NAME_KEY = "Schedule";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME_KEY)
    @SequenceGenerator(name = SEQUENCE_NAME_KEY, sequenceName = "SCHEDULE_SEQUENCE")
	private long Id;
	
	private int week;
	
	private String buildingName;
	
	private String corridor;
	
	private int roomNumber;
	
	@Transient
	private String occupantName;
	
	public Schedule(int week, String buildingName, String corridor, int roomNumber) {
		this.week = week;
		this.buildingName = buildingName;
		this.corridor = corridor;
		this.roomNumber = roomNumber;
	}
}
