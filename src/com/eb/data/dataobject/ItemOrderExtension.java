package com.eb.data.dataobject;

public class ItemOrderExtension extends ItemOrder{
	
/*	
	public String order_id;
	public String price;
	public String purchase_date;
	public String quantity_purchased;
	public String recipient_name;
	public String ship_address_1;
	public String ship_address_2;
	public String ship_city;
	public String shipping_fee;
	public String ship_state;
	public String ship_zip;
	public String sku;
	public String isbn;
//	public int purchasetimes;
	public String asin;
	public String SELLER;
	public String SELLER_PRICE;
	public String CODE;
	public String COST;
	public String ORDER_NUMBER;
	public String ACCOUNT;
	public String PROFIT;
	public String reference;
*/	
	public String order_history;
	public String check_time;
	public String sku_address;
	public String asin_address;
	public String isbn_address;
	public String checkedOrNot;
	
	public String toString()
	{
		String seller_out, seller_price_out, code_out, cost_out, order_number_out, account_out, profit_out, reference_out ;
		if (SELLER != null)
		{
			seller_out = SELLER;
		}
		else
		{
			seller_out = "";
		}
		
		if ( SELLER_PRICE != null )
		{
			seller_price_out = SELLER_PRICE;
		}
		else
		{
			seller_price_out = "";
		}
		
		if ( CODE != null )
		{
			code_out = CODE;
		}
		else
		{
			code_out = "";
		}
		
		if ( COST != null )
		{
			cost_out = COST;
		}
		else
		{
			cost_out = "";
		}
		
		if ( ORDER_NUMBER != null )
		{
			order_number_out = ORDER_NUMBER;
		}
		else
		{
			order_number_out = "";
		}
		
		if ( ACCOUNT != null )
		{
			account_out = ACCOUNT;
		}
		else
		{
			account_out = "";
		}
		
		if ( PROFIT != null )
		{
			profit_out = PROFIT;
		}
		else
		{
			profit_out = "";
		}
		
		if ( reference != null )
		{
			reference_out = reference;
		}
		else
		{
			reference_out = "";
		}
		
		if (isbn == null)
		{
			isbn = "";
		}
		if (asin == null)
		{
			asin = "";
		}
		if (check_time == null)
		{
			check_time = "";
		}
		if (sku_address == null)
		{
			sku_address = "";
		}
		if (asin_address == null)
		{
			asin_address = "";
		}
		if (isbn_address == null)
		{
			isbn_address = "";
		}
		/*
		if (isbn.length() > 1)
		{
			String firstisbn = isbn.substring(0, 1);
			if ( firstisbn.equals("0"))
	        {
		//		isbn = "-" + isbn;
	        }
		}
		*/
		
		String output = order_id + "," + price + "," + purchase_date + ","
				+ quantity_purchased + "," + recipient_name + "," + ship_address_1 + ","
				+ ship_address_2 + "," + ship_city + "," + shipping_fee + ","
				+ ship_state + "," + ship_zip + "," + sku_address + "," + sku + ","
				+ isbn + "," + seller_out + ","
				+ seller_price_out + "," + reference_out + "," + " " + "," + cost_out + ","
				+ order_number_out + "," + account_out + "," + profit_out +  "," +  asin + ","  
				 + asin_address + "," + isbn_address + ","  +  code_out  ;
		
		return output;
	}
}