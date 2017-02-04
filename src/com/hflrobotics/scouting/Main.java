package com.hflrobotics.scouting;

import com.hflrobotics.scouting.schedule.Schedule;
import com.hflrobotics.scouting.tablets.TabletManager;

public class Main
{

	static GUI gui;
	static Extractor extractor;
	static TabletManager tabletManager;
	static Schedule schedule;
	
	public static void main(String[] args)
	{
		gui = new GUI();
		(new Thread((extractor = new Extractor(gui)))).start();
		gui.passExtractor(extractor);
		tabletManager = new TabletManager(gui);
		schedule = new Schedule(gui);
	}	
	
}
