package tradeWorkflow;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ExceptionHandler.TradeException;

public class TradeImpl {

   public static Map<String,TradeBean> tradeList=new HashMap<String,TradeBean>();
	
	
	//check if no trade Exists
	public boolean checkIfTradeEmpty()
	{
		return tradeList.isEmpty();
	}
	
	/* Check transmission if the lower version is being received by the store,
	 *  it will reject the trade and throw an exception.
	 *	If the version is same it will override the existing record.
     */
	public boolean checkVersion(TradeBean trade,int version) throws TradeException
	{
		if(trade.getVersion()< version)
		{
			throw new TradeException(trade.getVersion()+" is less than "+ version);
			
		} else {
			return true;
		}
		
	}
	
	//Check if maturityDate is greater than today's date
	public boolean checkMaturityDate(Date maturityDate,Date CurrentDate)
	{	
		if(CurrentDate.compareTo(maturityDate) > 0)
			return false;
		
		return true;
	}
	
	/*
	 * Method to update the expiry status of trades if the maturity date has passed.
	 */
	public void checkExpiredDates() {
		
		Date currentDate=new Date();
		
		for(String strKey : tradeList.keySet() ){
		    if(currentDate.compareTo(tradeList.get(strKey).getMaturityDate())>0)
		    {
		    	TradeBean trade = tradeList.get(strKey);
		    	trade.setExpired('Y');
		   		tradeList.replace(strKey, trade);
		    }
		}	
	}
	
	
	// method to add a Trade
	public void addTrade(TradeBean T) throws Exception
	{
		if(tradeList.containsKey(T.getTradeId()))
		{
			TradeBean existingTread = tradeList.get(T.getTradeId());
			
			if( checkVersion(T, existingTread.getVersion()) &&
				checkMaturityDate(T.getMaturityDate(), existingTread.getMaturityDate()))
			{
				tradeList.replace(T.getTradeId(), T);
				System.out.println(T.getTradeId()+" is added to the Store");
				
			} else {
				System.out.println(T.getTradeId()+" could not be added in the store as maturity date is behind current date");
			}
		} else {
			
			if(checkMaturityDate(T.getMaturityDate(), T.getCreatedDate()))
			{
					tradeList.put(T.getTradeId(), T);
					System.out.println(T.getTradeId()+" is added to the Store");
			} else {
				System.out.println(T.getTradeId()+" could not be added in the store as maturity date is behind current date");
			}
		}
		
	}
	
	
	//get specific trade
	public TradeBean getTrade(String id) throws Exception {
		
		if(tradeList.containsKey(id))
			return tradeList.get(id);
		throw new TradeException ("Trade with "+id+" is not available");
		
	}
	
	//print the content of tradeList
	public void printTrade() {
		for(String id : tradeList.keySet())
		{
			System.out.println(tradeList.get(id).toString());
		}
	}
}
