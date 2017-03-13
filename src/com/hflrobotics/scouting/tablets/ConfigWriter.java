package com.hflrobotics.scouting.tablets;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.hflrobotics.scouting.schedule.Match;
import com.hflrobotics.scouting.schedule.Schedule;

public class ConfigWriter
{

	@SuppressWarnings("unchecked")
	public static void writeToConfig(String filename, JSpinner currentMatch, 
			JComboBox<String> team, JTextPane pitList)
	{
		if(filename != "")
		{		
			JSONObject obj = new JSONObject();
			obj.put("team", team.getSelectedItem().toString());
			obj.put("currentMatch", currentMatch.getValue());
			
			JSONArray matches = new JSONArray();
			
			for(Match match : Schedule.getMatches())
			{
				JSONObject matchTeamObj = new JSONObject();
				matchTeamObj.put("match", match.match);
				matchTeamObj.put("red1", match.r1);
				matchTeamObj.put("red2", match.r2);
				matchTeamObj.put("red3", match.r3);
				matchTeamObj.put("blue1", match.b1);
				matchTeamObj.put("blue2", match.b2);
				matchTeamObj.put("blue3", match.b3);
				matches.add(matchTeamObj);			
			}
			
			obj.put("matches", matches);
			
			JSONArray pit = new JSONArray();
			String[] pitTeams = pitList.getText().split("\\r?\\n");
			
			for(String pitTeam : pitTeams)
			{
				JSONObject pitTeamObj = new JSONObject();
				pitTeamObj.put("team", Integer.parseInt(pitTeam));
				pit.add(pitTeamObj);
			}
			
			obj.put("pits", pit);	
			
			try (FileWriter file = new FileWriter(filename))
			{
				file.write(obj.toJSONString());
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null, "Error in writing file.");
			}
		}
	}	
}
