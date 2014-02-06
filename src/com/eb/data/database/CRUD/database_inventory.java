
package com.eb.data.database.CRUD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import snaq.db.DBPoolDataSource;

import com.eb.configure.config_db;
import com.eb.data.dataobject.Item_Inventory;
import com.eb.data.dataobject.Item_InventoryExtension;
import com.eb.data.dataobject.Item_InventoryExtension2;
import com.eb.data.dataobject.Item_InventoryExtension3;
import com.mysql.jdbc.Driver;

public class database_inventory{
	
	private Connection con;
	private PreparedStatement psInsert;
	private PreparedStatement psInsertSummit;
	private PreparedStatement psSelectItem;
	private PreparedStatement psSelectItem_FromAsin;
	private PreparedStatement psSelectItem_FromIsbn;
	private PreparedStatement psSelectItem_FromSku;
	private PreparedStatement psSelectAsin;
//	private PreparedStatement psDeleteIsbn;
//	private PreparedStatement psDeleteAll;
	private PreparedStatement psSelectAll;
	private PreparedStatement psUpdate;
	private PreparedStatement psUpdate_US_and_Price;
	private PreparedStatement psUpdate_AmazonPrice_bySku;
	private PreparedStatement psUpdate_CompatitorPrice_bySku;
	private PreparedStatement psUpdate_AmazonPrice_byAsin;
	private PreparedStatement psUpdate_JPCompator_and_Price;
	private PreparedStatement psUpdate_BT_and_Price;
	private PreparedStatement psUpdateINTpostage;
	private PreparedStatement psUpdate_Isbn_byAsin;
	private PreparedStatement psGetMaxId;
	private PreparedStatement psGetListFromIdArrange;
	private PreparedStatement psGetListFromIdArrange2;
	private PreparedStatement psGetGoodBooks;
	private PreparedStatement psRank;
	private PreparedStatement psListPrice;
	private PreparedStatement psInsertPriceHistory;
	private PreparedStatement psInsertPriceNewest;
	private PreparedStatement psUpdateNewestPrice;
	private String[] datatable_names;
	private String datatablename;
	private int datatable_index;
	private static List<Item_InventoryExtension> inventorylist;
	private static List<Item_InventoryExtension2> booklist;
	private CallableStatement csCreateReport;
	static{
		inventorylist = new ArrayList<Item_InventoryExtension>() ;
		booklist = new ArrayList<Item_InventoryExtension2>() ;
	}
	public void iniconnect() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
	     Connection connection = null;
	        try {
	            // Load the JDBC driver
	            Class driver_class = Class.forName("com.mysql.jdbc.Driver");
	            Driver driver = (Driver)driver_class.newInstance();
	            DriverManager.registerDriver(driver);
	            String maindatabase = config_db.getDatabaseName();
		   		 datatable_names = config_db.getDatatableName();
		   		 datatablename = datatable_names[0];
		   		 datatable_index++;
		   		 String url = "jdbc:mysql://127.0.0.1:3306/" + maindatabase;
		   		 DBPoolDataSource ds = new DBPoolDataSource();
	                    ds.setName("pool-ds");
	                    ds.setDescription("Pooling DataSource");
	                    ds.setDriverClassName("com.mysql.jdbc.Driver");
	                    ds.setUrl(url);
	                    ds.setUser("root");
	                    ds.setPassword("");
	                    ds.setMinPool(2);
	                    ds.setMaxPool(3);
	                    ds.setMaxSize(30);
	                    ds.setIdleTimeout(3600);
	                    con = ds.getConnection();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InstantiationException e) {
	            e.printStackTrace();
	        }
	}
	
	public void iniStatements() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
 		
