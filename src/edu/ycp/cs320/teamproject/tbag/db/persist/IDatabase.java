package edu.ycp.cs320.teamproject.tbag.db.persist;

import java.util.ArrayList;

import edu.ycp.cs320.teamproject.tbag.model.Location;

public interface IDatabase{
	public Integer findUserIDFromUsername(String username); 
	public Integer insertItem(String name, int locationID, int descriptionID);
	public Location getLocationDescriptionLong(int location_id);
	public Integer getItemLocationID(String itemName);
	public Integer getLocationID();
	public Integer insertUserIntoUsersTable(String username, String password);
	public String findPasswordFromUsername(String username);
	public Boolean addUserInput(String input);
	public ArrayList<String> getInputs();
}

