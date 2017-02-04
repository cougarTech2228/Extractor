package com.hflrobotics.scouting.schedule;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.hflrobotics.scouting.GUI;
import com.opencsv.CSVReader;

public class Schedule
{

	private static ArrayList<Match> matches = new ArrayList<Match>();
	private static GUI gui;
	
	public Schedule(GUI gui)
	{
		Schedule.gui = gui;
	}
	
	
	public static void loadSchedule(String filename) throws IOException
	{
		CSVReader reader = new CSVReader(new FileReader(filename));
	    String [] nextLine;
	    while ((nextLine = reader.readNext()) != null)
	    {
	       matches.add(new Match(Integer.decode(nextLine[0]),
	    		   Integer.decode(nextLine[1]),
	    		   Integer.decode(nextLine[2]),
	    		   Integer.decode(nextLine[3]),
	    		   Integer.decode(nextLine[4]),
	    		   Integer.decode(nextLine[5]),
	    		   Integer.decode(nextLine[6])));
	    }
	    
	    reader.close();
	    updateGUI();
	}
	
	
	private static void updateGUI()
	{
		for(int i = 0; i < gui.scheduleTable.getRowCount(); i++)
		{
			((DefaultTableModel) gui.scheduleTable.getModel()).removeRow(i);
		}
		
		for(int i = 0; i < matches.size(); i++)
		{
			((DefaultTableModel) gui.scheduleTable.getModel()).addRow(new Object[]{
				matches.get(i).match,
				matches.get(i).r1,
				matches.get(i).r2,
				matches.get(i).r3,
				matches.get(i).b1,
				matches.get(i).b2,
				matches.get(i).b3,
				});
		}
	}	
}
