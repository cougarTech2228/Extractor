package com.hflrobotics.scouting;

import com.github.sarxos.webcam.Webcam;

public class Main
{
	
	static FileInterface fileInterface = new FileInterface();
	
	public static void main(String[] args)
	{
		QRFinder webcamInterface = new QRFinder(0);
		Thread thread = new Thread(webcamInterface);
		thread.start();
		webcamInterface.changeWebcam(1, Webcam.getDefault());
	}	
	
}
