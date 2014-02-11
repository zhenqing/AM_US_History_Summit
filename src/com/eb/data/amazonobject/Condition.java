
package com.eb.data.amazonobject;

public class Condition{
	
	public String condition;
	public String subcondition;
	
	public Condition()
	{
		
	}
	
	public Condition(String condition, String subcondition)
	{
		this.condition = condition;
		this.subcondition = subcondition;
	}
	
	public void setcondition(String con)
	{
		condition = con;
	}
	public String getcondition()
	{
		return condition;
	}
	public void setsubcondition(String scon)
	{
		subcondition = scon;
	}
	public String getsubcondition()
	{
		return this.subcondition;
	}
	
    public int getConditionValue()
	{
		int conditionvalue = 100;
		
		if (condition.equalsIgnoreCase("Collectible"))
		{
		//	conditionvalue = 5;
		}		
		else if (condition.equalsIgnoreCase("new"))
		{
			conditionvalue = 10;
		}
		else if (condition.equalsIgnoreCase("Refurbished"))
		{
			conditionvalue = 20;
		}
		
		else if (condition.equalsIgnoreCase("Used"))
		{
			conditionvalue = 30;
			if (subcondition.equalsIgnoreCase("Like New") || subcondition.equalsIgnoreCase("Min"))
			{
				conditionvalue = 31;
			}
			else if (subcondition.equalsIgnoreCase("Very Good") )
			{
				conditionvalue = 32;
			}
			else if (subcondition.equalsIgnoreCase("Good") )
			{
				conditionvalue = 33;
			}
			else if (subcondition.equalsIgnoreCase("Acceptable") )
			{
				conditionvalue = 34;
			}
		}
		return conditionvalue;
	}
}