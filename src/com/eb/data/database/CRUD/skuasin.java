package com.eb.data.database.CRUD;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.eb.configure.config_db;

import dataobject.ItemAsin;

// product_id is asin

public class skuasin{
	
	private Connection con;
	private PreparedStatement psInsert;
	private PreparedStatement  psSelectItem;
	private PreparedStatement psSelectAsin;
	private PreparedStatement psDeleteAsin;
	private PreparedStatement psSelectAll;
	private PreparedStatement psDeleteAll;
	private Map<String, String> mymap = new HashMap<String, String>();
	
//	private PreparedStatement  psUpdate;
	
	public void iniconnect() throws SQLException 
	{
		String mydatabase = config_db.getDatabaseName();
	   try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		System.out.println("Instance newed");
	} catch (InstantiationException | IllegalAccessException
			| ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		con=java.sql.DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + mydatabase,"root","");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}		 
	   try {
		psInsert = con.prepareStatement(
				    "insert into skuasin(sku,quantity,price,product_id) " +
				    "values(?,?,?,?)");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   try {
		psSelectItem = con.prepareStatement("select count(*) from skuasin where sku = ? and product_id = ?");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   psSelectAsin = con.prepareStatement("select product_id from skuasin where sku = ?");
	   
	   //	   psUpdate = con.prepareStatement(
	//			    "update ordersskuisbn set checktime = checktime+1 where sku = ? and isbn = ?");
	   psDeleteAsin = con.prepareStatement("delete from skuasin where sku = ?");
       psSelectAll = con.prepareStatement("select * from skuasin");
       psDeleteAll = con.prepareStatement("TRUNCATE TABLE skuasin");
       
	}
	
	public skuasin() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		iniconnect();
	}
	
	public void closecon() throws SQLException
	{
		psInsert.close();
		psSelectItem.close();
		psSelectAsin.close();
		psDeleteAsin.close();
//		psUpdate.close();
		con.close();
	}
	
	public String getAsinFromSku(String sku) // throws SQLException
	{	
		String al = "";
		try{
			psSelectAsin.setString(1,sku);           //set parameter
		    ResultSet rst=psSelectAsin.executeQuery(); 
		    
		    while(rst.next())
		    {
		    	return rst.getString(1);
		    }
		}
		catch(SQLException se){
			return al;
		}
		return al;
	}
		
	public boolean alreadycontinueSkuAsin(ItemAsin ia) // throws SQLException
	{
		boolean re = false;
		
		try{
			psSelectItem.setString(1,ia.sku);           //set parameter
			psSelectItem.setString(2,ia.asin);   
			
		    ResultSet rst=psSelectItem.executeQuery(); 
	        
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
	
	public void write(ItemAsin ito)// throws SQLException
	{	
		if (ito.sku == null || ito.asin == null )
		{
			return;
		}
		if ( ito.asin.length() != 10 )
		{
			return;
		}
		if (ito.asin.equals("0"))
		{
			return;
		}
		
	  	if ( alreadycontinueSkuAsin(ito) == true )
	   	{
	   		System.out.println("already have skuasin:");        
	   		// System.out.println(ito.toString());         
	   	    return;
	   	}
	    
	  	try{
		   	// if already contain sku, delete it first
		   	String asin = getAsinFromSku(ito.sku);
		   	if ( !asin.equals("")   )
		   	{
		   		System.out.println("already have sku, delete it first");   
		   		psDeleteAsin.setString(1, ito.sku);
		   		psDeleteAsin.executeUpdate();
		   	}
		   	//System.out.println("insert");
		   	psInsert.setString(1, ito.sku);
		   	psInsert.setInt(2, ito.quantity);
		   	psInsert.setDouble(3, ito.price);
		   	psInsert.setString(4, ito.asin);
	        	 
		   	psInsert.execute();
	  	}
	  	catch(SQLException se){
			return ;
		}
	   	
	}
	
	public void insert(ItemAsin ito) // throws SQLException
	{	
		if (ito.sku == null || ito.asin == null )
		{
			return;
		}
		if ( ito.asin.length() != 10 )
		{
			return;
		}
		if (ito.asin.equals("0"))
		{
			return;
		}
	    
	  	try{
		   	//System.out.println("insert");
		   	psInsert.setString(1, ito.sku);
		   	psInsert.setInt(2, ito.quantity);
		   	psInsert.setDouble(3, ito.price);
		   	psInsert.setString(4, ito.asin);
	        	 
		   	psInsert.execute();
	  	}
	  	catch(SQLException se){
			return ;
		}
	   	
	}
	
	
	public Map<String, String> SellectAll()// throws SQLException
	{
		try{
			 ResultSet rst= this.psSelectAll.executeQuery(); 
		     String sku = "";
		     String product_id = "";
		  //   String price = "";
		     while(rst.next())
		     {
		    //	int number = rst.getInt(1);
		    	sku = rst.getString(2);
		    	product_id = rst.getString(5);
		    //	price = rst.getString(4);
		    	mymap.put(sku, product_id);
		     }   
		}
		catch(SQLException se){
			return this.mymap;
		}
		return this.mymap;
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

    
    
