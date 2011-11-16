package trading;

import java.math.BigDecimal;

public class OrderServiceJ {
	public void processOrder(
			BigDecimal doneVolume, 
			BigDecimal targetVolume, 
			BigDecimal totalDoneVolume, 
			OrderJ order) {
		
		BigDecimal volumeToTrade = targetVolume.subtract(doneVolume);
		if (volumeToTrade.compareTo(order.getVolume().subtract(totalDoneVolume)) > 0) {
			volumeToTrade = order.getVolume().subtract(totalDoneVolume);
		}
		if (order.getVolumeRestriction() != null) {
			if (totalDoneVolume.add(volumeToTrade).compareTo(order.getVolumeRestriction()) > 0) {
				volumeToTrade = order.getVolumeRestriction().subtract(totalDoneVolume);
			}
		}
		if (volumeToTrade.compareTo(new BigDecimal(0)) > 0) {
			// do some order here
		}

	}

}
