package com.hflrobotics.scouting;

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

public class Scraper
{

	private static String VersionCode = "v1";
	private static final String TBA_URL = "https://www.thebluealliance.com/";
	private static final String TBA_API_URL = TBA_URL + "api/v2/";
	private static final String TBA_Header_NAME = "X-TBA-App-Id";
	private static String TBA_Header_VALUE = "frc2228:Scouting:" + Scraper.VersionCode;

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
	 * @param eventCode
	 *            - String yyyy[EVENT_CODE] from The Blue Alliance
	 * @throws IOException 
	 */
	public static void getOPRs(String eventCode, ArrayList<Team> teams) throws IOException
	{
		String data = Scraper.getEventDataFromTBA(eventCode, "stats");
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(data).getAsJsonObject();

		for (Map.Entry<String, JsonElement> entry : obj.get("oprs").getAsJsonObject().entrySet())
		{
			int i = Scraper.teamExists(entry.getKey(), teams);
			if (i != -1)
			{
				teams.get(i).opr = entry.getValue().toString();
			}
			else
			{
				Team newTeam = new Team(entry.getKey());
				teams.add(newTeam);
				newTeam.opr = entry.getValue().toString();
			}
		}
	}

	/**
	 * Pulls the rankings from The Blue Alliance for a given event and adds them
	 * to the teams ArrayList
	 * 
	 * @param eventCode
	 *            - String yyyy[EVENT_CODE] from The Blue Alliance
	 * @throws IOException 
	 */
	public static void getRankings(String eventCode, ArrayList<Team> teams) throws IOException
	{
		String data = Scraper.getEventDataFromTBA(eventCode, "rankings");
		JsonParser parser = new JsonParser();
		JsonArray obj = parser.parse(data).getAsJsonArray();

		for (JsonElement entry : obj)
		{
			JsonArray team = entry.getAsJsonArray();

			if (!team.get(0).getAsString().equalsIgnoreCase("Rank"))
			{
				int i = Scraper.teamExists(team.get(1).getAsString(), teams);
				if (i != -1)
				{
					teams.get(i).rank = team.get(0).getAsString();
				}
				else
				{
					Team newTeam = new Team(team.get(1).getAsString());
					teams.add(newTeam);
					newTeam.rank = team.get(0).getAsString();
				}
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
		URL url = new URL(Scraper.TBA_API_URL + "event/" + eventCode + "/" + request);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.addRequestProperty(Scraper.TBA_Header_NAME, TBA_Header_VALUE);

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
