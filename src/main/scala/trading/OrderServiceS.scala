package trading

object OrderServiceS {
	def processOrder(
			doneVolume: BigDecimal, 
			targetVolume: BigDecimal, 
			totalDoneVolume: BigDecimal, 
			order: OrderS) = {

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
		
		if (volumeToTrade > 0)
			Some(volumeToTrade)
		else
			None
		
	}

}
