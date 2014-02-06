
package com.eb.data.database.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.eb.data.database.CRUD.database_inventory;
import com.eb.data.dataobject.Item_InventoryExtension;
import com.eb.data.dataobject.Item_InventoryExtension2;
import com.eb.data.dataobject.Item_InventoryExtension3;
import com.eb.util.File.ReadFileCatalog;

public class database_inventory_business{
	
	// files

	private String  relatedwordsinfile;
		
	// buffers
			
	// this is the file reader buffer to read the realtedwords
	private BufferedReader relatedBuffer;
			
	private List<String> relalist = new ArrayList<String>();
	
	private int listsize = 0;
	
	private database_inventory db_inven;
	
	private static final String book_language = "EN";

	private void inirelatedbuffer(int i) throws FileNotFoundException
	{   
	//	System.out.println("i:" + i );
		relatedwordsinfile = relalist.get(i);	
		System.out.println("file name: " + relatedwordsinfile );
        File related = new File(relatedwordsinfile);
        FileReader frelated = new FileReader(related);        
        relatedBuffer = new BufferedReader(frelated); 
	}
	
	private ArrayList<Item_InventoryExtension2> getInventoryFromFile() throws IOException, SQLException
	{
		
		ArrayList<Item_InventoryExtension2> Inventories = new ArrayList<Item_InventoryExtension2>();
		
		ReadFileCatalog.ChangeDir("DatabaseFiles");
	//	relalist.clear();
		relalist = ReadFileCatalog.getAllFiles();
	    listsize = relalist.size();
	    System.out.println ( "file list size:" + relalist.size() );
        String linewords = "not empty";
        int fileindex = 0;
               
        while (fileindex < this.listsize)
        {     
     
        	this.inirelatedbuffer(fileindex); 
        	
        	// the first line is header       	
            linewords =  this.relatedBuffer.readLine();
          	
            while (linewords != null)
            { 
            	linewords =  this.relatedBuffer.readLine();
            	System.out.println(linewords);
              	if (linewords == null)
            	{
            		continue;
            	} 
            	if ( linewords.contains("asin") )
            	{
            		continue;
            	}
            	if (linewords == "")
            	{
            		continue;
            	}
 
            //	String strs[] = util.getTokens(linewords);
            	String strs[];
            	if (linewords.contains("\t"))
            	{
            		strs = linewords.split("\t");   
            	}
            	else
            	{
            		strs = linewords.split(",");   
            	}
            	
            	if (strs.length < 2)
            	{
            	//	continue;
            	}
            	/*
            	if ( strs[0].length() == 0  )
            	{ 
            		continue;
            	}
          	          	
            	if ( strs[0] == null )
            	{
            		continue;
            	}
            	     	*/ 
            	while(strs[0].length() < 10)
            	{
            		strs[0] = "0" + strs[0];
            	}
       

            	Item_InventoryExtension2 ii = new Item_InventoryExtension2();
            	
            
            	ii.isbn = strs[0];
       
            	
            	
            	/*
            	if (strs[2].equals("11"))
            	{
            		ii.con.condition = "new";
            		ii.con.subcondition = "new";
            	}
            	else if (strs[2].equals("3"))
            	{
            		ii.con.condition = "used";
            		ii.con.subcondition = "good";
            	}
            	else
            	{
            		ii.con.condition = "new";
            		ii.con.subcondition = "new";
            	}
            	*/
            	
            	//ii.con.condition = "new";
        		//ii.con.subcondition = "new";
        		
            	Inventories.add(ii);
                         
            }
            fileindex++;
          
         }        
        // read io close
        this.relatedBuffer.close();       

        System.out.println("read inventory finish");
        
        return Inventories;
        
	}
		
