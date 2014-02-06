
package com.eb.data.database.CRUD;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.eb.configure.config_db;

import dataobject.ItemOrder;

public class skuisbn{
	
	private Connection con;
	private PreparedStatement psInsert;
	private PreparedStatement psSelectItemAccount;
	private PreparedStatement psSelectIsbn;
	private PreparedStatement psSelectCheckTimes;
	private PreparedStatement psUpdataCheckTimes;
	private PreparedStatement psUpdataAsin;
	private PreparedStatement psUpdataIsbnAsin;
	private PreparedStatement psUpdateFlag;
	// private PreparedStatement psDeleteIsbn;
	private PreparedStatement psDeleteAll;

	private PreparedStatement psSelectAll;
	private PreparedStatement psSelectStartEnd;
	private PreparedStatement psSelectAllCreated;
	private PreparedStatement psSelectCount;
	
	private Map<String, String> mymap = new HashMap<String, String>();
	private Map<String, String> mymapchecktime = new HashMap<String, String>();
	
	public void iniconnect() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		 String mydatabase = config_db.getDatabaseName();
		 
		 Class.forName("com.mysql.jdbc.Driver").newInstance();
		 
	     con=java.sql.DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + mydatabase,"root","");		 
			
	     psInsert = con.prepareStatement(
			    "insert into skuisbn(sku,isbn,asin,checktime,iscreated) " +
			    "values(?,?,?,?,?)");
	     
		 psSelectItemAccount= con.prepareStatement("select count(*) from skuisbn where sku = ? and isbn = ?");

		 psSelectIsbn = con.prepareStatement("select isbn from skuisbn where sku = ?" );
		 
		 psSelectCheckTimes = con.prepareStatement("select checktime from skuisbn where sku = ?" );
		 
		 psUpdataCheckTimes = con.prepareStatement("update skuisbn set checktime = checktime + 1 where sku = ? and isbn = ?");
		 psUpdataAsin = con.prepareStatement("update skuisbn set asin = ? where sku = ? and isbn = ?");
		 psUpdataIsbnAsin = con.prepareStatement("update skuisbn set isbn = ?, asin = ? where sku = ?");
		 psUpdateFlag = con.prepareStatement("update skuisbn set iscreated = ? where sku = ?");
		 psSelectAll = con.prepareStatement("select * from skuisbn");
		 psSelectStartEnd = con.prepareStatement("SELECT * FROM  `skuisbn` WHERE  `id` >=? AND  `id` <=? ");
		 psSelectCount =  con.prepareStatement("select count(*) from skuisbn");
		 psSelectAllCreated = con.prepareStatement("select * from skuisbn where iscreated = '0' ");
		 
		 psDeleteAll = con.prepareStatement("TRUNCATE TABLE skuisbn");
	}
	
	public skuisbn() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		iniconnect();
	}
	
	public void closecon() throws SQLException
	{
		psInsert.close();
		psSelectItemAccount.close();
		psSelectIsbn.close();
		psSelectCheckTimes.close();
//		psUpdate.close();
		con.close();
	}
	
	public void UpdagteCheckTimesFromSku(String sku, String Isbn) // throws SQLException
	{	
		try{
		//	psUpdataCheckTimes.setInt(1,1);  
			psUpdataCheckTimes.setString(1,sku);           //set parameter
			psUpdataCheckTimes.setString(2,Isbn);  
		    psUpdataCheckTimes.executeUpdate(); 
		}
	    catch(SQLException se){
			return;
		}
	}
	
	public void UpdagteFlagFromSku(String sku, int flag) // throws SQLException
	{	
		try{
			psUpdateFlag.setInt(1,flag);  
			psUpdateFlag.setString(2,sku);           //set parameter
			
			psUpdateFlag.executeUpdate(); 
		}
	    catch(SQLException se){
			return;
		}
	}
	
	public int getCheckTimesFromSku(String sku)// throws SQLException
	{	 
		int checktimes = 0;
	
        try{
			psSelectCheckTimes.setString(1,sku);           //set parameter
		    ResultSet rst=psSelectCheckTimes.executeQuery(); 
		   
		    while(rst.next())
		    {    
		    	checktimes = rst.getInt(1);
		    	return checktimes;
		    }
        }
	    catch(SQLException se){
			return checktimes;
		}
	    return checktimes;

	}
	
	public String getIsbnFromSku(String sku) // throws SQLException
	{	
		String isbn = "";
			
		try {
		 
			psSelectIsbn.setString(1,sku);           //set parameter
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
	
	public boolean alreadycontinueSkuIsbn(ItemOrder ito) // throws SQLException
	{
		boolean re = false;
		try{
			psSelectItemAccount.setString(1,ito.sku);           //set parameter
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
		if (ito.sku == null || ito.isbn == null )
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
		// if already have the same sku and isbn, return.
	   	if ( alreadycontinueSkuIsbn(ito) == true )
	   	{
	   		System.out.println("already have skuIsbn, ignore.");        
	   	//	System.out.println(ito.toString());      
	   		
	   	//	psUpdateCreated.setString(1,ito.sku);           //set parameter
	   	//	psUpdateCreated.setString(2,ito.isbn);  
	   	//	psUpdateCreated.executeUpdate(); 
	   		
	   		return;
	   	}
	   	
	   	try{
		    // if already contain sku, but not same isbn, delete it first
		   	String isbn = getIsbnFromSku(ito.sku);
		   	
		   	if ( !isbn.equals("")   )
		   	{
		   		System.out.println("already have sku, update it");   
		   		
		   		psUpdataIsbnAsin.setString(1,ito.isbn); 
		   		psUpdataIsbnAsin.setString(2,ito.asin);           //set parameter
		   		psUpdataIsbnAsin.setString(3,ito.sku);  
		   		psUpdataIsbnAsin.executeUpdate(); 
		   	}
		   
		   	//System.out.println("insert");
		   	else{	  
		   		System.out.println("insert new sku isbn");
			   	psInsert.setString(1, ito.sku);
			   	psInsert.setString(2, ito.isbn);
			   	psInsert.setString(3, ito.asin);
			   	psInsert.setInt(4, 0);
			   	psInsert.setInt(5, 0);
			   	psInsert.execute();
		   	}
	   	}
	   	catch(SQLException se){
			return ;
		}
	}

	public void insertFollow(ItemOrder ito) // throws SQLException
	{
		if (ito.sku == null || ito.isbn == null )
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
		
	   	
	   	try{
		 	   
		   	//System.out.println("insert");
		   		   	
		   	psInsert.setString(1, ito.sku);
		   	psInsert.setString(2, ito.isbn);
		   	psInsert.setString(3, ito.asin);
		   	psInsert.setInt(4, 0);
		   	psInsert.setInt(5, 0);
		   	psInsert.execute();
	   	}
	   	catch(SQLException se){
			return ;
		}
	}
	
	public void insertCreated(ItemOrder ito) // throws SQLException
	{
		if (ito.sku == null || ito.isbn == null )
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
		
	   	
	   	try{
		 	   
		   	//System.out.println("insert");
		   		   	
		   	psInsert.setString(1, ito.sku);
		   	psInsert.setString(2, ito.isbn);
		   	psInsert.setString(3, ito.asin);
		   	psInsert.setInt(4, 0);
		   	psInsert.setInt(5, 1);
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
		     String sku = "";
		     String isbn = "";
		     String checktime = "";
		  // String price = "";
		     while(rst.next())
		     {
		    //	int number = rst.getInt(1);
		    	sku = rst.getString(2);
		    	isbn = rst.getString(3);
		    	checktime = rst.getString(4);
		    //	price = rst.getString(4);
		    	mymap.put(sku, isbn);
		    	mymapchecktime.put(sku, checktime);
		     }   
			 return this.mymap;
		}
		catch(SQLException se){
			return this.mymap;
		}
	}
	
	public Map<String, String> SellectStartEnd(int start, int end) // throws SQLException
	{
		try{
			 psSelectStartEnd.setInt(1,start);           //set parameter
			 psSelectStartEnd.setInt(2,end);   
			 ResultSet rst= this.psSelectStartEnd.executeQuery(); 
		     String sku = "";
		     String isbn = "";
		     String checktime = "";
		  // String price = "";
		     while(rst.next())
		     {
		    //	int number = rst.getInt(1);
		    	sku = rst.getString(2);
		    	isbn = rst.getString(3);
		    	checktime = rst.getString(4);
		    //	price = rst.getString(4);
		    	mymap.put(sku, isbn);
		    	mymapchecktime.put(sku, checktime);
		     }   
			 return this.mymap;
		}
		catch(SQLException se){
			return this.mymap;
		}
	}
	
	public int getTotalCount()
	{
		try{  
			 ResultSet rst= this.psSelectCount.executeQuery(); 
		     int totalCount = 0;

		     while(rst.next())
		     {
		    	totalCount = rst.getInt(1);
		     }   
			 return totalCount;
		}
		catch(SQLException se){
			return 0;
		}
	}
	
	public Map<String, String> SellectAllCreated() // throws SQLException
	{
		try{
			 ResultSet rst= this.psSelectAllCreated.executeQuery(); 
		     String sku = "";
		     String isbn = "";
		     String checktime = "";
		  // String price = "";
		     while(rst.next())
		     {
		    //	int number = rst.getInt(1);
		    	sku = rst.getString(2);
		    	isbn = rst.getString(3);
		    	checktime = rst.getString(4);
		    //	price = rst.getString(4);
		    	mymap.put(sku, isbn);
		    	mymapchecktime.put(sku, checktime);
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
	
	public Map<String, String> SellectAllChecktime() // throws SQLException
	{
		return this.mymapchecktime;
	}
	
	public void writeAsin(ItemOrder ito) // throws SQLException
	{
		if (ito.sku == null || ito.isbn == null )
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
	   	try{
	   		psUpdataAsin.setString(1,ito.asin); 
	   		psUpdataAsin.setString(2,ito.sku);           //set parameter
	   		psUpdataAsin.setString(3,ito.isbn);  
	   		psUpdataAsin.executeUpdate(); 
	   	}
	   	catch(SQLException se){
			return ;
		}
	}
	
}

    
    
