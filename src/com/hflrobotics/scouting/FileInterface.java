package com.hflrobotics.scouting;

import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

public class FileInterface
{

	
	public FileInterface()
	{
		
	}
	
	
	/***
	 * writeToCSV
	 * Writes one line to a CSV file
	 * @param filename name of CSV file to be written to
	 * @param data array of Strings containing each new value
	 * @return true - on completion, false - on failure
	 */
	public boolean writeToCSV(String filename, String[] data)
	{
		try
		{
			CSVWriter writer = new CSVWriter(new FileWriter(filename, true));
			writer.writeNext(data);
			writer.close();
			return true;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return false;
		}
	}	
}
