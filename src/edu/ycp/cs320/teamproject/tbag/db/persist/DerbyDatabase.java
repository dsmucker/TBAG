/**
 * 
 */
/**
 * @author Vincent Maresca
 *
 */
package edu.ycp.cs320.teamproject.tbag.db.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.teamproject.tbag.db.persist.InitialData;
import edu.ycp.cs320.teamproject.tbag.db.persist.DBUtil;
import edu.ycp.cs320.teamproject.tbag.db.persist.DerbyDatabase;
import edu.ycp.cs320.teamproject.tbag.db.persist.PersistenceException;
import edu.ycp.cs320.teamproject.tbag.db.model.ItemDb;

public class DerbyDatabase implements IDatabase{
	
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}
	
	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;
	
	@Override
	// All of the query commands go here:
	public List<ItemDb>getItemByName(String name){
		return new ArrayList<ItemDb>();
	}
	
	@Override
	public Integer insertItem(String name, int locationID, int descriptionID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// wrapper SQL transaction function that calls actual transaction function (which has retries)
	public<ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}
		
	// SQL transaction function which retries the transaction MAX_ATTEMPTS times before failing
	public<ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();
		
		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;
				
			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}
				
			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}
			
			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}
	
	// TODO: Here is where you name and specify the location of your Derby SQL database
		// TODO: Change it here and in SQLDemo.java under CS320_LibraryExample_Lab06->edu.ycp.cs320.sqldemo
		// TODO: DO NOT PUT THE DB IN THE SAME FOLDER AS YOUR PROJECT - that will cause conflicts later w/Git
		private Connection connect() throws SQLException {
			Connection conn = DriverManager.getConnection("jdbc:derby:test1.db;create=true");		
			//Connection conn = DriverManager.getConnection("jdbc:derby:C:/CS320-2019-LibraryExample-DB/library.db;create=true")
			
			// Set autocommit() to false to allow the execution of
			// multiple queries/statements as part of the same transaction.
			conn.setAutoCommit(false);
			
			return conn;
		}
		
		// retrieves Item information from query result set
		private void loadItem(ItemDb item, ResultSet resultSet, int index) throws SQLException {
			item.setName(resultSet.getString(index++));
			item.setLocationID(resultSet.getInt(index++));
			item.setDescriptionID(resultSet.getInt(index++));
			item.setItemID(resultSet.getInt(index++));
		}
		
		//  creates the item table
		public void createTables() {
			executeTransaction(new Transaction<Boolean>() {
				@Override
				public Boolean execute(Connection conn) throws SQLException {
					PreparedStatement stmt1 = null;
					PreparedStatement stmt2 = null;
					PreparedStatement stmt3 = null;				
				
					try {
						stmt1 = conn.prepareStatement(
							"create table item (" +
							"   name varchar(40), " +
							"   locationID integer, " +
							"   descriptionID integer, " +
							"	itemID integer primary key " +
							"		generated always as identity (start with 1, increment by 1), " +
							")"
						);	
						stmt1.executeUpdate();
						
						System.out.println("item table created");				
											
						return true;
					} finally {
						DBUtil.closeQuietly(stmt1);
					}
				}
			});
		}
		
		// loads data retrieved from CSV files into DB tables in batch mode
		public void loadInitialData() {
			executeTransaction(new Transaction<Boolean>() {
				@Override
				public Boolean execute(Connection conn) throws SQLException {
					List<ItemDb> itemList;
					
					try {
						itemList       = InitialData.getItem();				
					} catch (IOException e) {
						throw new SQLException("Couldn't read initial data", e);
					}

					PreparedStatement insertItem     = null;

					try {
						// must completely populate Authors table before populating BookAuthors table because of primary keys
						insertItem = conn.prepareStatement("insert into item (name, locationID, descriptionID) values (?, ?, ?)");
						for (ItemDb item : itemList) {
//							// Auto generate itemID
							insertItem.setString(1, item.getName());
							insertItem.setInt(2, item.getLocationID());
							insertItem.setInt(3, item.getDescriptionID());
							insertItem.addBatch();
						}
						insertItem.executeBatch();
						
						System.out.println("Item table populated");
						
						return true;
					} finally {
						DBUtil.closeQuietly(insertItem);					
					}
				}
			});
		}
		
		// The main method creates the database tables and loads the initial data.
		public static void main(String[] args) throws IOException {
			System.out.println("Creating tables...");
			DerbyDatabase db = new DerbyDatabase();
			db.createTables();
			
			System.out.println("Loading initial data...");
			db.loadInitialData();
			
			System.out.println("Library DB successfully initialized!");
		}
}