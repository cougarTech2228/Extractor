package com.hflrobotics.scouting.tablets;

import java.util.ArrayList;

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
			if(tablets.get(i).id == id)
			{
				tablets.remove(i);
				return;
			}
		}
		
		updateGUI();
	}
	
	public static void setTeam(String id, String team)
	{
		Tablet assigning;
		Tablet deassigning;
		
		for(int i = 0; i < tablets.size(); i++)
		{
			if(tablets.get(i).id == id)
			{
				assigning = tablets.get(i);
				assigning.team = team;
				// !TODO: send appropriate message to tablet
			}
			
			if(tablets.get(i).team == team)
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
		for(int i = 0; i < gui.getTabletCount(); i++)
		{
			if(i < tablets.size())
			{
				gui.setTabletCell(tablets.get(i).id, i, 1);
				gui.setTabletCell(tablets.get(i).team, i, 3);
				// !TODO: add query for ping, battery level, and match
			}
			else
			{
				gui.setTabletCell(null, i, 0);
				gui.setTabletCell(null, i, 1);
				gui.setTabletCell(null, i, 2);
				gui.setTabletCell(null, i, 3);
				gui.setTabletCell(null, i, 4);
			}
		}
	}
	
	
}
