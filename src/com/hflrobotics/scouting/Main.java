package com.hflrobotics.scouting;

import com.github.sarxos.webcam.Webcam;

public class Main
{
	
	public static void main(String[] args)
	{
		webcams();
	}	
	
	private static void webcams()
	{
		System.out.println(Webcam.getWebcams());		
	}
	
}
