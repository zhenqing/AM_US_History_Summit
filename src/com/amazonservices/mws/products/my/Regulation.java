
package com.amazonservices.mws.products.my;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.eb.data.amazonobject.Condition;
import com.eb.data.database.business.database_inventory_business;

public class Regulation{
	private static database_inventory_business my_db ;
	
    public static double calculateLowestPrice(List<LowestOffer> cs, Condition con)
    {
    	double lowestprice = Double.MAX_VALUE;
    	double lowestprice_second =  Double.MAX_VALUE;
    	// at least 1 buckets
    	if (cs.size() < 1)
    	{
    		return lowestprice;
    	}
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    		if (  ( cs.get(i).getSellerFeedbackCount() <= 100 ) )
    		{
    			continue;
    		}
    		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
    		{
    		//	continue;
    		}
    		
    		String rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
    		
    		if (!Util.isNumeric(rate))
    		{
    			continue;
    		}
    		
    		if (Integer.parseInt(rate) < 90)
    		{
    			continue;
    		}
    		
    		Condition thiscon = new Condition();
    		thiscon.setcondition( cs.get(i).getItemCondition() );
    		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
    		
    		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
    		if ( thiscon.getConditionValue() > con.getConditionValue()  )
    		{
    			continue;
    		}
    		
    		if ( cs.get(i).getListingPrice().doubleValue() < lowestprice_second )
    		{
    			lowestprice_second = cs.get(i).getListingPrice().doubleValue();
    			
    			if ( lowestprice_second < lowestprice)
    			{
    				double temp = lowestprice;
    				lowestprice = lowestprice_second;
    				lowestprice_second = temp;
    			}
    				
    			if (lowestprice == 0)
    			{
    				lowestprice = 0;
    			}
    			
    		}
    	}
    	if (lowestprice == Double.MAX_VALUE)
    	{
    		lowestprice = Double.MAX_VALUE;
    	}
    	return lowestprice_second;
    	
    }
    
    public static double calculate_SecondLowestPrice(List<LowestOffer> cs, Condition con)
    {
    	double lowestprice[] = new double[3];
    	
    	lowestprice[0] = Double.MAX_VALUE;
    	lowestprice[1] = Double.MAX_VALUE;
    	lowestprice[2] = Double.MAX_VALUE;
    	
    	/*
    	String lowestprice_str[] = new String[5];
    	for (int i = 0; i < 4; i++)
    	{
    		lowestprice_str[i] = String.valueOf(lowestprice[i]);
    	}
    	lowestprice_str[4] = "";
    	*/
    	  	   	
    	// at least 1 buckets
    	if (cs.size() < 2)
    	{
    	//	return lowestprice_str;
    		return lowestprice[0];
    	}
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    		if (cs.get(i).getListingPrice()==null)
			{
    			continue;
			}
    		if (  ( cs.get(i).getSellerFeedbackCount() <= 100 ) )
    		{
    			continue;
    		}
    		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
    		{
    		//	continue;
    		}
    		  		
    		String rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
    		
    		if (!Util.isNumeric(rate))
    		{
    			continue;
    		}
    		
    		if (Integer.parseInt(rate) < 90)
    		{
    			continue;
    		}
    		
			String shipday = cs.get(i).getAPshipday();
			if ( !shipday.contains("0-2") )
			{
				continue;
			}
			
    		// omit EliteDigital UK
    		if ( Integer.parseInt(rate) < 96 && cs.get(i).getSellerFeedbackCount() >= 213454 && cs.get(i).getSellerFeedbackCount() <= 233454 )
    		{
    		//	continue;
    		}
    		
    		Condition thiscon = new Condition();
    		thiscon.setcondition( cs.get(i).getItemCondition() );
    		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
    		
    		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
    		if ( thiscon.getConditionValue() > con.getConditionValue()  )
    		{
    			continue;
    		}
    		
    		if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[1] )
    		{
    				
    			if (lowestprice[0] > cs.get(i).getListingPrice().doubleValue())
    			{
    				lowestprice[1] = lowestprice[0];
    				lowestprice[0] = cs.get(i).getListingPrice().doubleValue();				
    			}
    			else
    			{
    				lowestprice[1] = cs.get(i).getListingPrice().doubleValue();
    			}
    			
    		} 
    		
    	}
	
    	return lowestprice[1];
    }
  
    public static double calculateAP_or_SecondLowestPrice(List<LowestOffer> cs, Condition con)
    {
    	double lowestprice[] = new double[4];
    	
    	lowestprice[0] = Double.MAX_VALUE;
    	lowestprice[1] = Double.MAX_VALUE;
    	lowestprice[2] = 0;
    	lowestprice[3] = 0;
    	
    	/*
    	String lowestprice_str[] = new String[5];
    	for (int i = 0; i < 4; i++)
    	{
    		lowestprice_str[i] = String.valueOf(lowestprice[i]);
    	}
    	lowestprice_str[4] = "";
    	*/
    	  	   	
    	// at least 1 buckets
    	if (cs.size() < 1)
    	{
    	//	return lowestprice_str;
    		return lowestprice[0];
    	}
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    		if (cs.get(i).getListingPrice()==null)
			{
    			continue;
			}
    		if (  ( cs.get(i).getSellerFeedbackCount() <= 100 ) )
    		{
    			continue;
    		}
    		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
    		{
    		//	continue;
    		}
    		

    		
    		String rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
    		
    		if (!Util.isNumeric(rate))
    		{
    			continue;
    		}
    		
    		if (Integer.parseInt(rate) < 90)
    		{
    			continue;
    		}
    		
			String shipday = cs.get(i).getAPshipday();
			if ( !shipday.contains("0-2") )
			{
				continue;
			}
			
    		// omit EliteDigital UK
    		if ( Integer.parseInt(rate) < 96 && cs.get(i).getSellerFeedbackCount() >= 213454 && cs.get(i).getSellerFeedbackCount() <= 233454 )
    		{
    			continue;
    		}
    		
    		Condition thiscon = new Condition();
    		thiscon.setcondition( cs.get(i).getItemCondition() );
    		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
    		
    		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
    		if ( thiscon.getConditionValue() > con.getConditionValue()  )
    		{
    			continue;
    		}
    		
    		// if AP, choose AP
    		if ( cs.get(i).getChannel().equalsIgnoreCase("Amazon"))  
    		{
    			if (lowestprice[0] >  cs.get(i).getListingPrice().doubleValue())
    			{
    				lowestprice[0] = cs.get(i).getListingPrice().doubleValue();
    			}
    		}
    		else if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[1] )
    		{
    			
    			if (lowestprice[0] > lowestprice[1])
    			{
    				lowestprice[0] = lowestprice[1];
    			}
    			lowestprice[1] = cs.get(i).getListingPrice().doubleValue();  

    		} 
    		
    	}
	
    	return lowestprice[0];
    }
    public static boolean checkSummit(List<LowestOffer> cs,String asinString) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException
    {
    	boolean isSummit = false;
    	double lowestprice[] = new double[4];
    	my_db =  new database_inventory_business();
    	lowestprice[0] = Double.MAX_VALUE;
    	lowestprice[1] = Double.MAX_VALUE;
    	lowestprice[2] = Double.MAX_VALUE;
    	lowestprice[3] = 0;
    	
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    	
    		 Condition myusedcon = new Condition();
    		myusedcon.condition = "used";
    		myusedcon.subcondition = "good";
    		
    		if(cs.get(i).getItemCondition().equalsIgnoreCase("used")){
	    		if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[1] )
	    		{
	    				
	    			if (lowestprice[0] > cs.get(i).getListingPrice().doubleValue())
	    			{
	    				lowestprice[1] = lowestprice[0];
	    				lowestprice[0] = cs.get(i).getListingPrice().doubleValue();				
	    			}
	    			else
	    			{
	    				lowestprice[1] = cs.get(i).getListingPrice().doubleValue();
	    			}
	    			
	    		} 
    		}else{
    			if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[2] )
	    		{
	    				
    				if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[2] )
	        		{
	        			lowestprice[2] = cs.get(i).getListingPrice().doubleValue();    
	        		} 
	    			
	    		} 
    		}
    		
    		
    	}
    	String info = "isbn:"+asinString+"\tused lowest:"+lowestprice[0] +"\tused second lowest:"+lowestprice[1] +"\tnew lowest:"+lowestprice[2] 
    					+"\tprofit:"+(lowestprice[1]*0.85-lowestprice[0]-4.35)+"\tlower than new:"+(lowestprice[2]-lowestprice[1]);
    	System.out.println(info);
    	if((lowestprice[1]*0.85-lowestprice[0]>14.35)&&(lowestprice[2]-lowestprice[1]>4)){
    		System.out.println("!!!");
    		sendMail(info,"zhaozhenqing@gmail.com","Summit isbn:"+asinString);
    		sendMail(info,"barberryibport@gmail.com","Summit isbn:"+asinString);
    		long time = System.currentTimeMillis();
    		Timestamp timestamp = new Timestamp(time);
    		my_db.insertSummit(asinString,lowestprice[0], lowestprice[1], lowestprice[2], timestamp);
    		isSummit = true;
    	}
    	return isSummit;
    }
    
    public static void sendMail(String content,String toAddress,String subject){
    	final String username = "ebzhenqing@gmail.com";
		final String password = "zhen2410144";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ebzhenqing@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toAddress));
			message.setSubject(subject);
			message.setText(content);
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }

    public static double[] calculateAP_and_SecondLowestPrice(List<LowestOffer> cs, Condition con)
    {
    	double lowestprice[] = new double[4];
    	
    	lowestprice[0] = Double.MAX_VALUE;
    	lowestprice[1] = Double.MAX_VALUE;
    	lowestprice[2] = Double.MAX_VALUE;
    	lowestprice[3] = 0;
    	
    	/*
    	String lowestprice_str[] = new String[5];
    	for (int i = 0; i < 4; i++)
    	{
    		lowestprice_str[i] = String.valueOf(lowestprice[i]);
    	}
    	lowestprice_str[4] = "";
    	*/
    	  	   	
    	// at least 2 buckets
    	if (cs.size() <1)
    	{
    	//	return lowestprice_str;
    		return lowestprice;
    	}
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    		if (cs.get(i).getListingPrice()==null)
			{
    			continue;
			}
    		if (  ( cs.get(i).getSellerFeedbackCount() <= 100 ) )
    		{
    			continue;
    		}
    		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
    		{
    		//	continue;
    		}
   		
    		String rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
    		
    		if (!Util.isNumeric(rate))
    		{
    			continue;
    		}
    		
    		if (Integer.parseInt(rate) < 90)
    		{
    			continue;
    		}
			String shipday = cs.get(i).getAPshipday();
		    if ( !shipday.contains("0-2") )
		    {
		    	continue;
		    }
    		
    		// omit EliteDigital UK
    		if ( Integer.parseInt(rate) < 96 && cs.get(i).getSellerFeedbackCount() >= 213454 && cs.get(i).getSellerFeedbackCount() <= 233454 )
    		{
    		//	continue;
    		}
    		
    		Condition thiscon = new Condition();
    		thiscon.setcondition( cs.get(i).getItemCondition() );
    		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
    		
    		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
    		if ( thiscon.getConditionValue() > con.getConditionValue()  )
    		{
    			continue;
    		}
    		
    		// if AP, choose AP
    		if ( cs.get(i).getChannel().equalsIgnoreCase("Amazon"))  
    		{
    			
    			// lowestprice_str[4] = cs.get(i).getAPshipday();
    			// String shipday = cs.get(i).getAPshipday();
    			// if ( shipday.contains("0-2") )
				{
    				lowestprice[0] = cs.get(i).getListingPrice().doubleValue();
				}
    		}
    		
    		if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[1] )
    		{
    			lowestprice[2] = lowestprice[1];
    			lowestprice[1] = cs.get(i).getListingPrice().doubleValue();    
    		}
    		else if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[2] )
    		{
    			lowestprice[2] = cs.get(i).getListingPrice().doubleValue();    
    		}   		
    		
    	}
    	
    	/*
    	for (int i = 0; i < 4; i++)
    	{
    		lowestprice_str[i] = String.valueOf(lowestprice[i]);
    	}
    	*/
    	
    	//return lowestprice_str; 	
    	return lowestprice;
    }
       
    public static double[] calculate_LowestPrice_allcondition(List<LowestOffer> cs, Condition con)
    {
    	double lowestprice[] = new double[3];
    	lowestprice[0] = Double.MAX_VALUE;
    	lowestprice[1] = Double.MAX_VALUE;
    	lowestprice[2] = Double.MAX_VALUE;
    	  	   	
    	Condition cons[] = new Condition[3];  	
    	cons[0] = new Condition("new","new");
    	cons[1] = new Condition("used","good");
    	cons[2] = new Condition("used","acceptable");
    	  	
    	// at least 1 buckets
    	if ( cs.size() < 2 )
    	{
    	//	return lowestprice_str;
    	//	return lowestprice;
    	}
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    		if ( cs.get(i).getListingPrice() == null )
			{
    			continue;
			}
    		if (  ( cs.get(i).getSellerFeedbackCount() < 100 ) )
    		{
    		//	continue;
    		}
    		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
    		{
    		//	continue;
    		}
    		String rate = cs.get(i).getSellerPositiveFeedbackRating();
    		if ( !rate.equals("95-97%" ) )
    		{
    		//	continue;
    		}
    		
    		String shipday = cs.get(i).getAPshipday();
			if ( !shipday.contains("0-2") )
			{
				continue;
			}
    		
    		rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
    		
    		if (!Util.isNumeric(rate))
    		{
    			continue;
    		}
    		
    		if (Integer.parseInt(rate) < 90)
    		{
    		//	continue;
    		}
    		
    		// omit EliteDigital UK
    		if ( Integer.parseInt(rate) < 96 && cs.get(i).getSellerFeedbackCount() >= 213454 && cs.get(i).getSellerFeedbackCount() <= 233454 )
    		{
    		//	continue;
    		}
    		
    		Condition thiscon = new Condition();
    		thiscon.setcondition( cs.get(i).getItemCondition() );
    		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
    		
    		if ( cs.get(i).getChannel().equalsIgnoreCase("Amazon"))  
    		{ 
    		//	continue;
    		}
    		
    		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
    		for (int k = 0; k < 3; k ++ )
    		{
	    		if ( thiscon.getConditionValue() == cons[k].getConditionValue()  )
	    		{
	        		if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[k] )
	        		{
	        			lowestprice[k] = cs.get(i).getListingPrice().doubleValue();    
	        		} 
	    		}
    		}
      	
    	}
        
    	//return lowestprice_str; 	
    	return lowestprice;
    }
    
	// choose the smallest between AP & second lowest price 
    public static double[] calculateFBA_OR_SecondLowestPrice_allcondition(List<LowestOffer> cs, Condition con)
    {
    	double lowestprice[] = new double[3];
    	
    	lowestprice[0] = Double.MAX_VALUE;
    	lowestprice[1] = Double.MAX_VALUE;
    	lowestprice[2] = Double.MAX_VALUE;
    	
    	double secondlowestprice[] = new double[3];
    	   	
    	secondlowestprice[0] = Double.MAX_VALUE;
    	secondlowestprice[1] = Double.MAX_VALUE;
    	secondlowestprice[2] = Double.MAX_VALUE;
    	  	   	
    	Condition cons[] = new Condition[3];  	
    	cons[0] = new Condition("new","new");
    	cons[1] = new Condition("used","good");
    	cons[2] = new Condition("used","acceptable");
    	  	
    	// at least 1 buckets
    	if ( cs.size() < 1 )
    	{
    	//	return lowestprice_str;
    		return lowestprice;
    	}
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    		if ( cs.get(i).getListingPrice() == null )
			{
    			continue;
			}
    		int count = cs.get(i).getSellerFeedbackCount();
    		if (  ( count <= 100 ) )
    		{
    			continue;
    		}
    		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
    		{
    		//	continue;
    		}
    		String rate = cs.get(i).getSellerPositiveFeedbackRating();
    		if ( !rate.equals("95-97%" ) )
    		{
    		//	continue;
    		}
    		
    		String shipday = cs.get(i).getAPshipday();
			if ( !shipday.contains("0-2") )
			{
				continue;
			}
    		
    		rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
    		
    		if (!Util.isNumeric(rate))
    		{
    			continue;
    		}
    		
    		if (Integer.parseInt(rate) < 90)
    		{
    			continue;
    		}
    		
    		// omit EliteDigital UK
    		if ( Integer.parseInt(rate) < 96 && cs.get(i).getSellerFeedbackCount() >= 213454 && cs.get(i).getSellerFeedbackCount() <= 233454 )
    		{
    		//	continue;
    		}
    		
    		Condition thiscon = new Condition();
    		thiscon.setcondition( cs.get(i).getItemCondition() );
    		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
    		
       		if ( cs.get(i).getChannel().equalsIgnoreCase("Amazon"))  
    		{       			
    			// lowestprice_str[4] = cs.get(i).getAPshipday();

				if (cs.get(i).getListingPrice().doubleValue() < lowestprice[0])
				{
					lowestprice[0] = cs.get(i).getListingPrice().doubleValue();
				}
				if (cs.get(i).getListingPrice().doubleValue() < lowestprice[1])
				{
					lowestprice[1] = cs.get(i).getListingPrice().doubleValue();
				}
				if (cs.get(i).getListingPrice().doubleValue() < lowestprice[2])
				{
					lowestprice[2] = cs.get(i).getListingPrice().doubleValue();
				}
				if (cs.get(i).getListingPrice().doubleValue() <  secondlowestprice[0] )
				{
					secondlowestprice[0] = cs.get(i).getListingPrice().doubleValue();
				}
				if (cs.get(i).getListingPrice().doubleValue() <  secondlowestprice[1])
				{
					secondlowestprice[1] = cs.get(i).getListingPrice().doubleValue();
				}
				if (cs.get(i).getListingPrice().doubleValue() <  secondlowestprice[2])
				{
					secondlowestprice[2] = cs.get(i).getListingPrice().doubleValue();
				}
	
    		}
       		
    		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
    		for ( int k = 0; k < 3; k ++ )
    		{
    			double landprice = Double.MAX_VALUE;
    			
    			if ( cs.get(i).getListingPrice() != null )
    			{
        			landprice = cs.get(i).getListingPrice().doubleValue();
    			}
        		
        		if ( thiscon.getConditionValue() <= cons[k].getConditionValue()  )
	    		{
	        		if ( cs.get(i).getListingPrice() != null && landprice < lowestprice[k] )
	        		{
	        			secondlowestprice[k] = lowestprice[k];
	        			lowestprice[k] = cs.get(i).getListingPrice().doubleValue();    
	        		} 
	        		else if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < secondlowestprice[k] )
	        		{
	        			secondlowestprice[k] = cs.get(i).getListingPrice().doubleValue(); 
	        		} 
	    		}
    		}
      	
    	}
        
    	//return lowestprice_str; 	
    	return secondlowestprice;
    }
    
	// choose the smallest between AP & second lowest price 
    public static double[] calculateSecondLowestPrice_allcondition(List<LowestOffer> cs, Condition con)
    {
    	double lowestprice[] = new double[3];
    	
    	lowestprice[0] = Double.MAX_VALUE;
    	lowestprice[1] = Double.MAX_VALUE;
    	lowestprice[2] = Double.MAX_VALUE;
    	
    	double secondlowestprice[] = new double[3];
    	   	
    	secondlowestprice[0] = Double.MAX_VALUE;
    	secondlowestprice[1] = Double.MAX_VALUE;
    	secondlowestprice[2] = Double.MAX_VALUE;
    	  	   	
    	Condition cons[] = new Condition[3];  	
    	cons[0] = new Condition("new","new");
    	cons[1] = new Condition("used","good");
    	cons[2] = new Condition("used","acceptable");
    	  	
    	// at least 1 buckets
    	if ( cs.size() < 1 )
    	{
    	//	return lowestprice_str;
    		return lowestprice;
    	}
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    		if ( cs.get(i).getListingPrice() == null )
			{
    			continue;
			}
    		int count = cs.get(i).getSellerFeedbackCount();
    		if (  ( count <= 100 ) )
    		{
    			continue;
    		}
    		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
    		{
    		//	continue;
    		}
    		String rate = cs.get(i).getSellerPositiveFeedbackRating();
    		if ( !rate.equals("95-97%" ) )
    		{
    		//	continue;
    		}
    		
    		String shipday = cs.get(i).getAPshipday();
			if ( !shipday.contains("0-2") )
			{
				continue;
			}
    		
    		rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
    		
    		if (!Util.isNumeric(rate))
    		{
    			continue;
    		}
    		
    		if ( Integer.parseInt(rate) < 90 )
    		{
    			continue;
    		}
    		
    		// omit EliteDigital UK
    		if ( Integer.parseInt(rate) < 96 && cs.get(i).getSellerFeedbackCount() >= 213454 && cs.get(i).getSellerFeedbackCount() <= 233454 )
    		{
    		//	continue;
    		}
    		
    		Condition thiscon = new Condition();
    		thiscon.setcondition( cs.get(i).getItemCondition() );
    		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
       		
    		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
    		for ( int k = 0; k < 3; k ++ )
    		{
    			double landprice = Double.MAX_VALUE;
    			
    			if ( cs.get(i).getListingPrice() != null )
    			{
        			landprice = cs.get(i).getListingPrice().doubleValue();
    			}
        		
        		if ( thiscon.getConditionValue() == cons[k].getConditionValue()  )
	    		{
	        		if ( cs.get(i).getListingPrice() != null && landprice < lowestprice[k] )
	        		{
	        			secondlowestprice[k] = lowestprice[k];
	        			lowestprice[k] = cs.get(i).getListingPrice().doubleValue();    
	        		} 
	        		else if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < secondlowestprice[k] )
	        		{
	        			secondlowestprice[k] = cs.get(i).getListingPrice().doubleValue(); 
	        		} 
	    		}
    		}
      	
    	}
        
    	//return lowestprice_str; 	
    	return secondlowestprice;
    }
    
    public static double calculate_LowestUsedPrice(List<LowestOffer> cs)
    {
    	double lowestprice=1000;
    	// at least 1 buckets
    	if ( cs.size() < 2 )
    	{
    	//	return lowestprice_str;
    	//	return lowestprice;
    	}
    	
    	for ( int i = 0; i < cs.size(); i++ )
    	{
    		//System.out.println(cs.get(i).getListingPrice());
    		if ( cs.get(i).getListingPrice() == null )
			{
    			continue;
			}
    		if (  ( cs.get(i).getSellerFeedbackCount() < 100 ) )
    		{
    		//	continue;
    		}
    		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
    		{
    		//	continue;
    		}
    		String rate = cs.get(i).getSellerPositiveFeedbackRating();
    		if ( !rate.equals("95-97%" ) )
    		{
    		//	continue;
    		}
    		
    		String shipday = cs.get(i).getAPshipday();
			if ( !shipday.contains("0-2") )
			{
				//continue;
			}
    		
    		rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
    		
    		if (!Util.isNumeric(rate))
    		{
    			continue;
    		}
    		
    		if (Integer.parseInt(rate) < 90)
    		{
    		//	continue;
    		}
    		double feedback = cs.get(i).getSellerFeedbackCount();
    		// omit EliteDigital UK
    		if ( Integer.parseInt(rate) < 96 && cs.get(i).getSellerFeedbackCount() >= 213454 && cs.get(i).getSellerFeedbackCount() <= 267086 )
    		{
    		//	continue;
    		}
    		
//    		Condition thiscon = new Condition();
//    		thiscon.setcondition( cs.get(i).getItemCondition() );
//    		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
//    		
    	
    		
    		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
//    		for (int k = 0; k < 2; k ++ )
//    		{
//	    		if ( thiscon.getConditionValue() == cons[k].getConditionValue()  )
//	    		{
//	        		if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[k] )
//	        		{
//	        			lowestprice[k] = cs.get(i).getListingPrice().doubleValue();    
//	        		} 
//	    		}
//    		}
    		if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice )
    		{
    			lowestprice = cs.get(i).getListingPrice().doubleValue();    
    		} 
    		
      	
    	}
        
    	//return lowestprice_str; 	
    	return lowestprice;
    }


