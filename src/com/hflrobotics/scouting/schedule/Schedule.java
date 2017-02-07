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
	
	
	@SuppressWarnings("serial")
	private static void updateGUI()
	{
		DefaultTableModel newModel = new DefaultTableModel();
		Object[][] data = new Object[matches.size()][7];
		
		for(int i = 0; i < matches.size(); i++)
		{
			data[i][0] = matches.get(i).match;
			data[i][1] = matches.get(i).r1;
			data[i][2] = matches.get(i).r2;
			data[i][3] = matches.get(i).r3;
			data[i][4] = matches.get(i).b1;
			data[i][5] = matches.get(i).b2;
			data[i][6] = matches.get(i).b3;
		}
		
		gui.scheduleTable.setModel(new DefaultTableModel(
				data,
				new String[] {
					"M", "R1", "R2", "R3", "B1", "B2", "B3"
				}
			) {
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					String.class, String.class, String.class, String.class, String.class, String.class, String.class
				};
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
		gui.scheduleTable.getColumnModel().getColumn(0).setResizable(false);
		gui.scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		gui.scheduleTable.getColumnModel().getColumn(0).setMinWidth(30);
		gui.scheduleTable.getColumnModel().getColumn(0).setMaxWidth(30);
		gui.scheduleTable.getColumnModel().getColumn(1).setResizable(false);
		gui.scheduleTable.getColumnModel().getColumn(2).setResizable(false);
		gui.scheduleTable.getColumnModel().getColumn(3).setResizable(false);
		gui.scheduleTable.getColumnModel().getColumn(4).setResizable(false);
		gui.scheduleTable.getColumnModel().getColumn(5).setResizable(false);
		gui.scheduleTable.getColumnModel().getColumn(6).setResizable(false);
	}	
}
