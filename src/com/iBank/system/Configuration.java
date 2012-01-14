package com.iBank.system;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The configuration class for iBank
 * @author steffengy
 * 
 */
public class Configuration {
	public static enum Entry
	{
		Enabled("System.Enabled", true),
		BoundToRegion("System.BoundToRegion", true),
		
		InterestEnabled("System.Interest.Enabled", false),
		InterestOnPercentage("System.Interest.Percentages.Online", 1),
		InterestOffPercentage("System.Interest.Percentages.Offline", 1),
		
		DatabaseType("System.Database.Type", "sqlite"),
		DatabaseUrl("System.Database.Url", "bank.db"),
		DatabaseName("System.Database.Database", "bank"),
		DatabaseUser("System.Database.User", "user"),
		DatabasePW("System.Database.Password", "pw"), 
		DatabaseRegionTable("System.Database.Tables.Region", "regions"),
		DatabaseAccountsTable("System.Database.Tables.Accounts", "accounts"),
		
		SelectionTool("System.SelectionTool", 280);
	
		String key;
		Object value;
	
		/**
		 * Constructor of a Value
		 * @param Name The name of the entry
		 * @param value The value of the entry
		 */
		private Entry(String Name, Object value) {
			this.key = Name;
			this.value = value;
		}
		/**
		 * @return The key of the Entry
		 */
		public String getKey() {
			return key;
		}
		/**
		 * @return The value of the Entry
		 */
		public Object getValue() {
			return value;
		}
		/**
		 * @return The value of the Entry as boolean
		 */
		public Boolean getBoolean() {
            return (Boolean) value;
        }
		/*
		 * @return The value of the Entry as Integer (or Double)
		 */
        public Integer getInteger() {
            if(value instanceof Double)
                return ((Double) value).intValue();

            return (Integer) value;
        }
        /**
         * @return The value of the Entry as Double (or Integer)
         */
        public Double getDouble() {
            if(value instanceof Integer)
                return (double) ((Integer) value).intValue();

            return (Double) value;
        }
        /**
         * @return The value of the Entry as Long (or Integer)
         */
        public Long getLong() {
            if(value instanceof Integer)
                return ((Integer) value).longValue();

            return (Long) value;
        }
        /*
         * @return The value of the Entry as List
         */
        public List<String> getStringList() {
            return (List<String>) value;
        }
        /*
         * Sets the value of this Entry
         * @param value the value as object
         */
        public void setValue(Object value) {
        	this.value = value;
        }
        @Override
        public String toString() {
        	return String.valueOf(value);
        }
	}
	public static enum StringEntry
	{
		BankTag("Tags.Bank", "&g&[&w&iBank&g&]"),
		HelpDescription("Description.Help", "Displays the help"),
		BankDescription("Description.bank", "Shows a list of your accounts"),
		AddRegionDescription("Description.addregion", "Adds a region"),
		RegionDescription("Description.region", "Shows info about a region"),
		DelRegionDescription("Description.delregion", "Deletes a region"),
		
		SuccessAddRegion("Success.addregion", "Successfully, created the region $name$"),
		SuccessDelRegion("Success.delregion", "Successfully, deleted the region $name$"),
		
		ErrorAlreadyExists("Error.already_exists", "$name$ does already exists!"),
		ErrorRegionSelect("Error.region_select", "Please select a region first!"),
		ErrorWrongArguments("Error.wrong_arguments", "Wrong arguments given!"), 
		ErrorNoPlayer("Error.no_player", "Sorry, you need to be a player to execute this!"),
		ErrorNotExist("Error.not_exist", "$name$ does not exist!"),
		ErrorNotRegion("Error.not_region", "You need to be in a bank region!"),
		
		GeneralInfo("General.Info", "Info about $type$ $name$:");
		
		String key;
		String value;
	
		/**
		 * Constructor of a Value
		 * @param Name The name of the entry
		 * @param value The value of the entry
		 */
		private StringEntry(String Name, String value) {
			this.key = Name;
			this.value = value;
		}
		/**
		 * @return The key of the Entry
		 */
		public String getKey() {
			return key;
		}
		/**
		 * @return The value of the Entry
		 */
		public String getValue() {
			return value;
		}
        /*
         * Sets the value of this Entry
         * @param value the value
         */
        public void setValue(String value) {
        	this.value = value;
        }
        @Override
        public String toString() {
        	return String.valueOf(value);
        }
	}
    /**
     * Sets the config system up
     * @param config The configuration got from the YamlFile 
     */
    public static void init(YamlConfiguration config)
    {
    	for(Entry s : Entry.values())
    		if(!s.getKey().isEmpty())
    			if(config.get(s.getKey()) != null)
    				s.setValue(config.get(s.getKey()));
    }
    /**
     * Sets the lang system up
     * @param config The configuration got from the YamlFile 
     */
    public static void stringinit(YamlConfiguration config)
    {
    	for(StringEntry s : StringEntry.values())
    		if(!s.getKey().isEmpty())
    			if(config.get(s.getKey()) != null)
    				s.setValue(config.getString(s.getKey()));
    }
}