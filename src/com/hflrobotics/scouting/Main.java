package com.hflrobotics.scouting;

public class Main
{
	
	// static Scanner scanner = new Scanner();
	static GUI gui;
	static Extractor extractor;
	// static FileInterface files = new FileInterface();
	
	public static void main(String[] args)
	{
		gui = new GUI();
		(new Thread((extractor = new Extractor(gui)))).start();
		gui.passExtractor(extractor);
	}	
	
}
