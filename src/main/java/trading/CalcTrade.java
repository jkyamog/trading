package trading;

import java.math.BigDecimal;

public class CalcTrade {

	public static BigDecimal calcByIteration(BigDecimal marketVolume, BigDecimal volumeDone, Double pctAllowed) {
		BigDecimal increaseInRestrictionFromOurTrade = new BigDecimal(0);
		
		BigDecimal volumeToTrade = marketVolume.multiply((new BigDecimal(pctAllowed))).divide(new BigDecimal(100)).subtract(volumeDone);
		for (int i = 0; i <= CalcTradeRestriction.maxPower(); i++ ) {
			increaseInRestrictionFromOurTrade = increaseInRestrictionFromOurTrade.add(volumeToTrade.multiply(new BigDecimal(Math.pow(pctAllowed/100, i))));
		}
		return (increaseInRestrictionFromOurTrade).setScale(0, BigDecimal.ROUND_FLOOR).add(volumeDone);
	}
}
