package com.eb.data.dataobject;

public class ItemAsin{
	
	public String sku;
	public int quantity;
	public double price;
	public String asin;
	
	public String toString()
	{
		String output = sku + "," + quantity + "," + price + ","
				+ asin + "," ;
		
		return output;
	}
}