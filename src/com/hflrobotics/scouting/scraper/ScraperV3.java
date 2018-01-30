package com.hflrobotics.scouting.scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ScraperV3
{

	/**
	 * Scraper for v3 of TBA API
	 */
	
	private static final String TBA_URL = "https://www.thebluealliance.com/";
	private static final String TBA_API_URL = TBA_URL + "api/v3/";
	private static final String TBA_Header_NAME = "X-TBA-Auth-Key";
	private static String TBA_Header_VALUE = ScraperAPIKey.getAPIKey();
	
	public static ArrayList<String[]> getCSVWriteableData(ArrayList<Team> teams)
	{
		ArrayList<String[]> result = new ArrayList<String[]>();

		for (Team team : teams)
		{
			result.add(new String[] { team.team, team.rank, team.opr });
		}

		return result;

	}

	/**
	 * Pulls the OPRs from The Blue Alliance for a given event and adds them to
	 * the teams ArrayList
	 * 
	 * v3 Compliant.
	 * 
	 * @param eventCode
	 *            - String yyyy[EVENT_CODE] from The Blue Alliance
	 * @throws IOException 
	 */
	public static void getOPRs(String eventCode, ArrayList<Team> teams) throws IOException
	{
		String data = ScraperV3.getEventDataFromTBA(eventCode, "oprs");
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(data).getAsJsonObject();

		for (Map.Entry<String, JsonElement> entry : obj.get("oprs").getAsJsonObject().entrySet())
		{
			String teamID = entry.getKey().substring(3); // Removes "frc" from team id (API: frc2228)
			String teamOPR = entry.getValue().toString();
			
			int i = ScraperV3.teamExists(teamID, teams);
			if (i != -1)
			{
				teams.get(i).opr = teamOPR;
			}
			else
			{
				Team newTeam = new Team(teamID);
				teams.add(newTeam);
				newTeam.opr = teamOPR;
			}
		}
	}

	/**
	 * Pulls the rankings from The Blue Alliance for a given event and adds them
	 * to the teams ArrayList
	 * 
	 * v3 Compliant
	 * 
	 * @param eventCode
	 *            - String yyyy[EVENT_CODE] from The Blue Alliance
	 * @throws IOException 
	 */
	public static void getRankings(String eventCode, ArrayList<Team> teams) throws IOException
	{
		String data = ScraperV3.getEventDataFromTBA(eventCode, "rankings");
		JsonParser parser = new JsonParser();
		JsonArray obj = parser.parse(data).getAsJsonObject().get("rankings").getAsJsonArray();
		
		for (JsonElement entry : obj)
		{
			JsonObject entryObj = entry.getAsJsonObject();
			String teamID = entryObj.get("team_key").getAsString().substring(3); // Removes "frc" from team id (API: frc2228)
			String teamRank = entryObj.get("rank").getAsString();
			
			int i = ScraperV3.teamExists(teamID, teams);
			
			if (i != -1)
			{
				teams.get(i).rank = teamRank;
			}
			else
			{
				Team newTeam = new Team(teamID);
				teams.add(newTeam);
				newTeam.rank = teamRank;
			}
		}
	}

	/**
	 * checks if team exists in team ArrayList
	 * 
	 * @param searchTeam
	 *            -- String team
	 * @param teams
	 *            -- ArrayList of teams
	 * @return index -- index of team in teams[], -1 otherwise
	 */
	private static int teamExists(String searchTeam, ArrayList<Team> teams)
	{
		for (int i = 0; i < teams.size(); i++)
		{
			if (teams.get(i).team.equalsIgnoreCase(searchTeam))
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Performs an API request from The Blue Alliance of specified event and
	 * request type.
	 * 
	 * @param eventCode
	 *            - String yyyy[EVENT_CODE] from The Blue Alliance
	 * @param request
	 *            - String [rankings/stats]
	 * @return String of JSON information
	 * @throws IOException 
	 */
	private static String getEventDataFromTBA(String eventCode, String request) throws IOException
	{
		URL url = new URL(ScraperV3.TBA_API_URL + "event/" + eventCode + "/" + request);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.addRequestProperty(ScraperV3.TBA_Header_NAME, TBA_Header_VALUE);
		con.addRequestProperty("accept", "application/json");
		
		con.setRequestMethod("GET");
		con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
		{
			response.append(inputLine);

		}
		in.close();
		return response.toString();
	}
}
