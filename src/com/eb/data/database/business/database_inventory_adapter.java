package com.eb.data.database.business;

import java.sql.SQLException;
import java.util.ArrayList;

import com.eb.data.database.CRUD.database_inventory;
import com.eb.data.dataobject.Item_InventoryExtension;

public class database_inventory_adapter {
	
	private database_inventory db_inven;
	
	private ArrayList<Item_InventoryExtension> Inventories = new ArrayList<Item_InventoryExtension>();
	
	public database_inventory_adapter() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		db_inven = new database_inventory();
		SellectAll();
	}
	
	public void SellectAll()
	{		
		Inventories = db_inven.SellectAll();
	}
	
	public ArrayList<Item_InventoryExtension> getItemssssFromAsin(String asin)
	{
		ArrayList<Item_InventoryExtension> Inventories_asin =  new ArrayList<Item_InventoryExtension>();
		
		for ( int i = 0; i < Inventories.size(); i++ )
		{
			String asinmy = Inventories.get(i).asin;
			if (asinmy.equals(asin))
			{
				Inventories_asin.add(Inventories.get(i));
			}
		}
		
		return Inventories_asin;
	}
	
}
