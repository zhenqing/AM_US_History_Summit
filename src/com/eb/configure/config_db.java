
package com.eb.configure;

public class config_db{
	
	 private final static String database_name = "inventory";
	 
	 private static String[] datatable_name = {"textbook"};

	 public static String getDatabaseName()
	 {
		return database_name;
	 }
	 
	 public static String[] getDatatableName()
	 {
		 return datatable_name;
	 }
	
}