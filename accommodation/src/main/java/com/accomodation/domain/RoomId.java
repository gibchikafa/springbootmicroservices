package com.accomodation.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Composite key for Room class
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomId implements Serializable{
	private Integer roomNumber;
	private String buildingName;
}