	public database_inventory_business() throws IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		db_inven = new database_inventory();
	}
	
	public void insertAllInventoryFromFile() throws IOException, SQLException
	{
		
		ArrayList<Item_InventoryExtension2> Inventories = getInventoryFromFile();
		
		for (int i = 0; i < Inventories.size(); i++ )
		{
			if (book_language.equals("EN"))
			{
			//	if ( Inventories.get(i).asin.substring(0, 1).equals("0") || Inventories.get(i).asin.substring(0, 1).equals("1"))
			//	{
				 	db_inven.insert( Inventories.get(i) );
					
				 	//	    db_inven.UpdatepsUpdate_Isbn_byAsin(Inventories.get(i).isbn, Inventories.get(i).asin);	
				 	//    System.out.println("updating isbn: " + Inventories.get(i).asin + "\t" + Inventories.get(i).isbn); 
				    
				    //	System.out.println("setting postage: " + Inventories.get(i).asin + " " +  Inventories.get(i).postage);
				//  db_inven.UpdateINTpostage( Inventories.get(i).asin, Inventories.get(i).postage );
				   
			//	}
			//	else
				{
			//		System.out.println("asin " + Inventories.get(i).asin + " is not english, ignored");
				}
			}
		}
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException
	{
		database_inventory_business  my_dib = new database_inventory_business();
		my_dib.insertAllInventoryFromFile();
	}
	
	public int getMaxId() throws SQLException
	{		
		return db_inven.getMaxId() ;
	}
	
	
	
	
	public List<Item_InventoryExtension> getListFromIdArrange(int startpoint, int endpoint)
	{
		return db_inven.getListFromIdArrange(startpoint, endpoint);
	}
	public List<Item_InventoryExtension2> getListFromIdArrange2(int startpoint, int endpoint)
	{
		return db_inven.getListFromIdArrange2(startpoint, endpoint);
	}
	public Item_InventoryExtension getItemFromAsin (String asin)
	{
		return db_inven.getItemFromAsin ( asin );
	}
	
	public ArrayList<Item_InventoryExtension> getItemssssFromAsin(String asin)
	{
		return db_inven.getItemssssFromAsin ( asin );
	}
	
	
	public ArrayList<Item_InventoryExtension> getItemssssFromIsbn(String isbn)
	{
		return db_inven.getItemssssFromIsbn ( isbn );
	}
	
	public Item_InventoryExtension getItemFromSku (String sku)
	{
		return db_inven.getItemFromSku ( sku );
	}
	
	
	public void UpdatepsUpdate_Isbn_byAsin(String isbn, String asin)
	{
		try {
			db_inven.UpdatepsUpdate_Isbn_byAsin ( isbn, asin );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void change_datatable()
	{
		db_inven.change_datatable();
	}
	
	/*
	public String getIsbnFromAsin(String Asin)
	{	
		return mymap_asinisbn.get(Asin);
	}
	
	public String getIsbnFromSku(String sku)
	{
		String isbn = "";
		isbn = this.mymap_skuisbn.get(sku);
		if ( isbn == null || isbn.length() != 10  || isbn.equals("0000000000") || isbn.equals("") )
		{
			isbn = db_skuisbn.getIsbnFromSku(sku); 
		}		 
	    return isbn;
	}
	*/
	
	
	// function Update_Price will update all of the price related stuffs
	// price_new -> price
	
	public void Update_Price(Item_InventoryExtension2 iie) throws SQLException
	{
		db_inven.update(iie);
	}
	
	public void updatebatch(ArrayList<Item_InventoryExtension> ites) throws SQLException
	{
		db_inven.updatebatch(ites);
	}
	
	public void Update_AmazonPrice_bySku(Item_InventoryExtension iie) throws SQLException
	{
		db_inven.update_Amazonprice_bysku (iie);
	}
	public void Update_CompatitorPrice_bySku(Item_InventoryExtension iie) throws SQLException
	{
		db_inven.update_Compatitorprice_bysku (iie);
	}
	
	public void Update_AmazonPrice_byAsin(Item_InventoryExtension iie) throws SQLException
	{
		db_inven.Update_AmazonPrice_byAsin (iie);
	}
	
	public void Update_US_and_Price(Item_InventoryExtension iie) throws SQLException
	{
		db_inven.updateUSprice_and_Price(iie);
	}
	
	public void Update_JP_and_Price(Item_InventoryExtension iie) throws SQLException
	{
		db_inven.Update_JPCompator_and_Price(iie);
	}
		
	public void Update_BT_and_Price(Item_InventoryExtension iie) throws SQLException
	{
		db_inven.Update_BT_and_Price(iie);
	}
	
	public void setSalesRank(String asin, int u) throws SQLException
	{
		db_inven.setSalesRank(asin, u);
	}
	
	public void setListPrice(Double listprice, String asin) throws SQLException
	{
		db_inven.setListPrice(listprice, asin);
	}
	public void insertPriceHistory(String isbn,Double aprice,Double tnlprice,Double tulprice,Timestamp time,char flag) throws SQLException{
		db_inven.insertPriceHistory(isbn,aprice,tnlprice,tulprice,time,flag);
	}
	public void insertPriceNewest(String isbn,Double aprice,Double tnlprice,Double tulprice,Timestamp time,char flag) throws SQLException{
		db_inven.insertPriceNewest(isbn,aprice,tnlprice,tulprice,time,flag);
	}
	public void updateNewestPrice(String isbn,Double aprice,Double tnlprice,Double tulprice,Timestamp time,char flag) throws SQLException{
		db_inven.updateNewestPrice(isbn,aprice,tnlprice,tulprice,time,flag);
	}
	
	public void insertSummit(String isbn,Double tulprice,Double tulprice2,Double tnlprice,Timestamp time) throws SQLException{
		db_inven.insertSummit(isbn,tulprice,tulprice2,tnlprice,time);
	}
	/*
	public void Update_Quantity (Item_InventoryExtension iie) throws SQLException
	{
		db_inven.Update_QT(iie);
	}
	*/
	public void createReport() throws SQLException
	{
		db_inven.createReport();
	}
	public List<Item_InventoryExtension3> getGoodBooks() throws SQLException
	{
		return db_inven.getGoodBooks();
	}
}