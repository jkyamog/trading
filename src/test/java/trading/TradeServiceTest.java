package trading;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TradeServiceTest {
	
	@Test
	public void testFailingTradeService() {
		TradeService ts = new TradeService() {
			@Override
			public List<Trade> getTrades() throws Exception {
				throw new Exception("service that always throw exception");
			}
		};

		List<Trade> trades = null;
		String msg = "";
		try {
			trades = ts.getTrades();
		} catch (Exception e) {
			msg = e.getMessage();
			trades = new ArrayList<Trade> ();
		}
		
		boolean gotTrades = false;
		if (trades.size() > 0) gotTrades = true; 
		
		assertEquals("service that always throw exception", msg);
		assertFalse(gotTrades);
		assertEquals(0, trades.size());
		
	}
	

}