//	     psInsert = con.prepareStatement( 
//			    "insert into " + datatablename + "(sku,asin,price,quantity,maincondition,subcondition,isbn)" +
//			    "values(?,?,?,?,?,?,?)");
	     psInsert = con.prepareStatement( 
				    "insert into " + datatablename + "(isbn)" +
				    "values(?)");
	     psInsertSummit = con.prepareStatement( 
				    "insert into summit (isbn,tulprice,tulprice2,tnlprice,time)" +
				    "values(?,?,?,?,?)");
		 psSelectItem = con.prepareStatement("select * from " + datatablename + " where sku = ? and asin = ?");
		 
		 psSelectItem_FromAsin = con.prepareStatement("select * from " + datatablename + " where asin = ?");
		 
		 psSelectItem_FromIsbn = con.prepareStatement("select * from " + datatablename + " where isbn = ?");
		 
		 psSelectItem_FromSku = con.prepareStatement("select * from " + datatablename + " where sku = ?");

		 psSelectAsin = con.prepareStatement("select asin from " + datatablename + " where sku = ?" );
		 		 
	//	 psDeleteIsbn = con.prepareStatement("delete from inventory where asin = ?");
		 
		 psSelectAll = con.prepareStatement("select * from " + datatablename + "");
		 
	//	 psDeleteAll = con.prepareStatement("TRUNCATE TABLE inventory");
		 
		 psUpdate = con.prepareStatement("update " + datatablename + " set asin = ?, price = ?, amazoncom_price = ?, better_world_price = ?, competitors_price = ?, quantity = ?, AP_thirdparty = ?, amazon_new_lowest = ?, amazon_good_lowest = ?, amazon_acceptable_lowest = ? where sku = ?");
		 
		 psUpdate_US_and_Price = con.prepareStatement("update " + datatablename + " set price = ?, amazoncom_price = ? where sku = ?");
		 
		 psUpdate_AmazonPrice_bySku = con.prepareStatement("update " + datatablename + " set amazoncom_price = ? where sku = ?");
		 
		 psUpdate_CompatitorPrice_bySku = con.prepareStatement("update " + datatablename + " set competitors_price = ? where sku = ?");
		 
		 psUpdate_AmazonPrice_byAsin = con.prepareStatement("update " + datatablename + " set amazoncom_price = ? where asin = ?");
		 
		 psUpdate_JPCompator_and_Price = con.prepareStatement("update " + datatablename + " set price = ?, competitors_price = ? where sku = ?");
		 		 
		 psUpdate_BT_and_Price = con.prepareStatement("update " + datatablename + " set price = ?, better_world_price	 = ? where sku = ?");
		 
		 psUpdate_Isbn_byAsin = con.prepareStatement("update " + datatablename + " set isbn = ? where asin = ?");
		 
		 psGetMaxId = con.prepareStatement("SELECT MAX( id ) FROM  " + datatablename );
		 
		 psGetListFromIdArrange = con.prepareStatement("SELECT * FROM  " + datatablename + " WHERE `salesrank`<50000 AND `id` >=? AND  `id` <=? ");
		 psGetListFromIdArrange2 = con.prepareStatement("SELECT * FROM  " + datatablename + " WHERE  `id` >=? AND  `id` <=? ");
		 psGetGoodBooks = con.prepareStatement("SELECT * FROM  textbook_good_tmp");
		 psRank = con.prepareStatement("update " + datatablename + " set salesrank = ? where isbn = ?");
		 
		 psListPrice = con.prepareStatement("update " + datatablename + " set listprice = ? where isbn = ?");
		 psInsertPriceHistory = con.prepareStatement("insert into pricehistory(isbn,aprice,tnlprice,tulprice,time,flag) values (?,?,?,?,?,?)");
		 psInsertPriceNewest = con.prepareStatement("insert into pricenewest(isbn,aprice,tnlprice,tulprice,time,flag) values (?,?,?,?,?,?)");
		 psUpdateNewestPrice = con.prepareStatement("update pricenewest set aprice=?,tnlprice=?,tulprice=?,time=?,flag=? where isbn=?");
		 psUpdateINTpostage = con.prepareStatement("update " + datatablename + " set INT_postage = ? where asin = ?");
		 csCreateReport = con.prepareCall("{call find_good_textbook_1000}");
	} 
	
	public database_inventory() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
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
		psInsert.close();
		psSelectItem.close();
		psSelectItem_FromAsin.close();
		psSelectItem_FromSku.close();
		psSelectAsin.close();
		psSelectAll.close();
		psUpdate.close();
		psUpdate_US_and_Price.close();
		psUpdate_AmazonPrice_bySku.close();
		psUpdate_CompatitorPrice_bySku.close();
		psUpdate_AmazonPrice_byAsin.close();
		psUpdate_JPCompator_and_Price.close();
		psUpdate_BT_and_Price.close();
		psUpdate_Isbn_byAsin.close();
		psGetMaxId.close();
		psGetListFromIdArrange.close();
		psGetListFromIdArrange2.close();
		psRank.close();
		psListPrice.close();
		psUpdateNewestPrice.close();
		psUpdateINTpostage.close();
		psGetGoodBooks.close();
		csCreateReport.close();
		psInsertPriceNewest.close();
		psInsertPriceHistory.close();
	}
	
	public void change_datatable()
	{
		if (datatable_index < datatable_names.length)
		{
			datatablename = datatable_names[datatable_index];
			datatable_index++;
		}
		else
		{
			datatable_index = 0;
		}
	}
	
	public String getAsinFromInventory(String sku) // throws SQLException
	{	
		if (sku == null)
		{
			return "";
		}

		String asin = "";
			
		try {
		 
			psSelectAsin.setString(1,asin);           //set parameter
		    ResultSet rst= psSelectAsin.executeQuery(); 
		   
		    while(rst.next())
		    {    
		    //	number ++;
		    	asin = rst.getString(1);
		    	break;
		    }
		 //   if (number == 1)
		 //   {
		    return asin;
		//    }
		//    else
		//    {
		//    	return "";
		//    }
		}
		catch(SQLException se){
			return asin;
		}
	}
	
	public Item_InventoryExtension getItemFromSku(String sku)
	{
		Item_InventoryExtension iie = new Item_InventoryExtension();
		
		if (sku == null)
		{
			return iie;
		}
		try {
		    
			psSelectItem_FromSku.setString(1,sku);           // set parameter
			
		    ResultSet rst= psSelectItem_FromSku.executeQuery(); 
		   
		    while(rst.next())
		    {    
		    	iie.sku = rst.getString(2);
		    	iie.asin = rst.getString(3);
		    	iie.price = rst.getDouble(4);
		    	iie.amazon_price = rst.getDouble(6);
		    	iie.better_world_price = rst.getDouble(7);
		    	iie.competitors_price = rst.getDouble(8);
		    	iie.quantity = rst.getInt(9);
		    	iie.con.condition = rst.getString(10);
		    	iie.con.subcondition = rst.getString(11);
		    	break;
		    }

		    return iie;

		}
		catch(SQLException se){
			return iie;
		}
		
	}

	public ArrayList<Item_InventoryExtension> getItemssssFromAsin(String asin)
	{
		ArrayList<Item_InventoryExtension> iies = new ArrayList<Item_InventoryExtension>();
		
		if (asin == null)
		{
			return iies;
		}
		
		try {
		    
			psSelectItem_FromAsin.setString(1,asin);           // set parameter
			
		    ResultSet rst= psSelectItem_FromAsin.executeQuery(); 
		   
		    while(rst.next())
		    {    
				
				Item_InventoryExtension iie = new Item_InventoryExtension();
				
		    	iie.sku = rst.getString(2);
		    	iie.asin = rst.getString(3);
		    	iie.price = rst.getDouble(4);
		    	iie.postage = rst.getDouble(5);
		    	iie.amazon_price = rst.getDouble(6);
		    	iie.better_world_price = rst.getDouble(7);
		    	iie.competitors_price = rst.getDouble(8);
		    	iie.quantity = rst.getInt(9);
		    	iie.con.condition = rst.getString(10);
		    	iie.con.subcondition = rst.getString(11);
		    	iies.add(iie);

		    }

		    return iies;

		}
		catch(SQLException se){
			return iies;
		}
		
	}
	
	public ArrayList<Item_InventoryExtension> getItemssssFromIsbn(String isbn)
	{
		ArrayList<Item_InventoryExtension> iies = new ArrayList<Item_InventoryExtension>();
		
		if (isbn == null)
		{
			return iies;
		}
		
		try {
		    
			psSelectItem_FromIsbn.setString(1,isbn);           // set parameter
			
		    ResultSet rst= psSelectItem_FromIsbn.executeQuery(); 
		   
		    while(rst.next())
		    {    
				
				Item_InventoryExtension iie = new Item_InventoryExtension();
				
		    	iie.sku = rst.getString(2);
		    	iie.asin = rst.getString(3);
		    	iie.price = rst.getDouble(4);
		    	iie.postage = rst.getDouble(5);
		    	iie.amazon_price = rst.getDouble(6);
		    	iie.better_world_price = rst.getDouble(7);
		    	iie.competitors_price = rst.getDouble(8);
		    	iie.quantity = rst.getInt(9);
		    	iie.con.condition = rst.getString(10);
		    	iie.con.subcondition = rst.getString(11);
		    			    	
		    	iies.add(iie);

		    }

		    return iies;

		}
		catch(SQLException se){
			return iies;
		}
		
	}
	
	public Item_InventoryExtension getItemFromAsin(String asin)
	{
		Item_InventoryExtension iie = new Item_InventoryExtension();
		
		if (asin == null)
		{
			return iie;
		}
		
		try {
		    
			psSelectItem_FromAsin.setString(1,asin);           // set parameter
			
		    ResultSet rst= psSelectItem_FromAsin.executeQuery(); 
		   
		    while(rst.next())
		    {    
		    	iie.sku = rst.getString(2);
		    	iie.asin = rst.getString(3);
		    	iie.price = rst.getDouble(4);
		    	iie.amazon_price = rst.getDouble(6);
		    	iie.better_world_price = rst.getDouble(7);
		    	iie.competitors_price = rst.getDouble(8);
		    	iie.quantity = rst.getInt(9);
		    	iie.con.condition = rst.getString(10);
		    	iie.con.subcondition = rst.getString(11);
		    	break;
		    }

		    return iie;

		}
		catch(SQLException se){
			return iie;
		}
		
	}

	public boolean alreadycontainItem(Item_Inventory ite) // throws SQLException
	{
		boolean re = false;
		try{
			psSelectItem.setString(1,ite.sku);           //set parameter
			psSelectItem.setString(2,ite.asin);   
			
		    ResultSet rst=psSelectItem.executeQuery(); 
	        
		    while(rst.next())
		    {
		    	return true;
		    }   
		}
		catch(SQLException se){
			return re;
		}
		return re;
	}
	
	/*
	 * 
	
	public void insert(Item_InventoryExtension ite) // throws SQLException
	{
		//if (ite.sku == null || ite.asin == null )
		{
		//	return;
		}
		//if (ite.asin.length() != 10 )
		{
		//	return;
		}
		
		// if already have the same asin and isbn, return.
	   	if ( alreadycontainItem(ite) == true )
	   	{
	   		System.out.println("already have inventory" + ite.sku + " , ignore it.");     
	   		
	   	//	System.out.println(ito.toString());      
	   		
	   	//	psUpdateCreated.setString(1,ito.asin);           //set parameter
	   	//	psUpdateCreated.setString(2,ito.isbn);  
	   	//	psUpdateCreated.executeUpdate(); 
	   		
	   		return;
	   	}
	   	
	   	try{
	   		
		    // if already contain sku, but not same asin, update it
		   	String asin = getAsinFromInventory(ite.sku);
		   	
		   	if ( !asin.equals("")   )
		   	{
		   		System.out.println("already have sku with different asin, update it");
		   		update(ite);
		   		
//		   		psDeleteIsbn.setString(1, ite.asin);
//		      	psDeleteIsbn.setString(2, ito.isbn);
//		   		psDeleteIsbn.executeUpdate();
//		   		
		   	}
		   	else
		   	{
			   	System.out.println("inserting sku " + ite.sku + " asin " + ite.asin);
			   		   	
			   	psInsert.setString(1, ite.sku);
			   	psInsert.setString(2, ite.asin);
			   	psInsert.setDouble(3, ite.price);
			   	psInsert.setInt(4, ite.quantity);
			   	psInsert.setString(5, ite.con.condition);
			   	psInsert.setString(6, ite.con.subcondition);
			   	psInsert.setString(7,ite.isbn);
	
			   
		   	}
	   	}
	   	
	   	catch(SQLException se){
			return ;
		}
	}
	
	  
	 **/
	public void insert(Item_InventoryExtension2 ite) // throws SQLException
	{
		//if (ite.sku == null || ite.asin == null )
		{
		//	return;
		}
		//if (ite.asin.length() != 10 )
		{
		//	return;
		}
		
		// if already have the same asin and isbn, return.
	 //  	if ( alreadycontainItem(ite) == true )
	   	{
	 //  		System.out.println("already have inventory" + ite.sku + " , ignore it.");     
	   		
	   	//	System.out.println(ito.toString());      
	   		
	   	//	psUpdateCreated.setString(1,ito.asin);           //set parameter
	   	//	psUpdateCreated.setString(2,ito.isbn);  
	   	//	psUpdateCreated.executeUpdate(); 
	   		
	   //		return;
	   	}
	   	
	   	try{
	   		
		    // if already contain sku, but not same asin, update it
	//	   	String asin = getAsinFromInventory(ite.sku);
		   	
		  // 	if ( !asin.equals("")   )
	//	   	{
//		   		System.out.println("already have sku with different asin, update it");
		   //		update(ite);
		   		
//		   		psDeleteIsbn.setString(1, ite.asin);
//		      	psDeleteIsbn.setString(2, ito.isbn);
//		   		psDeleteIsbn.executeUpdate();
//		   		
		//   	}
		//   	else
		   	{
		//	   	System.out.println("inserting sku " + ite.sku + " asin " + ite.asin);
			   		   	
			   	psInsert.setString(1, ite.isbn);
			   	psInsert.executeUpdate();
			   	psInsert.execute();
			   
		   	}
	   	}
	   	
	   	catch(SQLException se){
	   		se.printStackTrace();
			return ;
		}
	}
	
	public void updatebatch(ArrayList<Item_InventoryExtension> ites) throws SQLException
	{
		 
		 String Updatestr = "update " + datatablename + " set asin = ?, price = ?, amazoncom_price = ?, better_world_price = ?, competitors_price = ?, quantity = ?, AP_thirdparty = ?, amazon_new_lowest = ?, amazon_good_lowest = ?, amazon_acceptable_lowest = ? where sku = ?";
		 con.setAutoCommit(false);   
		 PreparedStatement prest = con.prepareStatement(Updatestr,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY); 
		 
		 Item_InventoryExtension ite = null;
		 
		 for(int x = 0; x < ites.size(); x++){   
			 
			 ite = ites.get(x);
			 
			 prest.setString(1,ite.asin); 
			 prest.setDouble(2,ite.price_new);           //set parameter
			 prest.setDouble(3, ite.amazon_price);
			 prest.setDouble(4, ite.better_world_price);
			 prest.setDouble(5, ite.competitors_price);
			 prest.setInt(6,ite.quantity);
			 prest.setString(7, ite.AP_thirdparty);
			 prest.setDouble(8, ite.amazon_new_lowest);
			 prest.setDouble(9, ite.amazon_good_lowest);
			 prest.setDouble(10, ite.amazon_acceptable_lowest);
			 prest.setString(11,ite.sku);  
	         
	         prest.addBatch();   
	      }   
		  
	      prest.executeBatch();
	      con.commit();		 
	}
