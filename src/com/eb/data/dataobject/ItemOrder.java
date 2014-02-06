package com.eb.data.dataobject;
public class ItemOrder{
	
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
	
	public String toString()
	{
		String output = order_id + "," + price + "," + purchase_date + ","
				+ quantity_purchased + "," + recipient_name + "," + ship_address_1 + ","
				+ ship_address_2 + "," + ship_city + "," + shipping_fee + ","
				+ ship_state + "," + ship_zip + "," + sku + ","
				+ isbn + "," + asin + "," + SELLER + ","
				+ SELLER_PRICE + "," + "" + "," + COST + ","
				+ ORDER_NUMBER + "," + ACCOUNT + "," + PROFIT+ ","
				+ reference + ",";
		
		return output;
	}
}