package com.accomodation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accomodation.domain.Room;
import com.accomodation.domain.RoomId;

@Repository
public interface RoomRepository extends JpaRepository<Room, RoomId> {
	Room findByRoomNumberAndBuildingName(int roomNumber, String buildingName);
	List<Room> findByBuildingNameAndCorridor(String buildingName, String corridor);
}
