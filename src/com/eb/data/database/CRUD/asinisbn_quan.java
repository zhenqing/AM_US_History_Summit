
package com.eb.data.database.CRUD;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.eb.configure.config_db;


import dataobject.ItemOrder;

public class asinisbn_quan{
	
	private Connection con;
	private PreparedStatement psInsert;
	private PreparedStatement psSelectItemAccount;
	private PreparedStatement psSelectIsbn;
	private PreparedStatement psDeleteIsbn;
	private PreparedStatement psDeleteAll;
	private PreparedStatement psSelectAll;
	private Map<String, String> mymap = new HashMap<String, String>();
	
	public void iniconnect() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		 String maindatabase = config_db.getDatabaseName();
		 
		 Class.forName("com.mysql.jdbc.Driver").newInstance();
		 
	     con=java.sql.DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + maindatabase,"root","");		 
			
	     psInsert = con.prepareStatement(
			    "insert into asinisbn(asin,isbn)" +
			    "values(?,?)");
	     
		 psSelectItemAccount= con.prepareStatement("select count(*) from asinisbn where asin = ? and isbn = ?");

		 psSelectIsbn = con.prepareStatement("select isbn from asinisbn where asin = ?" );
		 		 
		 psDeleteIsbn = con.prepareStatement("delete from asinisbn where asin = ?");
		 
		 psSelectAll = con.prepareStatement("select * from asinisbn");
		 
		 psDeleteAll = con.prepareStatement("TRUNCATE TABLE asinisbn");
	}
	
	public asinisbn_quan() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		iniconnect();
	}
	
	public void closecon() throws SQLException
	{
		psInsert.close();
		psSelectItemAccount.close();
		psSelectIsbn.close();
		con.close();
	}
	
	public String getIsbnFromAsin(String asin) // throws SQLException
	{	
		if (asin == null)
		{
			return "";
		}
		if (asin.length() != 10)
		{
			return "";
		}
		String isbn = "";
			
		try {
		 
			psSelectIsbn.setString(1,asin);           //set parameter
		    ResultSet rst=psSelectIsbn.executeQuery(); 
		   
		    while(rst.next())
		    {    
		    //	number ++;
		    	isbn = rst.getString(1);
		    }
		 //   if (number == 1)
		 //   {
		    	return isbn;
		//    }
		//    else
		//    {
		//    	return "";
		//    }
		}
		catch(SQLException se){
			return isbn;
		}
	}
	
	public boolean alreadycontainAsinIsbn(ItemOrder ito) // throws SQLException
	{
		boolean re = false;
		try{
			psSelectItemAccount.setString(1,ito.asin);           //set parameter
			psSelectItemAccount.setString(2,ito.isbn);   
			
		    ResultSet rst=psSelectItemAccount.executeQuery(); 
	        
		    while(rst.next())
		    {
		    	int number = rst.getInt(1);
		    	if ( number != 0 )
		    	{
		    		return true;
		    	}
		    }   
		}
		catch(SQLException se){
			return re;
		}
		return re;
	}
	
	public void write(ItemOrder ito) // throws SQLException
	{
		if (ito.asin == null || ito.isbn == null )
		{
			return;
		}
		if (ito.asin.length() != 10 )
		{
			return;
		}
		if ( ito.isbn.length() != 10 && ito.isbn.length() != 13 )
		{
			return;
		}
		
        if (ito.isbn.equals("0000000000"))
        {
        	return;
        }
		// if already have the same asin and isbn, return.
	   	if ( alreadycontainAsinIsbn(ito) == true )
	   	{
	   		System.out.println("already have asinisbn, ignore.");        
	   	//	System.out.println(ito.toString());      
	   		
	   	//	psUpdateCreated.setString(1,ito.asin);           //set parameter
	   	//	psUpdateCreated.setString(2,ito.isbn);  
	   	//	psUpdateCreated.executeUpdate(); 
	   		
	   		return;
	   	}
	   	
	   	try{
		    // if already contain asin, but not same isbn, delete it first
		   	String isbn = getIsbnFromAsin(ito.asin);
		   	
		   	if ( !isbn.equals("")   )
		   	{
		   		System.out.println("already have asin, delete it first");   
		   		psDeleteIsbn.setString(1, ito.asin);
		   	//	psDeleteIsbn.setString(2, ito.isbn);
		   		psDeleteIsbn.executeUpdate();
		   	}
		   
		   	System.out.println("inserting asin isbn");
		   		   	
		   	psInsert.setString(1, ito.asin);
		   	psInsert.setString(2, ito.isbn);

		   	psInsert.execute();
	   	}
	   	catch(SQLException se){
			return ;
		}
	}
	
	public Map<String, String> SellectAll() // throws SQLException
	{
		try{
			 ResultSet rst= this.psSelectAll.executeQuery(); 
		     String asin = "";
		     String isbn = "";

		     while(rst.next())
		     {
		    //	int number = rst.getInt(1);
		    	asin = rst.getString(2);
		    	isbn = rst.getString(3);

		    //	price = rst.getString(4);
		    	mymap.put(asin, isbn);

		     }   
			 return this.mymap;
		}
		catch(SQLException se){
			return this.mymap;
		}
	}
	
	public void DeleteAll() // throws SQLException
	{
		try{
			this.psDeleteAll.execute(); 
		}
		catch(SQLException se){

		}
	}
	
}

    
    
