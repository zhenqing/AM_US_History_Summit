
package com.eb.data.amazonobject;

public class AsinProduct{
	
	private String asinString;
	private Condition con;
	private double price;
	private String sku;
	
	public void setAsinString(String as)
	{
		asinString = as;
	}
	public String getAsinString()
	{
		return asinString;
	}
	
	public void setCondition(Condition cd)
	{
		con = cd;
	}
	
	public Condition getCondition()
	{
		return con;
	}
	
	public void setprice(double p)
	{
		this.price = p;
	}
    public double getprice()
    {
    	return this.price;
    }
    
    public void setsku(String sk)
    {
    	this.sku  = sk;
    }
    public String getsku()
    {
    	return this.sku;
    }

}