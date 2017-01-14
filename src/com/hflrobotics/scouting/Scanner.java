package com.hflrobotics.scouting;

import com.google.zxing.Result;

public class Scanner
{

	QRFinder qrFinder;
	
	public Scanner()
	{
		qrFinder = new QRFinder(this);
		Thread thread = new Thread(qrFinder);
		thread.start();
		qrFinder.changeWebcam(1);
	}
	
	
	public void qrDetected(Result qrData)
	{
		System.out.println(qrData);
	}
	
	
}
