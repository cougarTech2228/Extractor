package com.hflrobotics.scouting.tablets;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.hflrobotics.scouting.GUI;

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
		Tablet assigning;
		Tablet deassigning;
		
		for(int i = 0; i < tablets.size(); i++)
		{
			if(tablets.get(i).id.equals(id))
			{
				assigning = tablets.get(i);
				assigning.team = team;
				// !TODO: send appropriate message to tablet
			}
			
			if(tablets.get(i).team.equals(team))
			{
				deassigning = tablets.get(i);
				deassigning.team = "__";
				// !TODO: send appropriate message to tablet
			}
		}
		
		updateGUI();
	}
	
	
	private static void updateGUI()
	{
		for(int i = 0; i < gui.tabletTable.getRowCount(); i++)
		{
			((DefaultTableModel) gui.tabletTable.getModel()).removeRow(i);
		}
		
		for(int i = 0; i < tablets.size(); i++)
		{
			((DefaultTableModel) gui.tabletTable.getModel()).addRow(new Object[]{
				null,
				tablets.get(i).id,
				null,
				tablets.get(i).team,
				null,
				});
		}
	}		
}
