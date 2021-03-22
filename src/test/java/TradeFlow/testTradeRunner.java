package TradeFlow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ExceptionHandler.TradeException;
import tradeWorkflow.TradeBean;
import tradeWorkflow.TradeImpl;

 //Order of Junit Testcases
public class testTradeRunner {

	TradeImpl tf=new TradeImpl();
    Date todaysDate=Calendar.getInstance ().getTime ();
    SimpleDateFormat sd=new SimpleDateFormat("dd/MM/yyyy");
    
   
    
    @Test
    void testIfTradeEmpty()
    {

        assertTrue(tf.checkIfTradeEmpty());
    }
    
  //Check if 1st Trade is added
    //T1	1	CP-1	B1	20/05/2020	today date	N

    @Test
    void testAddTrade() throws Exception
    {
        Date maturityDate = sd.parse("20/05/2021");
        TradeBean t1=new TradeBean("T1",1,"CP-1","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t1);
        assertEquals(1,tf.tradeList.size());
    }
    
  //Check for Version

    //Check if Version is high the list will be updated
    //T2	2	CP-1	B1	20/05/2021	today date	N
    //T2	5	CP-5	B1	20/05/2021	today date 	N
    @Test
    void testVersionHigh() throws Exception
    {
    	Date maturityDate = sd.parse("20/05/2021");
        TradeBean t2=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t2);

       //Changing Version as 3 and Changing Counter-Party ID to CP-4
        TradeBean t3=new TradeBean("T2",5,"CP-4","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t3);
        assertEquals("CP-4",tf.tradeList.get("T2").getCounterPartId());
    }
    //Check if Version is same the list will be updated
    //T1	1	CP-1	B1	20/05/2020	today date  N
    //T1	1	CP-2	B1	20/05/2020	today date	N
    @Test
    void testVersionSame() throws Exception
    {
        Date maturityDate=sd.parse("20/05/2021");
        //Same Version as before and Changing Counter-Party ID to CP-2
        TradeBean t4=new TradeBean("T1",1,"CP-2","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t4);
        assertEquals("CP-2",tf.tradeList.get("T1").getCounterPartId());
    }

    //Check if Version is low the trade will be rejected
    //T3	5	CP-3	B1	20/05/2014	today date	N
    //T3	1	CP-2	B1	20/05/2014	today date	N
    @Test
    void testVersionLow() throws Exception
    {
    	Date maturityDate = sd.parse("20/05/2021");
    	TradeBean t5 = new TradeBean("T3",5,"CP-3","B1",maturityDate,todaysDate, 'N');
        tf.addTrade(t5);
        int sizeofList=TradeImpl.tradeList.size();
        //Now Adding Another List
        TradeBean t6=new TradeBean("T3",1,"CP-2","B1",maturityDate, todaysDate, 'N');

        assertThrows(TradeException.class,()->tf.addTrade(t6),"1 is less than 5");

    }
    
    //Check if maturity Date is greater than todays date the trade is added
    //T4	5	CP-3	B1	20/05/2021	today date	N
    @Test
    void testMaturityGreater() throws Exception
    {
        Date maturityDate=sd.parse("20/05/2021");

        TradeBean t7=new TradeBean("T4",5,"CP-4","B3",maturityDate, todaysDate, 'N');
        tf.addTrade(t7);

        assertEquals(t7,tf.tradeList.get("T4"));

    }

    //Check if maturity Date is lower than todays date the Trade will not be added
    //T5  5  CP-3  B1  20/05/2020   today date  N
    @Test
    void testMaurityLower() throws Exception
    {
        Date maturityDate=sd.parse("20/05/2020");
        TradeBean t8=new TradeBean("T5",1,"CP-4","B3",maturityDate, todaysDate, 'N');
        tf.addTrade(t8);
        assertNull(tf.tradeList.get("T5"));
    }

    //Check if Version is Same and date is lower the trade is not updated
    //T6	1	CP-2	B1	20/05/2021	today date N
    //T6	1	CP-2	B1	20/05/2020	today date	N
    @Test
    void testMaturityLowerVersionSame() throws Exception
    {

        Date maturityDate1=sd.parse("20/05/2021");
        TradeBean t9=new TradeBean("T6",1,"CP-2","B1",maturityDate1, todaysDate, 'N');
        tf.addTrade(t9);
        Date maturityDate=sd.parse("20/05/2021");
        TradeBean t10=new TradeBean("T6",1,"CP-2","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t10);
        assertEquals(maturityDate1,tf.tradeList.get("T6").getMaturityDate());
    }

    //Check if Maturity Date is Same as Todays Date the list will be added
    //T7 7  CP-5  B4  todaysDate  todaysDate  N

    @Test
    void testSameMaturity() throws Exception
    {
        Date todaysDate=Calendar.getInstance ().getTime ();
        TradeBean t11=new TradeBean("T7",7,"CP-5","B4",todaysDate, todaysDate, 'N');
        tf.addTrade(t11);
        assertNotNull(tf.tradeList.get("T7"));
    }
    
  //Check if version is high but maturity date is low the trade will be regected
    //T8 1  CP-3  B1  20/05/2021  todaysDate  N
    //T8 5  CP-3  B1  20/05/2020  todaysDate  N
    @Test
    void testMaturitySameVersionMaturityLow() throws Exception
    {

        Date maturityDate=sd.parse("20/05/2021");

        TradeBean t12=new TradeBean("T8",1,"CP-3","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t12);
        maturityDate=sd.parse("20/05/2020");
        //Now Adding Another List
        TradeBean t13=new TradeBean("T8",5,"CP-2","B1",maturityDate, todaysDate, 'N');
        assertEquals(1,tf.tradeList.get("T8").getVersion());

    }

    //Check if both version and maturity low the trade will not be added
    //T8 1  CP-3  B1  20/05/2021  todaysDate  N
    //T8 5  CP-3  B1  20/05/2020  todaysDate  N1
    @Test
    void testVersionAndMaturityLow() throws Exception
    {
        Date maturityDate=sd.parse("20/05/2021");

        TradeBean t14=new TradeBean("T9",5,"CP-3","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t14);

        maturityDate=sd.parse("20/05/2020");
        //Now Adding Another List
        TradeBean t15=new TradeBean("T9",1,"CP-2","B1",maturityDate, todaysDate, 'N');
        assertThrows(TradeException.class,()->tf.addTrade(t15),"1 is less than 5");

    }

    //Check If Maturity Date is Expired it will update the Expired Flag
    @Test
    void testExpiry() throws ParseException
    {
        Date maturityDate=sd.parse("20/05/2020");
        TradeBean t16=new TradeBean("T10",6,"CP-4","B1",maturityDate, todaysDate, 'N');
        tf.tradeList.put("T10",t16); // hardcoded as it need to be tested and the conditio is false
        tf.checkExpiredDates();
        assertEquals('Y',tf.tradeList.get("T10").getExpired());
    }
    
    //Empty the HashMap to add / update given testcase from the table
    void removeAllTrade()
    {
    	tf.tradeList.clear();
    }
    
    //Check the testcase for T1	1	CP-1	B1	20/05/2020	<today date>	N
    //Adding the trade will fail so Checking the size of the map to be empty
    @Test
    void test1() throws Exception
    {
    	Date maturityDate=sd.parse("20/05/2020");
    	TradeBean t17=new TradeBean("T1",1,"CP-1","B1",maturityDate, todaysDate, 'N');
    	tf.addTrade(t17);
    	assertEquals(0, tf.tradeList.size());
    }
    
    //Check the testcase for T2	2	CP-2	B1	20/05/2021	<today date>	N
    //Adding the trade will be added in the trade map
    @Test
    void test2() throws Exception
    {
    	Date maturityDate=sd.parse("20/05/2021");
    	TradeBean t18=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
    	tf.addTrade(t18);
    	assertEquals(1, tf.tradeList.size());
    }
    
    //Check the testcase for T2	1	CP-1	B1	20/05/2021	14/03/2015	N
    //Adding the trade will not be added to the trade list
    @Test
    void test3() throws Exception
    {
    	Date maturityDate=sd.parse("20/05/2021");
    	TradeBean t18=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
    	tf.addTrade(t18);
    	assertEquals(1, tf.tradeList.size());
    	maturityDate=sd.parse("20/05/2021");
    	Date createdDate=sd.parse("14/03/2015");
    	TradeBean t19=new TradeBean("T2",1,"CP-2","B1",maturityDate, createdDate, 'N');
//    	System.out.println("Hellp");
    	 assertThrows(TradeException.class,()->tf.addTrade(t19));
    }
    
    @Test
    void test4() throws Exception
    {
    	Date maturityDate=sd.parse("20/05/2020");
        TradeBean t17=new TradeBean("T1",1,"CP-1","B1",maturityDate, todaysDate, 'N');
        maturityDate=sd.parse("20/05/2021");
        TradeBean t18=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
    
        maturityDate=sd.parse("20/05/2020");
        TradeBean t20=new TradeBean("T3",3,"CP-3","B2",maturityDate, todaysDate, 'N');
        tf.tradeList.put("T3", t20);
        
        tf.checkExpiredDates();
        assertEquals('Y',tf.tradeList.get("T3").getExpired());
    }


}