/**
 * 
 */
/**
 * @author Vincent Maresca
 *
 */
package edu.ycp.cs320.teamproject.tbag.db.persist;

import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.teamproject.tbag.model.Location;
import edu.ycp.cs320.teamproject.tbag.db.model.ItemDb;

public class FakeDatabase implements IDatabase{
	
	private List<ItemDb> itemList;
	
	public FakeDatabase() {
		itemList = new ArrayList<ItemDb>();
		
		populateLists();
	}

	public void populateLists() {
		/*try {
			itemList.addAll(InitialData.getItem());
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}*/
	}
	
	public List<ItemDb>getItemByName(String name){
		List<ItemDb> returnList = new ArrayList<ItemDb>();
		for(ItemDb item : itemList) {
			if(item.getName().equals(name)) {
				returnList.add(item);
			}
		}
		return returnList;
	}
	
	public Integer insertItem(String name, int locationID, int descriptionID) {
		
		ItemDb item = new ItemDb();
		
		item.setItemID(itemList.size() + 1);
		
		item.setName(name);
		item.setLocationID(locationID);
		item.setDescriptionID(descriptionID);
		itemList.add(item);
		
		// This should return the itemID
		return item.getItemID();
	}

	@Override
	public Location getLocationDescriptionLong(int location_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getItemLocationID(String itemName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getLocationID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer insertUserIntoUsersTable(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findPasswordFromUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}