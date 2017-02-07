package com.hflrobotics.scouting.tablets;

import com.hflrobotics.scouting.schedule.ScheduleTeam;

public class Tablet
{

	String id;
	String address;
	ScheduleTeam team = ScheduleTeam.NONE;
	
	
	public Tablet(String address, String id)
	{
		this.id = id;
		this.address = address;
	}
	
	
}
