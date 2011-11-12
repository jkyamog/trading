package trading;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DummyTradeService implements TradeService {
	public List<Trade> getTrades() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterday = cal.getTime();
		
		List<Trade> trades = new ArrayList<Trade> ();
		trades.add(new Trade("TEL", 10, yesterday));
		trades.add(new Trade("CEN", 12, new Date()));
		trades.add(new Trade("NZX", 20, new Date()));
		
		return trades;
	}
	
}
