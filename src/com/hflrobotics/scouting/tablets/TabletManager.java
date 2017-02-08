package com.hflrobotics.scouting.tablets;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.hflrobotics.scouting.FileInterface;
import com.hflrobotics.scouting.GUI;
import com.hflrobotics.scouting.schedule.Match;
import com.hflrobotics.scouting.schedule.ScheduleTeam;
import com.opencsv.CSVReader;

public class TabletManager
{

	private static ArrayList<Tablet> tablets = new ArrayList<Tablet>();
	private static GUI gui;
	public static String tabletDataFile = null;
	
	public TabletManager(GUI gui)
	{
		this.gui = gui;
	}
	
	
	public static void addTablet(String address, String id)
	{
		for(Tablet tablet : tablets)
		{
			if(tablet.id.equals(id))
			{
				JOptionPane.showMessageDialog(null, "Tablet already exists.");
				return;
			}			
		}
		
		tablets.add(new Tablet(address, id));
		updateGUI();
		writeTabletConfig();
	}
	
	
	public static void removeTablet(String id)
	{
		for(int i = 0; i < tablets.size(); i++)
		{
			if(tablets.get(i).id.equals(id))
			{
				tablets.remove(i);
				updateGUI();
				writeTabletConfig();
				return;
			}
		}			
	}
	
	
	public static void setTeam(String id, String team)
	{
		// !TODO: send messages to tablets
		Tablet assigning = null;
		Tablet deassigning = null;
		ScheduleTeam teamType = ScheduleTeam.NONE;
		
		switch(team.toUpperCase())
		{
			case "B1":
				teamType = ScheduleTeam.B1;
				break;
				
			case "B2":
				teamType = ScheduleTeam.B2;
				break;
				
			case "B3":
				teamType = ScheduleTeam.B3;
				break;
				
			case "R1":
				teamType = ScheduleTeam.R1;
				break;
				
			case "R2":
				teamType = ScheduleTeam.R2;
				break;
				
			case "R3":
				teamType = ScheduleTeam.R3;
				break;
				
			default:
				JOptionPane.showMessageDialog(null, "Invalid team.");
				return;
		}
		
		for(int i = 0; i < tablets.size(); i++)
		{
			if(tablets.get(i).id.equals(id))
			{
				assigning = tablets.get(i);
			}
			
			if(tablets.get(i).team == teamType)
			{
				deassigning = tablets.get(i);
			}
		}
		
		if(deassigning != null)
		{
			deassigning.team = ScheduleTeam.NONE;
		}
		
		if(assigning != null)
		{
			assigning.team = teamType;
		}
		
		updateGUI();
	}
	
	
	@SuppressWarnings("serial")
	private static void updateGUI()
	{
		@SuppressWarnings("unused")
		DefaultTableModel newModel = new DefaultTableModel();
		Object[][] data = new Object[tablets.size()][5];
		
		for(int i = 0; i < tablets.size(); i++)
		{
			data[i][0] = null;
			data[i][1] = tablets.get(i).id;
			data[i][2] = null;
			data[i][3] = tablets.get(i).team;
			data[i][4] = null;
		}
		
		gui.tabletTable.setModel(new DefaultTableModel(
				data,
				new String[] {
					"", "ID", "Battery", "Team", "Match"
				}
			) {
				@SuppressWarnings("rawtypes")
				Class[] columnTypes = new Class[] {
					Object.class, String.class, String.class, String.class, Integer.class
				};
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			gui.tabletTable.getColumnModel().getColumn(0).setResizable(false);
			gui.tabletTable.getColumnModel().getColumn(0).setPreferredWidth(15);
			gui.tabletTable.getColumnModel().getColumn(0).setMaxWidth(15);
			gui.tabletTable.getColumnModel().getColumn(1).setResizable(false);
			gui.tabletTable.getColumnModel().getColumn(2).setResizable(false);
			gui.tabletTable.getColumnModel().getColumn(3).setResizable(false);
			gui.tabletTable.getColumnModel().getColumn(4).setResizable(false);
	}		
	
	
	
	
	private static void writeTabletConfig()
	{
		if(tabletDataFile != null)
		{
			ArrayList<String[]> data = new ArrayList<String[]>();
			
			for(Tablet tablet : tablets)
			{
				data.add(new String[] {tablet.address, tablet.id});
			}
			
			try
			{
				FileInterface.writeAllData(tabletDataFile, data);
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "Unable to write tablet config file.");
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Specify tablet config file.");
		}
	}
	
	
	public static void loadFile(String filename)
	{
		CSVReader reader;
		try
		{
			reader = new CSVReader(new FileReader(filename));
			String [] nextLine;
		    while ((nextLine = reader.readNext()) != null)
		    {
		       tablets.add(new Tablet(nextLine[0], nextLine[1]));
		    }
		    
		    reader.close();
		    updateGUI();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Failed to load file.");
		}    
	}
	
}