/**
	public void update(Item_InventoryExtension ite)
	{
		try{
			
	   		psUpdate.setString(1,ite.asin); 
	   		psUpdate.setDouble(2,ite.price_new);           //set parameter
	   		psUpdate.setDouble(3, ite.amazon_price);
	   		psUpdate.setDouble(4, ite.better_world_price);
	   		psUpdate.setDouble(5, ite.competitors_price);
	   		psUpdate.setInt(6,ite.quantity);
	   		psUpdate.setString(7, ite.AP_thirdparty);
	   		psUpdate.setDouble(8, ite.amazon_new_lowest);
	   		psUpdate.setDouble(9, ite.amazon_good_lowest);
	   		psUpdate.setDouble(10, ite.amazon_acceptable_lowest);
	   		psUpdate.setString(11,ite.sku); 
	
	   		psUpdate.executeUpdate(); 
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	**/
	public void update(Item_InventoryExtension2 ite)
	{
		try{
			
	   		psUpdate.setString(1,ite.isbn); 
	
	   		psUpdate.executeUpdate(); 
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void updateUSprice_and_Price(Item_InventoryExtension ite) throws SQLException
	{
		psUpdate_US_and_Price.setDouble(1,ite.price_new);           //set parameter
		psUpdate_US_and_Price.setDouble(2, ite.amazon_price);
		psUpdate_US_and_Price.setString(3,ite.sku); 
		psUpdate_US_and_Price.executeUpdate();  		
	}
		
	public void update_Amazonprice_bysku(Item_InventoryExtension ite) throws SQLException
	{
		psUpdate_AmazonPrice_bySku.setDouble(1, ite.amazon_price);
		psUpdate_AmazonPrice_bySku.setString(2,ite.sku); 
		psUpdate_AmazonPrice_bySku.executeUpdate();  		
	}
	
	public void update_Compatitorprice_bysku(Item_InventoryExtension ite) throws SQLException
	{
		psUpdate_CompatitorPrice_bySku.setDouble(1, ite.competitors_price);
		psUpdate_CompatitorPrice_bySku.setString(2,ite.sku); 
		psUpdate_CompatitorPrice_bySku.executeUpdate();  		
	}
		
	public void Update_AmazonPrice_byAsin(Item_InventoryExtension ite) throws SQLException
	{
		psUpdate_AmazonPrice_byAsin.setDouble(1, ite.amazon_price);
		psUpdate_AmazonPrice_byAsin.setString(2,ite.asin); 
		psUpdate_AmazonPrice_byAsin.executeUpdate();  		
	}
	
	public void Update_JPCompator_and_Price(Item_InventoryExtension ite) throws SQLException
	{
		psUpdate_JPCompator_and_Price.setDouble(1,ite.price_new);           //set parameter
		psUpdate_JPCompator_and_Price.setDouble(2, ite.competitors_price);
		psUpdate_JPCompator_and_Price.setString(3,ite.sku); 
		psUpdate_JPCompator_and_Price.executeUpdate();  		
	}
		
	public void Update_BT_and_Price(Item_InventoryExtension ite) throws SQLException
	{
		psUpdate_BT_and_Price.setDouble(1,ite.price_new);           //set parameter
		psUpdate_BT_and_Price.setDouble(2, ite.better_world_price);
		psUpdate_BT_and_Price.setString(3,ite.sku); 
		psUpdate_BT_and_Price.executeUpdate();  		
	}
	
	public void setSalesRank(String asin, int u) throws SQLException
	{
		psRank.setInt(1, u);
		psRank.setString(2, asin);
		psRank.executeUpdate();  	
	}
		
	public void setListPrice( Double u,String asin) throws SQLException
	{
		psListPrice.setDouble(1, u);
		psListPrice.setString(2, asin);
		psListPrice.executeUpdate();  	
	}
	public void insertPriceHistory(String isbn,Double aprice,Double tnlprice,Double tulprice,Timestamp time,char flag) throws SQLException{
		psInsertPriceHistory.setString(1, isbn);
		psInsertPriceHistory.setDouble(2, aprice);
		psInsertPriceHistory.setDouble(3, tnlprice);
		psInsertPriceHistory.setDouble(4, tulprice);
		psInsertPriceHistory.setTimestamp(5, time);
		psInsertPriceHistory.setString(6,String.valueOf(flag) );
		psInsertPriceHistory.executeUpdate();
	}
	public void updateNewestPrice(String isbn,Double aprice,Double tnlprice,Double tulprice,Timestamp time,char flag) throws SQLException{
		
		psUpdateNewestPrice.setDouble(1, aprice);
		psUpdateNewestPrice.setDouble(2, tnlprice);
		psUpdateNewestPrice.setDouble(3, tulprice);
		psUpdateNewestPrice.setTimestamp(4, time);
		psUpdateNewestPrice.setString(5,String.valueOf(flag) );
		psUpdateNewestPrice.setString(6, isbn);
		psUpdateNewestPrice.executeUpdate();
	}
	
	
	public void insertPriceHistory_NonTextbook(String isbn,Double aprice,Double tnlprice,Double tulprice,Timestamp time,char flag) throws SQLException{
		psInsertPriceHistory.setString(1, isbn);
		psInsertPriceHistory.setDouble(2, aprice);
		psInsertPriceHistory.setDouble(3, tnlprice);
		psInsertPriceHistory.setDouble(4, tulprice);
		psInsertPriceHistory.setTimestamp(5, time);
		psInsertPriceHistory.setString(6,String.valueOf(flag) );
		psInsertPriceHistory.executeUpdate();
	}
	public void insertPriceNewest(String isbn,Double aprice,Double tnlprice,Double tulprice,Timestamp time,char flag) throws SQLException{
		psInsertPriceNewest.setString(1, isbn);
		psInsertPriceNewest.setDouble(2, aprice);
		psInsertPriceNewest.setDouble(3, tnlprice);
		psInsertPriceNewest.setDouble(4, tulprice);
		psInsertPriceNewest.setTimestamp(5, time);
		psInsertPriceNewest.setString(6,String.valueOf(flag) );
		psInsertPriceNewest.executeUpdate();
	}
	public void insertSummit(String isbn,Double tulprice,Double tulprice2,Double tnlprice,Timestamp time) throws SQLException{
		psInsertSummit.setString(1, isbn);
		psInsertSummit.setDouble(2, tulprice);
		psInsertSummit.setDouble(3, tulprice2);
		psInsertSummit.setDouble(4, tnlprice);
		psInsertSummit.setTimestamp(5, time);
		psInsertSummit.executeUpdate();
	}
	public void UpdateINTpostage(String asin, double p) throws SQLException
	{
		psUpdateINTpostage.setDouble(1, p);
		psUpdateINTpostage.setString(2, asin);
		psUpdateINTpostage.executeUpdate();  	
	}
	public void UpdatepsUpdate_Isbn_byAsin( String isbn, String asin ) throws SQLException
	{
		try{
		psUpdate_Isbn_byAsin.setString(1, isbn);
		psUpdate_Isbn_byAsin.setString(2, asin);
		psUpdate_Isbn_byAsin.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();	
		}
	}
	
	public ArrayList<Item_InventoryExtension> SellectAll() // throws SQLException
	{
		ArrayList<Item_InventoryExtension> Inventories = new ArrayList<Item_InventoryExtension>();
		
		try{
			 ResultSet rst= this.psSelectAll.executeQuery(); 
			 
			 Item_InventoryExtension myII = new Item_InventoryExtension();
	   
		     while(rst.next())
		     {
		    	myII = new Item_InventoryExtension();
		    	
		    	myII.sku = rst.getString(2);
		    	myII.asin = rst.getString(3);
		    	myII.price = rst.getDouble(4);
		    	myII.amazon_price = rst.getDouble(6);
		    	myII.better_world_price = rst.getDouble(7);
		    	myII.competitors_price = rst.getDouble(8);
		    	myII.quantity = rst.getInt(9);
		    	myII.con.condition = rst.getString(10);
		    	myII.con.subcondition = rst.getString(11);

		    	Inventories.add( myII );

		     }   
			 return Inventories;
		}
		catch(SQLException se){
			
			return Inventories;
			
		}
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
	public List<Item_InventoryExtension2> getListFromIdArrange2( int start, int end )
	{
		 List<Item_InventoryExtension2> inventorylist = new ArrayList<Item_InventoryExtension2>();
		
		if ( inventorylist != null )
		{
			inventorylist.clear();
		}
		
		try{
			 psGetListFromIdArrange2.setInt(1,start);           //set parameter
			 psGetListFromIdArrange2.setInt(2,end);   
			 ResultSet rst= this.psGetListFromIdArrange2.executeQuery(); 

		     while(rst.next())
		     {
		    	Item_InventoryExtension2 ii = new Item_InventoryExtension2();
		    	
		    	ii.isbn = rst.getString(2);

		    	inventorylist.add(ii);
		     }   
			 return inventorylist;
		}
		catch(SQLException se){
			se.printStackTrace();
			return inventorylist;
		}
	}
	public void createReport(){
		try {
			csCreateReport.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Item_InventoryExtension3> getGoodBooks(){
		
		List<Item_InventoryExtension3> inventorylist = new ArrayList<Item_InventoryExtension3>();
		
		
		try{
			
			 ResultSet rst= this.psGetGoodBooks.executeQuery(); 

		     while(rst.next())
		     {
		    	Item_InventoryExtension3 ii = new Item_InventoryExtension3();
		    	
		    	ii.isbn = rst.getString(1);
		    	ii.aprice = rst.getDouble(2);
		    	ii.avg_aprice = rst.getDouble(3);
		    	ii.tnlprice = rst.getDouble(4);
		    	ii.avg_tnlprice= rst.getDouble(5);
		    	ii.tulprice = rst.getDouble(6);
		    	ii.avg_tulprice = rst.getDouble(7);
		    	ii.profit_a = 0.85*ii.avg_aprice-ii.aprice-15.35;
		    	ii.profit_tnl = 0.85*ii.avg_tnlprice-ii.tnlprice-15.35;
		    	ii.profit_tul = 0.85*ii.avg_tulprice-ii.tulprice-15.35;
		    	inventorylist.add(ii);
		     }   
			 return inventorylist;
		}
		catch(SQLException se){
			se.printStackTrace();
			return inventorylist;
		}
	}
	public List<Item_InventoryExtension> getListFromIdArrange( int start, int end )
	{
		// List<Item_InventoryExtension> inventorylist = new ArrayList<Item_InventoryExtension>();
		
		if ( inventorylist != null )
		{
			inventorylist.clear();
		}
		
		try{
			 psGetListFromIdArrange.setInt(1,start);           //set parameter
			 psGetListFromIdArrange.setInt(2,end);   
			 ResultSet rst= this.psGetListFromIdArrange.executeQuery(); 

		     while(rst.next())
		     {
		    	Item_InventoryExtension ii = new Item_InventoryExtension();
		    	
		    	ii.sku = rst.getString(2);
		    	ii.asin = rst.getString(3);
		    	ii.price = rst.getDouble(4);
		    	ii.postage = rst.getDouble(5);
		    	ii.amazon_price = rst.getDouble(6);
		    	ii.quantity = rst.getInt(9);
		    	ii.con.condition = rst.getString(10);
		    	ii.con.subcondition = rst.getString(11);
		    	ii.salesrank = rst.getInt(13);
		    	ii.isbn = rst.getString(14);
		    	ii.amazon_new_lowest = rst.getDouble(15);
		    	ii.amazon_good_lowest = rst.getDouble(16);
		    	ii.amazon_acceptable_lowest = rst.getDouble(17);

		    	inventorylist.add(ii);
		     }   
			 return inventorylist;
		}
		catch(SQLException se){
			se.printStackTrace();
			return inventorylist;
		}
	}
	
	
	public List<Item_InventoryExtension2> getBookListFromIdArrange( int start, int end )
	{
		// List<Item_InventoryExtension> inventorylist = new ArrayList<Item_InventoryExtension>();
		
		if ( booklist != null )
		{
			booklist.clear();
		}
		try{
			 psGetListFromIdArrange.setInt(1,start);           //set parameter
			 psGetListFromIdArrange.setInt(2,end);   
			 ResultSet rst= this.psGetListFromIdArrange.executeQuery(); 
		     while(rst.next())
		     {
		    	Item_InventoryExtension2 ii = new Item_InventoryExtension2();
		    	ii.isbn = rst.getString(2);
		    	booklist.add(ii);
		     }   
			 return booklist;
		}
		catch(SQLException se){
			return booklist;
		}
	}
	
	
}
