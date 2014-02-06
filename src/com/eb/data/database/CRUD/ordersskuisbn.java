package com.eb.data.database.CRUD;
import java.sql.*;

import com.eb.configure.config_db;


import dataobject.ItemOrder;

public class ordersskuisbn{
	
	private Connection con;
	private PreparedStatement psInsert;
	private PreparedStatement psUpdate;
	private PreparedStatement psSelectItem;
	private PreparedStatement psDeletebyOrderId;
	
	private PreparedStatement psSelectSkuIsbnTimes;
	
	private PreparedStatement psSelectWorkerCode;
	
	private PreparedStatement psGetMaxId;
	private PreparedStatement psSelectSkuAsinProfit;
	
//	private PreparedStatement  psUpdate;
	
	public void iniconnect() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{	
		 String mydatabase = config_db.getDatabaseName();
		 
		 Class.forName("com.mysql.jdbc.Driver").newInstance();
		   // Class.forName("org.gjt.mm.mysql.Driver").newInstance();
	   //con=java.sql.DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/amazon","root","");		 
		 con=java.sql.DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + mydatabase,"root","");	
	     psInsert = con.prepareStatement(
			    "insert into ordersskuisbn(order_id,price,purchase_date,quantity_purchased,recipient_name,ship_address_1,ship_address_2,ship_city,shipping_fee,ship_state,ship_zip,sku,isbn,asin,SELLER,SELLER_PRICE,CODE,COST,ORDER_NUMBER,ACCOUNT,PROFIT,reference) " +
			    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	     psUpdate = con.prepareStatement(
				    "update ordersskuisbn SET order_id=?,price=?,purchase_date=?,quantity_purchased=?,recipient_name=?,ship_address_1=?,ship_address_2=?,ship_city=?,shipping_fee=?,ship_state=?,ship_zip=?,sku=?,isbn=?,asin=?,SELLER=?,SELLER_PRICE=?,CODE=?,COST=?,ORDER_NUMBER=?,ACCOUNT=?,PROFIT=?,reference=? " +
				    " where order_id = ?");
	     
	     psDeletebyOrderId = con.prepareStatement("DELETE FROM `ordersskuisbn` WHERE `order_id` = ?");
	     
		 String sql="select count(*) from ordersskuisbn where order_id = ? and sku = ? and isbn = ?";
		 psSelectItem= con.prepareStatement(sql);
		 
		 psSelectSkuIsbnTimes = con.prepareStatement("select count(*) from ordersskuisbn where sku = ? and isbn = ?");
		 
		 psSelectWorkerCode =  con.prepareStatement("select id, CODE from ordersskuisbn where sku = ? and isbn = ? ORDER BY id DESC"); // 
		 psSelectSkuAsinProfit = con.prepareStatement("select sku, asin, profit from ordersskuisbn where id = ? ");
		 psGetMaxId = con.prepareStatement("SELECT MAX( id ) FROM  `ordersskuisbn`");
		 
	}
	
