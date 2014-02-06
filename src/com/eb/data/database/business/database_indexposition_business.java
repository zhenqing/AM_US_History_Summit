package com.eb.data.database.business;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.eb.data.database.CRUD.database_indexposition;

public class database_indexposition_business {
	
	private database_indexposition db_position; 
	
	public database_indexposition_business() throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		db_position = new database_indexposition();
	}
	
	public int getValueFromName(String name) throws SQLException
	{		
		return db_position.getValueFromName(name) ;
	}
	
	public void update(String name, int value) throws SQLException
	{		
		db_position.update(value, name);
	}
	public void insertStartPoint(String name, int value,Timestamp time) throws SQLException
	{		
		db_position.insertStartPoint(name, value,time);
	}
}
