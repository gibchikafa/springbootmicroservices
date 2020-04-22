package com.cleaningschedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleaningschedule.domain.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	List<Schedule> findByRoomNumberAndBuildingNameAndCorridor(int roomNumber, String buildingName, String collidor);

	List<Schedule> findByWeekAndBuildingNameAndCorridor(int week, String buildingName, String collidor);

	List<Schedule> findByBuildingNameAndCorridor(String buildingName, String collidor);
}
