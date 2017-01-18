package com.hflrobotics.scouting;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JOptionPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamLockException;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class Extractor implements Runnable, ThreadFactory
{

	private Executor executor = Executors.newSingleThreadExecutor(this);
	private GUI gui;
	public String state = "searching";
	private int total = 0;
	ArrayList<String> data;
	ArrayList<String> matchData;
	ArrayList<String> pitData;
	ArrayList<String> driverData;
	
	
	public Extractor(GUI gui)
	{
		super();
		
		this.gui = gui;
		
		executor.execute(this);
	}

	
	private void closeAllWebcamsExcept(Webcam webcam)
	{
		for(Webcam curr : Webcam.getWebcams())
		{
			if(curr != webcam)
			{
				curr.close();
			}
		}
	}
	
	
	private Result getQRFromWebcam()
	{
		Webcam webcam = null;

		
		if(gui.cameraSelector.getSelectedIndex() == -1)
		{
			JOptionPane.showMessageDialog(null, "No webcam is connected. Please connect one.");
			
			// prevent this from endless looping until problem is solved, enables btnClear to go back to "searching"
			state = "complete";
			gui.btnClear.setEnabled(true);
			return null;
		}
		
		try
		{
			webcam = Webcam.getWebcams().get(gui.cameraSelector.getSelectedIndex());
		}
		catch(IndexOutOfBoundsException ex)
		{
			gui.updateCameraList();  // should fix the camera missing problem
			webcam = Webcam.getWebcams().get(gui.cameraSelector.getSelectedIndex());
		}
		
		closeAllWebcamsExcept(webcam);
		BufferedImage image = null;

		try
		{
			webcam.open();
		}
		catch(WebcamLockException ex)
		{
			JOptionPane.showMessageDialog(null, "Webcam is locked. Please restart Extractor.");
		}
		
		if(webcam.isOpen())
		{		
			if ((image = webcam.getImage()) != null)
			{
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	
				try
				{
					return new MultiFormatReader().decode(bitmap);
				}
				catch (NotFoundException e)
				{
					// No QR detected, falls through to return null
				}
			}
		}
		
		return null;
	}
		
		
	public void run()
	{
		Result result = null;
		data = new ArrayList<String>();
		matchData = new ArrayList<String>();
		pitData = new ArrayList<String>();
		driverData = new ArrayList<String>();
		
		while(true)
		{
			// Delay the thread by 250 milliseconds, prevents problems in running
			try
			{
				Thread.sleep(250);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			System.out.println(state);
			
			switch(state)
			{
				case "searching":
					/*
					 * searches for a QR code following format of "START:*total qr codes"
					 * pre-condition: gui.btnClear - disabled, gui.btnSubmit - disabled, gui.transferProgress indeterminate
					 */
					result = getQRFromWebcam();
					System.out.print("-");
					
					if(result != null)
					{
						String[] qr = result.toString().split(":");
						
						if(qr[0].equals("START"))
						{				
							try
							{
								total = Integer.decode(qr[1]);
								
								gui.transferProgress.setIndeterminate(false);
								gui.transferProgress.setMaximum(total);
								gui.transferProgress.setValue(0);
								
								gui.btnStop.setEnabled(true);
								
								state = "in progress";
							}
							catch(NumberFormatException|ArrayIndexOutOfBoundsException ex)
							{
								// START code is not of proper format, continue searching
							}							
						}
					}

					break;
					
				case "in progress":
					/*
					 * in progress seeks out MATCH, PIT, and DRIVER entries
					 * pre-condition: gui.btnStop - enabled, gui.btnSubmit - disabled, gui.btnClear - disabled
					 * gui.tranfserProgress - not indeterminate
					 */
					result = getQRFromWebcam();
					
					if(result != null)
					{
						String[] qr = result.toString().split(":");
						
						if(!data.contains(qr[0] + ":" + qr[1])) // qr has not already been scanned
						{						
							if(qr[0].equals("MATCH"))
							{
								data.add(qr[0] + ":" + qr[1]);
								matchData.add(qr[1]);
							}
							else if(qr[0].equals("PIT"))
							{
								data.add(qr[0] + ":" + qr[1]);
								pitData.add(qr[1]);
							}
							else if(qr[0].equals("DRIVER"))
							{
								data.add(qr[0] + ":" + qr[1]);
								driverData.add(qr[1]);
							}
							
							gui.currentDataList.setListData(data.toArray());
							gui.transferProgress.setValue(data.size());
							
							if(data.size() == total)
							{
								System.out.println(data.size());
								System.out.println(total);
								gui.btnClear.setEnabled(true);
								gui.btnStop.setEnabled(false);
								gui.btnSubmit.setEnabled(true);
								state = "complete";
							}
						}
					}

					break;
					
				case "complete":
					break;
					
				default:
					break;
			}
		}
	}
	
	
	@Override
	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}
	
}
