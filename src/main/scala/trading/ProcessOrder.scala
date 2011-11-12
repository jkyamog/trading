package trading

object ProcessRunner {
	
	val ordersAtDb = for {
		i <- 1 to 5
		oDb = new OrderAtDb(i, i * 100)
	} yield oDb

	val matchById = (oAt: OrderAtTrading, oDb: OrderAtDb) => oAt.id == oDb.id.toString
	val volumeChanged = (oDb: OrderAtDb, oAt: OrderAtTrading) => oDb.volume != oAt.volume
	val printing = (order: OrderAtTrading) => println("order: " + order.id + " has changed to volume " + order.volume)

	def main(args: Array[String]) {  
		
		ProcessOrder.processOrderAtDb(ordersAtDb, matchById, volumeChanged, printing)

//		ProcessOrder( ordersAtDb) by printing when matchById and volumeChanged 
	}
	
}


object ProcessOrder {

	private val ordersAtTrading = for {
		i <- 1 to 3
		oAt = new OrderAtTrading(i.toString, i * 200)
	} yield oAt
	
	def processOrderAtDb(ordersAtDb: Seq[OrderAtDb],
			matchCriteria: (OrderAtTrading, OrderAtDb) => Boolean,
			criteriaForProcess: (OrderAtDb, OrderAtTrading) => Boolean,
			processOrder: (OrderAtTrading) => Unit) {

		val ordersToProcess = for {
			order <- ordersAtDb
			orderMatching <- ordersAtTrading filter (matchCriteria(_, order))
			if (criteriaForProcess(order, orderMatching))
		} yield orderMatching
	
		ordersToProcess foreach processOrder
	}
	
	
//	def apply(p: ProcessOrder) = processOrderAtDb(p.ordersAtDb, p.matchCriteria, p.criteriaForProcess, p.processOrder)
}

trait CanBeProcessed {
	private var processOrder: Function1[OrderAtTrading, Unit] = _
	private var matchCriteria: Function2[OrderAtTrading, OrderAtDb, Boolean] = _ 
	private var criteriaForProcess: Function2[OrderAtDb, OrderAtTrading, Boolean] = _
	
	def by(processOrder: (OrderAtTrading) => Unit) = { this.processOrder = processOrder; this }
	def when(matchCriteria: (OrderAtTrading, OrderAtDb) => Boolean) = { this.matchCriteria = matchCriteria; this }
	def and(criteriaForProcess: (OrderAtDb, OrderAtTrading) => Boolean) = { this.criteriaForProcess = criteriaForProcess; this }
}

case class OrderAtDb(id: Int, volume: Int)
case class OrderAtTrading(id: String, volume: Int)	
	