public static double[] calculate_LowestNewPrice_AP(List<LowestOffer> cs)
{
	double lowestprice[] = new double[2];
	lowestprice[0] =1000;
	lowestprice[1] = 1000;

	  	
	// at least 1 buckets
	if ( cs.size() < 2 )
	{
	//	return lowestprice_str;
	//	return lowestprice;
	}
	
	for ( int i = 0; i < cs.size(); i++ )
	{
		System.out.print(cs.get(i).getListingPrice());
		System.out.println("feedback"+cs.get(i).getSellerFeedbackCount()+"channel"+cs.get(i).getChannel());
		if ( cs.get(i).getListingPrice() == null )
		{
			continue;
		}
		if (  ( cs.get(i).getSellerFeedbackCount() < 100 ) )
		{
		//	continue;
		}
		if ( ! ( cs.get(i).getCurrencyCode() !=  "USD" ) )
		{
		//	continue;
		}
		String rate = cs.get(i).getSellerPositiveFeedbackRating();
		if ( !rate.equals("95-97%" ) )
		{
		//	continue;
		}
		
		String shipday = cs.get(i).getAPshipday();
		if ( !shipday.contains("0-2") )
		{
			//continue;
		}
		
		rate = cs.get(i).getSellerPositiveFeedbackRating().substring(0, 2);
		
		if (!Util.isNumeric(rate))
		{
			continue;
		}
		
		if (Integer.parseInt(rate) < 90)
		{
		//	continue;
		}
		double feedback = cs.get(i).getSellerFeedbackCount();
		// omit EliteDigital UK
		if ( Integer.parseInt(rate) < 96 && cs.get(i).getSellerFeedbackCount() >= 213454 && cs.get(i).getSellerFeedbackCount() <= 267086 )
		{
		//	continue;
		}
		
//		Condition thiscon = new Condition();
//		thiscon.setcondition( cs.get(i).getItemCondition() );
//		thiscon.setsubcondition( cs.get(i).getItemSubcondition() );
//		
	
		
		// if ( ( thiscon.getConditionValue() > con.getConditionValue() ) || ( thiscon.getcondition().equalsIgnoreCase("new") ) )
//		for (int k = 0; k < 2; k ++ )
//		{
//    		if ( thiscon.getConditionValue() == cons[k].getConditionValue()  )
//    		{
//        		if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[k] )
//        		{
//        			lowestprice[k] = cs.get(i).getListingPrice().doubleValue();    
//        		} 
//    		}
//		}
		if ( cs.get(i).getListingPrice() != null && cs.get(i).getListingPrice().doubleValue() < lowestprice[0] )
		{
			lowestprice[0] = cs.get(i).getListingPrice().doubleValue();    
		} 
		
		if ( cs.get(i).getChannel().equalsIgnoreCase("Amazon") && cs.get(i).getListingPrice().doubleValue() < lowestprice[1] )
    		{
				{
    				lowestprice[1] = cs.get(i).getListingPrice().doubleValue();
				}
    	}
		
	}
    
	//return lowestprice_str; 	
	return lowestprice;
}
}