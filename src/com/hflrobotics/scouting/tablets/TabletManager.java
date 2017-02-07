package com.hflrobotics.scouting.tablets;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import com.hflrobotics.scouting.GUI;
import com.hflrobotics.scouting.schedule.ScheduleTeam;

public class TabletManager
{

	private static ArrayList<Tablet> tablets = new ArrayList<Tablet>();
	private static GUI gui;
	
	public TabletManager(GUI gui)
	{
		this.gui = gui;
	}
	
	
	public static void addTablet(String address, String id)
	{
		tablets.add(new Tablet(address, id));
		updateGUI();
	}
	
	
	public static void removeTablet(String id)
	{
		for(int i = 0; i < tablets.size(); i++)
		{
			if(tablets.get(i).id.equals(id))
			{
				tablets.remove(i);
				updateGUI();
				return;
			}
		}	
	}
	
	
	public static void setTeam(String id, String team)
	{
		// !TODO: send messages to tablets
		Tablet assigning = null;
		Tablet deassigning = null;
		
		for(int i = 0; i < tablets.size(); i++)
		{
			if(tablets.get(i).id.equals(id))
			{
				assigning = tablets.get(i);
			}
			
			if(tablets.get(i).team.equals(team))
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
			switch(team.toUpperCase())
			{
				case "B1":
					assigning.team = ScheduleTeam.B1;
					break;
					
				case "B2":
					assigning.team = ScheduleTeam.B2;
					break;
					
				case "B3":
					assigning.team = ScheduleTeam.B3;
					break;
					
				case "R1":
					assigning.team = ScheduleTeam.R1;
					break;
					
				case "R2":
					assigning.team = ScheduleTeam.R2;
					break;
					
				case "R3":
					assigning.team = ScheduleTeam.R3;
					break;
					
				default:
					JOptionPane.showMessageDialog(null, "Invalid team.");
			}
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
}
