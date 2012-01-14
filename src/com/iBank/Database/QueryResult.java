package com.iBank.Database;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class QueryResult {
	private HashMap<Integer, HashMap<String, Object>> keyvalues = new HashMap<Integer, HashMap<String, Object>>();
	private int readpointer = 0; //readpointer = pointer at current position
	private int pointer = 0; //pointer = next empty position , -1 : ""
	public boolean found = false;
	
	/**
	 * Initials the object
	 */
	public QueryResult() {
		newEntry();
	}
	/**
	 * Adds an entry to the result
	 * @param key The key
	 * @param value The entry
	 */
	public void add(String key, Object value) {
		keyvalues.get(readpointer).put(key, value);
		found = true;
	}
	/**
	 * Returns if the current set contains this key
	 * @param string
	 * @return
	 */
	public boolean hasKey(String string) {
		return keyvalues.size()>readpointer ? keyvalues.get(readpointer).containsKey(string) : false;
	}
	/**
	 * Gets an entry from the result
	 * @param key
	 */
	public Object get(String key) {
		return keyvalues.size()>readpointer ? keyvalues.get(readpointer).get(key) : null;
	}
	/**
	 * Gets an entry from the result as String
	 * @param key
	 */
	public String getString(String key) {
		return keyvalues.size()>readpointer ? (String)keyvalues.get(readpointer).get(key) : null;
	}
	/**
	 * Gets an entry from the result as Double
	 * @param key String
	 */
	public double getDouble(String key) {
		try{
			return keyvalues.size()>readpointer ? (Double)keyvalues.get(readpointer).get(key) : null;
		}catch(Exception e) { return 0.00; }
	}
	/**
	 * Gets an entry from the result as BigInteger
	 * @param key String
	 * @return
	 */
	public BigInteger getBigInteger(String key) {
		try{
			return keyvalues.size()>readpointer ? (BigInteger)keyvalues.get(readpointer).get(key) : null;
		}catch(Exception e) { return null; }
	}
    /**
     * Creates an new entry and counts up
     */
	public void newEntry() {
		keyvalues.put(pointer, new HashMap<String, Object>());
		pointer++;
	}
	/**
	 * Go to the next entry
	 */
	public boolean nextEntry() {
		if((readpointer + 1)<pointer) readpointer++; else return false;
		return true;
	}
	/**
	 * Gets the previous entry
	 */
	public boolean previousEntry() {
		if((readpointer - 1) > 0) readpointer--; else return false;
		return true;
	}
	/**
	 * Resets the read pointer
	 */
	public void resetPointer() {
		readpointer = 0;
	}
	public void print() {
		for(Map.Entry<Integer, HashMap<String, Object>> s : keyvalues.entrySet()) {
			System.out.println(s.getKey());
			for(Map.Entry <String,Object> ss : s.getValue().entrySet()) {
				System.out.println("==>"+ss.getKey() + "=" + ss.getValue());
			}
		}
	}

}