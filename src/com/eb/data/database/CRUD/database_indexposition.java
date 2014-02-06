package com.eb.data.database.CRUD;

import java.sql.*;

import com.eb.configure.config_db;


public class database_indexposition {
	
	private Connection con;
	private String datatablename;
	private PreparedStatement psSelectItem;
	private PreparedStatement psUpdate;
	private PreparedStatement psInsertStartPoint;
	public void iniconnect() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		 String maindatabase = config_db.getDatabaseName();
		 datatablename = "indexposition";
		 Class.forName("com.mysql.jdbc.Driver").newInstance();
		 
	     con=java.sql.DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + maindatabase,"root","");	
	//	 con=java.sql.DriverManager.getConnection("jdbc:mysql://108.252.137.125:3306/" + maindatabase,"root","");		
	}
	public void iniStatements() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		psSelectItem = con.prepareStatement("select * from " + datatablename + " where name = ?");
		psUpdate = con.prepareStatement("update " + datatablename + " set value = ? where name = ?");
		psInsertStartPoint = con.prepareStatement("insert into monitor(program,sid,time) values (?,?,?)");
		}
	
	public database_indexposition() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		iniconnect();
		iniStatements();
	}
	
	public void closecon() throws SQLException
	{
		con.close();
	}
	
	public void close_statements() throws SQLException
	{

		psSelectItem.close();

		psUpdate.close();
		psInsertStartPoint.close();
	}
	
	public int getValueFromName(String name)
	{
		int position_value = 1;
		
		if (name == null)
		{
			return position_value;
		}
		
		try {
		    
			psSelectItem.setString(1,name);           // set parameter
			
		    ResultSet rst= psSelectItem.executeQuery(); 
		   
		    while(rst.next())
		    {    
		    	position_value = rst.getInt(2);
	    	
		    	break;
		    }

		    return position_value;

		}
		catch(SQLException se){
			return position_value;
		}
		
	}
	
	public void update(int value, String name) throws SQLException
	{
		psUpdate.setInt( 1,value ); 
   		psUpdate.setString( 2,name ); 
   		psUpdate.executeUpdate(); 
	}	
	public void insertStartPoint( String name,int value,Timestamp time) throws SQLException
	{
		
		psInsertStartPoint.setString( 1,name ); 
		psInsertStartPoint.setInt( 2,value ); 
		psInsertStartPoint.setTimestamp( 3,time ); 
		psInsertStartPoint.executeUpdate(); 
	}	
}
