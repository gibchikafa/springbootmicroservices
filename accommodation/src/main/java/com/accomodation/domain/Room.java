package com.accomodation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RoomId.class)
public class Room {
	@Id
	@Column(name="RoomNumber")
	@NotNull(message = "room.roomNumber.missing")
	private int roomNumber;
	
	@Id
	@Column(name="BuildingName")
	@NotNull(message = "room.buildingname.missing")
	private String buildingName;
	
	@Column(name = "Corridor")
	private String corridor;
	
	@Column(name="OccupantName")
	private String occupantName;
}
