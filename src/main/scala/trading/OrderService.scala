package trading

class OrderService {
	def processOrder(
			doneVolume: BigDecimal, 
			targetVolume: BigDecimal, 
			totalDoneVolume: BigDecimal, 
			order: Order) {

		def reduceToEntireOrder = if (targetVolume - doneVolume > order.volume - totalDoneVolume)
				order.volume - totalDoneVolume
			else
				targetVolume - doneVolume
				
		def reduceToRestriction(volumeToTrade: BigDecimal, volumeRestriction: BigDecimal) = {
			if (totalDoneVolume + volumeToTrade > volumeRestriction) {
				volumeRestriction - totalDoneVolume
			} else volumeToTrade
		}
			
		val volumeToTrade = order.volumeRestriction match {
			case Some(volumeRestrictionPercentage) => reduceToRestriction(reduceToEntireOrder, volumeRestrictionPercentage.toDouble)
			case None => reduceToEntireOrder
		}					
		
		if (volumeToTrade > 0) {
			// do some order here
		}
		
	}

}