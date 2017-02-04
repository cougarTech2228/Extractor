package com.hflrobotics.scouting;

import com.hflrobotics.scouting.tablets.TabletManager;

public class Main
{

	static GUI gui;
	static Extractor extractor;
	static TabletManager tabletManager;
	
	public static void main(String[] args)
	{
		gui = new GUI();
		(new Thread((extractor = new Extractor(gui)))).start();
		gui.passExtractor(extractor);
		tabletManager = new TabletManager(gui);
	}	
	
}
