package TradeFlow;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import junit.framework.TestCase;
import tradeWorkflow.TradeBean;
import tradeWorkflow.TradeImpl;

@TestMethodOrder(OrderAnnotation.class)
public class TradeTest extends TestCase {
	
	TradeImpl tf=new TradeImpl();
    Date todaysDate=Calendar.getInstance ().getTime ();
    SimpleDateFormat sd=new SimpleDateFormat("dd/MM/yyyy");
	
	@Test
	@Order(1)
    public void testIfTradeEmpty()
    {

        assertTrue(tf.checkIfTradeEmpty());
    }
	
	
	//Check if 1st Trade is added
    //T1	1	CP-1	B1	20/05/2020	today date	N

    @Test
    @Order(2)
    public void testAddTrade() throws Exception
    {
        Date maturityDate = sd.parse("20/05/2021");
        TradeBean t1=new TradeBean("T1",1,"CP-1","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t1);
        assertNotEquals(0,TradeImpl.tradeList.size());
    }
    
    //Check for Version

    //Check if Version is high the list will be updated
    //T2	2	CP-1	B1	20/05/2021	today date	N
    //T2	5	CP-5	B1	20/05/2021	today date 	N
    @Order(3)
    @Test
    public void testVersionHigh() throws Exception
    {
    	Date maturityDate = sd.parse("20/05/2021");
        TradeBean t2=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t2);

       //Changing Version as 3 and Changing Counter-Party ID to CP-4
        TradeBean t3=new TradeBean("T2",5,"CP-4","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t3);
        assertEquals("CP-4",TradeImpl.tradeList.get("T2").getCounterPartId());
    }
    
    
  //Check if Version is same the list will be updated
    //T1	1	CP-1	B1	20/05/2020	today date  N
    //T1	1	CP-2	B1	20/05/2020	today date	N
    @Test
    @Order(4)
    public void testVersionSame() throws Exception
    {
        Date maturityDate=sd.parse("20/05/2021");
        //Same Version as before and Changing Counter-Party ID to CP-2
        TradeBean t4=new TradeBean("T1",1,"CP-2","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t4);
        assertEquals("CP-2",TradeImpl.tradeList.get("T1").getCounterPartId());
    }


    
    //Check if maturity Date is greater than todays date the trade is added
    //T4	5	CP-3	B1	20/05/2021	today date	N
    @Test
    @Order(5)
    public void testMaturityGreater() throws Exception
    {
        Date maturityDate=sd.parse("20/05/2021");

        TradeBean t7=new TradeBean("T4",5,"CP-4","B3",maturityDate, todaysDate, 'N');
        tf.addTrade(t7);

        assertEquals(t7,TradeImpl.tradeList.get("T4"));

    }

    //Check if maturity Date is lower than todays date the Trade will not be added
    //T5  5  CP-3  B1  20/05/2020   today date  N
    @Test
    @Order(6)
    public void testMaurityLower() throws Exception
    {
        Date maturityDate=sd.parse("20/05/2020");
        TradeBean t8=new TradeBean("T5",1,"CP-4","B3",maturityDate, todaysDate, 'N');
        tf.addTrade(t8);
        assertNull(TradeImpl.tradeList.get("T5"));
    }

    //Check if Version is Same and date is lower the trade is not updated
    //T6	1	CP-2	B1	20/05/2021	today date N
    //T6	1	CP-2	B1	20/05/2020	today date	N
    @Test
    @Order(7)
    public void testMaturityLowerVersionSame() throws Exception
    {

        Date maturityDate1=sd.parse("20/05/2021");
        TradeBean t9=new TradeBean("T6",1,"CP-2","B1",maturityDate1, todaysDate, 'N');
        tf.addTrade(t9);
        Date maturityDate=sd.parse("20/05/2021");
        TradeBean t10=new TradeBean("T6",1,"CP-2","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t10);
        assertEquals(maturityDate1,TradeImpl.tradeList.get("T6").getMaturityDate());
    }

    //Check if Maturity Date is Same as Todays Date the list will be added
    //T7 7  CP-5  B4  todaysDate  todaysDate  N

    @Test
    @Order(8)
    public void testSameMaturity() throws Exception
    {
        Date todaysDate=Calendar.getInstance ().getTime ();
        TradeBean t11=new TradeBean("T7",7,"CP-5","B4",todaysDate, todaysDate, 'N');
        tf.addTrade(t11);
        assertNotNull(TradeImpl.tradeList.get("T7"));
    }
    
  //Check if version is high but maturity date is low the trade will be regected
    //T8 1  CP-3  B1  20/05/2021  todaysDate  N
    //T8 5  CP-3  B1  20/05/2020  todaysDate  N
    @Test
    @Order(9)
    public void testMaturitySameVersionMaturityLow() throws Exception
    {

        Date maturityDate=sd.parse("20/05/2021");

        TradeBean t12=new TradeBean("T8",1,"CP-3","B1",maturityDate, todaysDate, 'N');
        tf.addTrade(t12);
        maturityDate=sd.parse("20/05/2020");
        //Now Adding Another List
        TradeBean t13=new TradeBean("T8",5,"CP-2","B1",maturityDate, todaysDate, 'N');
        assertEquals(1,TradeImpl.tradeList.get("T8").getVersion());

    }



    //Check If Maturity Date is Expired it will update the Expired Flag
    @Test
    @Order(10)
    public void testExpiry() throws ParseException
    {
        Date maturityDate=sd.parse("20/05/2020");
        TradeBean t16=new TradeBean("T10",6,"CP-4","B1",maturityDate, todaysDate, 'N');
        TradeImpl.tradeList.put("T10",t16); // hardcoded as it need to be tested and the conditio is false
        tf.checkExpiredDates();
        assertEquals('Y',TradeImpl.tradeList.get("T10").getExpired());
    }
    
    //Empty the HashMap to add / update given testcase from the table
    public void removeAllTrade()
    {
    	TradeImpl.tradeList.clear();
    }
    
    //Check the testcase for T1	1	CP-1	B1	20/05/2020	<today date>	N
    //Adding the trade will fail so Checking the size of the map to be empty
    @Test
    @Order(11)
    public void test1() throws Exception
    {
    	removeAllTrade();
    	Date maturityDate=sd.parse("20/05/2020");
    	TradeBean t17=new TradeBean("T1",1,"CP-1","B1",maturityDate, todaysDate, 'N');
    	tf.addTrade(t17);
    	assertEquals(0, TradeImpl.tradeList.size());
    }
    
    //Check the testcase for T2	2	CP-2	B1	20/05/2021	<today date>	N
    //Adding the trade will be added in the trade map
    @Test
    @Order(12)
    public void test2() throws Exception
    {
    	Date maturityDate=sd.parse("20/05/2021");
    	TradeBean t18=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
    	tf.addTrade(t18);
    	assertEquals(1, TradeImpl.tradeList.size());
    }
    
    
    @Test
    @Order(13)
    public void test4() throws Exception
    {
    	Date maturityDate=sd.parse("20/05/2020");
        TradeBean t17=new TradeBean("T1",1,"CP-1","B1",maturityDate, todaysDate, 'N');
        maturityDate=sd.parse("20/05/2021");
        TradeBean t18=new TradeBean("T2",2,"CP-2","B1",maturityDate, todaysDate, 'N');
    
        maturityDate=sd.parse("20/05/2020");
        TradeBean t20=new TradeBean("T3",3,"CP-3","B2",maturityDate, todaysDate, 'N');
        TradeImpl.tradeList.put("T3", t20);
        
        tf.checkExpiredDates();
        assertEquals('Y',TradeImpl.tradeList.get("T3").getExpired());
    }


}