	public ordersskuisbn() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		iniconnect();
	}
	
	public void closecon() throws SQLException
	{
		psInsert.close();
		psSelectItem.close();
		psSelectSkuIsbnTimes.close();
	//	psUpdate.close();
		con.close();
	}
	
	public int getAmountOfSkuIsbn(String sku, String Isbn) // throws SQLException
	{
		int number = 0;
		try{
			psSelectSkuIsbnTimes.setString(1, sku);
			psSelectSkuIsbnTimes.setString(2, Isbn);
			
			ResultSet rst=psSelectSkuIsbnTimes.executeQuery(); 
	
			while(rst.next())
		    {
				number = rst.getInt(1);
	
		    	return number;
		    }   
		}
		catch(SQLException se){
			return number;
		}
		return number;
	}

	public ItemOrder getSkuAsinProfitByID(int id) throws SQLException
	{
		
		ItemOrder myio = new ItemOrder();
		psSelectSkuAsinProfit.setInt(1, id);
		ResultSet rst=psSelectSkuAsinProfit.executeQuery(); 
	    if(rst.next())
	    {
	    	myio.sku = rst.getString(1);
	    	myio.asin = rst.getString(2);
	    	myio.PROFIT = rst.getString(3);
	    }   
	    return myio;
		
	}
	
	public boolean alreadycontainOrderSkuIsbn(ItemOrder ito)// throws SQLException
	{
		boolean re = false;
		try{
			psSelectItem.setString(1,ito.order_id);           //set parameter
			psSelectItem.setString(2,ito.sku);   
			psSelectItem.setString(3,ito.isbn);   
			
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
	
	public void write(ItemOrder ito) throws SQLException // throws SQLException
	{
	
	   	if ( alreadycontainOrderSkuIsbn(ito) == true )
	   	{
	   		/* update first
	   		System.out.println("already have order sku isbn. update it");
	   		psUpdate.setString(1, ito.order_id);
	   		psUpdate.setString(2, ito.price);
	   		psUpdate.setString(3, ito.purchase_date);
	   		psUpdate.setString(4, ito.quantity_purchased);
	   		psUpdate.setString(5, ito.recipient_name);
	   		psUpdate.setString(6, ito.ship_address_1);
	   		psUpdate.setString(7, ito.ship_address_2);
	   		psUpdate.setString(8, ito.ship_city);
	   		psUpdate.setString(9, ito.shipping_fee);
	   		psUpdate.setString(10, ito.ship_state);
	   		psUpdate.setString(11, ito.ship_zip);
	   		psUpdate.setString(12, ito.sku);
	   		psUpdate.setString(13, ito.isbn);
		// 	psInsert.setInt(14, ito.purchasetimes);
	   		psUpdate.setString(14, ito.asin);
	   		psUpdate.setString(15, ito.SELLER);
	   		psUpdate.setString(16, ito.SELLER_PRICE);
	   		psUpdate.setString(17, ito.CODE);
	   		psUpdate.setString(18, ito.COST);
	   		psUpdate.setString(19, ito.ORDER_NUMBER);
	   		psUpdate.setString(20, ito.ACCOUNT);
	   		psUpdate.setString(21, ito.PROFIT);
	   		psUpdate.setString(22, ito.reference);
	   		psUpdate.setString(23, ito.order_id);
	   		psUpdate.execute();
	   	//	System.out.println(ito.toString());         
	   	    return;
	   	    */
	   		
	   		/* delete first */
	   		System.out.println("already have order sku isbn. delete it first");
	   		psDeletebyOrderId.setString(1,  ito.order_id);
	   		psDeletebyOrderId.execute();
	   	}
	    
	   	try{
		   	//System.out.println("insert");
		   	psInsert.setString(1, ito.order_id);
		   	psInsert.setString(2, ito.price);
		   	psInsert.setString(3, ito.purchase_date);
		   	psInsert.setString(4, ito.quantity_purchased);
		   	psInsert.setString(5, ito.recipient_name);
		   	psInsert.setString(6, ito.ship_address_1);
		   	psInsert.setString(7, ito.ship_address_2);
		   	psInsert.setString(8, ito.ship_city);
		   	psInsert.setString(9, ito.shipping_fee);
		   	psInsert.setString(10, ito.ship_state);
		   	psInsert.setString(11, ito.ship_zip);
		   	psInsert.setString(12, ito.sku);
		   	psInsert.setString(13, ito.isbn);
		// 	psInsert.setInt(14, ito.purchasetimes);
		   	psInsert.setString(14, ito.asin);
		   	psInsert.setString(15, ito.SELLER);
		   	psInsert.setString(16, ito.SELLER_PRICE);
		   	psInsert.setString(17, ito.CODE);
		   	psInsert.setString(18, ito.COST);
		   	psInsert.setString(19, ito.ORDER_NUMBER);
		   	psInsert.setString(20, ito.ACCOUNT);
		   	psInsert.setString(21, ito.PROFIT);
		   	psInsert.setString(22, ito.reference);
		   	psInsert.execute();
	   	}
	   	catch(SQLException se){
			return;
		}
	}
	
	public String getWorkerCode(String sku, String Isbn) // throws SQLException
	{
		try{
			psSelectWorkerCode.setString(1, sku);
			psSelectWorkerCode.setString(2, Isbn);
			
			ResultSet rst=psSelectWorkerCode.executeQuery(); 
		//	int number = 0;
			if(rst.next())
		    {
				return rst.getString(2);
	
		    //	return number;
		    }   
		}
		catch(SQLException se){
			return null;
		}
		return null;
	}
	
	public int getMaxId() throws SQLException
	{
		ResultSet rst= psGetMaxId.executeQuery(); 
		if(rst.next())
	    {
			return rst.getInt(1);
	    }   
		else
		{
			return 0;
		}
	}
}

    
    
