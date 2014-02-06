package com.eb.data.database.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eb.util.File.ReadFileCatalog;

import util.util;

import database.skuisbn;

public class Isbn{
	
	// files

	private String  relatedwordsinfile;
		
	// buffers
			
	// this is the file reader buffer to read the realtedwords
	private BufferedReader relatedBuffer;
		
	// for asin -- isbn quanku
	private Map<String, String> mymap_asinisbn = new HashMap<String, String>();
	
	// for zizaodian sku -- isbn
	private Map<String, String> mymap_skuisbn = new HashMap<String, String>();
	
	private List<String> relalist = new ArrayList<String>();
	private int listsize = 0;
	
	private skuisbn db_skuisbn;

	private void inirelatedbuffer(int i) throws FileNotFoundException
	{   
	//	System.out.println("i:" + i );
		relatedwordsinfile = relalist.get(i);	
		System.out.println("file name: " + relatedwordsinfile );
        File related = new File(relatedwordsinfile);
        FileReader frelated = new FileReader(related);        
        relatedBuffer = new BufferedReader(frelated); 
	}
	
	private void getQuan() throws IOException, SQLException
	{
		ReadFileCatalog.ChangeDir("Quanku");
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
 
            	String strs[] = util.getTokens(linewords);
            	//	String strs[]=linewords.split(",");   
            	
            	if ( strs[0].length() == 0  )
            	{ 
            		continue;
            	}
          	          	
            	if ( strs[0] == null )
            	{
            		continue;
            	}
            	
                mymap_asinisbn.put(strs[0], strs[1]);
                
            }
            fileindex++;
          
         }        
        // read io close
        this.relatedBuffer.close();       

        System.out.println("read quanku finish");
        
	}
	
	private void getzizaodian() throws IOException, SQLException
	{
		ReadFileCatalog.ChangeDir("Zizaodian");
		relalist.clear();
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
            	
              	if (linewords == null)
            	{
            		continue;
            	} 
            	if ( linewords.contains("sku") )
            	{
            		continue;
            	}
            	if (linewords.equals(""))
            	{
            		continue;
            	}
 
            	String strs[] = util.getTokens(linewords);
            //	String strs[]=linewords.split(",");   
            	
            	
            	if (strs == null)
            	{
            		continue;
            	}
            	
            	if (strs.length < 2)
            	{
            		continue;
            	}
            	
            	if ( strs[0] == null )
            	{
            		continue;
            	}
            	
            	if (strs[0].length() == 0  )
            	{ 
            		continue;
            	}
          	               	
            	mymap_skuisbn.put(strs[0], strs[1]);
                
            }
            fileindex++;
          
         }        
        // read io close
        this.relatedBuffer.close();       

        System.out.println("read zizaodian finish");
        
	} 
		
	public Isbn() throws IOException, SQLException{
		getQuan();
		getzizaodian();
		try {
			db_skuisbn = new skuisbn();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	

}