package com.hflrobotics.scouting;

public class Main
{

	static GUI gui;
	static Extractor extractor;
	
	public static void main(String[] args)
	{
		gui = new GUI();
		(new Thread((extractor = new Extractor(gui)))).start();
		gui.passExtractor(extractor);
	}	
	
}
