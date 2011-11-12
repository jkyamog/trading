package trading;

import java.util.Date;

public class Trade {
	private final String symbol;
	private final Integer volume;
	private final Date lastUpdated;
	
	public Trade(String symbol, Integer volume, Date lastUpdated) {
		this.symbol = symbol;
		this.volume = volume;
		this.lastUpdated = lastUpdated;
	}

	public String getSymbol() {
		return symbol;
	}

	public Integer getVolume() {
		return volume;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
}
