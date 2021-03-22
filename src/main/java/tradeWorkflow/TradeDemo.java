package tradeWorkflow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TradeDemo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		TradeImpl tf = new TradeImpl();
		Date todaysDate = Calendar.getInstance ().getTime();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		
		
		
//		Adding Trade T1
		Date maturityDate=sd.parse("10/05/2021");
		TradeBean t1=new TradeBean("T1",1,"CP-1","B1",maturityDate, todaysDate, 'N');
		tf.addTrade(t1);
		
		//Adding Trade T2
		maturityDate=sd.parse("20/05/2021");
		TradeBean t2=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
		tf.addTrade(t2);		

		TradeBean t4=new TradeBean("T3",5,"CP-4","B1",maturityDate, todaysDate, 'N');
		tf.addTrade(t4);
		
		
		//Adding Trade T3
		maturityDate=sd.parse("20/05/2021");
		TradeBean t3=new TradeBean("T4",5,"CP-3","B2",maturityDate, todaysDate, 'N');
		tf.addTrade(t3);
		
		
		
		//Display all Trade
		System.out.println("\n\n");
		System.out.println("Displaying total number of Trade in the list");
		tf.printTrade();
		System.out.println("\n\n");	
				
		//Checking for all Expired Flag
		System.out.println("Checking for Expired Flag");
		maturityDate=sd.parse("20/01/2021");
		TradeBean t6=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
		tf.tradeList.replace("T2", t6);
		
		maturityDate=sd.parse("20/01/2021");
		TradeBean t7=new TradeBean("T4",5,"CP-3","B2",maturityDate, todaysDate, 'N');
		tf.tradeList.replace("T4", t7);
		tf.checkExpiredDates();
		tf.printTrade();
		
		

	}
}
