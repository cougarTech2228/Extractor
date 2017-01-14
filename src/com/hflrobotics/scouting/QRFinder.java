package com.hflrobotics.scouting;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class QRFinder implements Runnable, ThreadFactory
{

	private Executor executor = Executors.newSingleThreadExecutor(this);
	private Webcam webcam = null;
	public Result result = null;
	Scanner scanner;

	public QRFinder(Scanner scanner)
	{
		super();
		this.scanner = scanner;
		Dimension size = WebcamResolution.QVGA.getSize();
		webcam = Webcam.getDefault();
		webcam.setViewSize(size);

		executor.execute(this);
	}

	
	/**
	 * changeWebcam
	 * Changes the webcam being used to decode QR codes
	 * @param cameraNumber int of camera in webcam list
	 * @param mainWebcam Sarxos Webcam
	 **/
	public void changeWebcam(int cameraNumber)
	{
		if(cameraNumber < Webcam.getWebcams().size())
		{
			webcam = Webcam.getWebcams().get(cameraNumber);
		}
	}

	public void run()
	{
		do
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			result = null;
			BufferedImage image = null;
			webcam.open();

			if (webcam.isOpen())
			{

				if ((image = webcam.getImage()) == null)
				{
					continue;
				}

				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try
				{
					result = new MultiFormatReader().decode(bitmap);
					scanner.qrDetected(result);
				}
				catch (NotFoundException e)
				{
					// fall thru, it means there is no QR code in image
				}
			}

		} while (true);
	}

	@Override
	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(r, "example-runner");
		t.setDaemon(true);
		return t;
	}
}
