package com.hflrobotics.scouting;

public class Main
{
	
	static Scanner scanner = new Scanner();
	GUI gui = new GUI(this);
	static FileInterface files = new FileInterface();
	
	public static void main(String[] args)
	{
		String[] data = {
				"i can't",
				"belive",
				"chester AND nathan",
				"deleted it"
		};
		files.writeToCSV("C:/Users/mgugl/Documents/_CougarTech/Steamworks/test.csv", data);
	}	
	
	
	private void stateMachine()
	{
		String state ="bitts";
		
		switch(state)
		{
			case "searching":
				// submit, stop, and cancel disabled
				// progress bar indeterminate
				break;
				
			case "in progress":
				// submit and cancel disabled
				break;
				
			case "complete":
				// stop disabled
				break;
		}
	}
	
}
