package com.iBank.Database;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.iBank.iBank;
import com.iBank.system.Configuration;
import com.iBank.utils.StreamUtils;

/**
 * Providing simple access to all datasources
 * @author steffengy
 *
 */
public class DataSource {
	private static Drivers type = null;
	private static SQLite db = null;
	private static Mysql mysqldb = null;
	
	
	public static enum Drivers {
		MYSQL("mysql.jar", "com.mysql.jdbc.Driver"),
		SQLite("sqlite.jar", "org.sqlite.JDBC");
		
		String filename;
		String classname;
		
		private Drivers(String filename, String classname) {
			this.filename = filename;
			this.classname = classname;
		}
		
		public String getFilename() {
			return filename;
		}
		public boolean useable() {
			try{
			Class.forName(classname);
			return true;
			}catch(Exception e) {
				return false;
			}
		}
	}

	public static boolean setup(Drivers driver, String url, iBank main) {
		if(driver == Drivers.SQLite)
		{
			if(Drivers.SQLite.useable())
			{
				type = driver;
				db = new SQLite(new File(main.getDataFolder(), url));
				if(!db.success())  return false;
				if(!db.existsTable("accounts") || !db.existsTable("regions")) {
					System.out.println("[iBank] Creating SQLite tables...");
					String sql = StreamUtils.inputStreamToString(main.getResource("sql/sqlite.sql"));
					for(String line : sql.split(";")) {
						if(line.length()>1) db.execute(line.replace("{$accounts$}", Configuration.Entry.DatabaseAccountsTable.toString()).replace("{$regions$}", Configuration.Entry.DatabaseRegionTable.toString()));
					}
				}
				return true;
			}else{
				return false;
			}
		}else if(driver == Drivers.MYSQL) {
			if(Drivers.MYSQL.useable()) {
				type = driver;
				mysqldb = new Mysql(url, Configuration.Entry.DatabaseUser.toString(), Configuration.Entry.DatabasePW.toString(), Configuration.Entry.DatabaseName.toString());
				if(!mysqldb.success()) return false;
				if(!mysqldb.existsTable("accounts") || !db.existsTable("regions")) {
					System.out.println("[iBank] Creating Mysql tables...");
					String sql = StreamUtils.inputStreamToString(main.getResource("sql/mysql.sql"));
					for(String line : sql.split(";")) {
						if(line.length()>1) mysqldb.execute(line.replace("{$accounts$}", Configuration.Entry.DatabaseAccountsTable.toString()).replace("{$regions$}", Configuration.Entry.DatabaseRegionTable.toString()));
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Shuts the data sources down
	 */
	public static void shutdown() {
		if(type == Drivers.MYSQL) {
			mysqldb.close();
			mysqldb = null;
		}else if(type == Drivers.SQLite) {
			db.close();
			db = null;
		}
	}
	/**
	 * 
	 * @param fields
	 * @param table
	 * @param condition
	 * @return QueryResult
	 */
	public static QueryResult query(String[] fields, String table, Condition... condition) {
		if(type == Drivers.MYSQL || type == Drivers.SQLite) {
			String query = SQLBuilder.select(fields,table, condition);
			ResultSet result = null;
			if(type == Drivers.MYSQL) {
				result = mysqldb.query(query);
			}else if(type==Drivers.SQLite){
				result = db.query(query);
			}
			if(result == null) return null;
			QueryResult retval = new QueryResult();
			boolean first = true;
			try {
				while(result.next()) {
					if(!first) {
						retval.newEntry();
						retval.nextEntry();
					}
					first = false;
					for(String field : fields) {
						retval.add(field, result.getObject(field));
						retval.found = true;
					}
				}
			} catch (SQLException e) { System.out.println("[iBank] Error while parsing DB-Query result!"); }
			retval.resetPointer();
			return retval;
		}
		return null;
	}
	/**
	 * Inserts into a table
	 * @param table The table
	 * @param fields The fields
	 * @param values The values
	 */
	public static void insertEntry(String table, String[] fields,  Object[] values) {
		if(type == Drivers.MYSQL || type == Drivers.SQLite) {
			String query = SQLBuilder.insert(fields,table, values);
			if(type == Drivers.MYSQL) {
				mysqldb.execute(query);
			}else if(type==Drivers.SQLite){
				db.execute(query);
				db.commit();
			}
		}
	}

	/**
	 * Deletes an entry from a table
	 * @param table The table
	 * @param Condition The condition (set)
	 */
	public static void deleteEntry(String table, Condition... condition) {
		if(type == Drivers.MYSQL || type == Drivers.SQLite) {
			String query = SQLBuilder.delete(table, condition);
			if(type == Drivers.MYSQL) {
				mysqldb.execute(query);
			}else if(type==Drivers.SQLite){
				db.execute(query);
				db.commit();
			}
		}
	}
	/**
	 * Updates an entry in the table
	 * @param table The table
	 * @param fields The fields
 	 * @param values The values
	 * @param condition The conditions
	 */
	public static void update(String table, String[] fields, Object[] values, Condition... condition) {
		if(type == Drivers.MYSQL || type == Drivers.SQLite) {
			String query = SQLBuilder.update(fields,table, values);
			if(type == Drivers.MYSQL) {
				mysqldb.execute(query);
			}else if(type==Drivers.SQLite){
				db.execute(query);
				db.commit();
			}
		}
	}

}