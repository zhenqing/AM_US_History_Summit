package com.eb.data.dataobject;

public class ItemIsbn{
	
	public String sku;
	public String isbn;
	public double checktime;
	public String asin;
	
	public String toString()
	{
		String output = sku + "," + isbn + "," + checktime + ","
				+ asin + "," ;
		
		return output;
	}
}