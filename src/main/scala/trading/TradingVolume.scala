package trading

import org.scala_tools.time.Imports._
import scala.collection.JavaConversions._
import java.util.Date

/**
 * This code shows how to use a class wrapper for java class.  This would make the
 * class easier to manipulate as in this example the lastUpdate becomes jodatime
 * and the volume is now a rich scala int
 */
object TradingVolume {
	val trades: Seq[Trade] = {
		val ts = new DummyTradeService(); ts.getTrades
	}
	
	val updatedToday = (trade: Trade) => DateTime.yesterday <= trade.lastUpdated
	
	def main(args: Array[String]) {
		val sumOfVolumeTradedToday = trades filter updatedToday map (_.volume) sum;
		println(sumOfVolumeTradedToday)
	}

	implicit def dateToDateTime(date: Date): DateTime = new DateTime(date.getTime)
	implicit def tradeToTradeWrapper(trade: Trade): TradeWrapper = new TradeWrapper(trade)
}

class TradeWrapper(trade: Trade) {
	def symbol = trade.getSymbol
	def lastUpdated = new DateTime(trade.getLastUpdated().getTime)
	def volume = trade.getVolume: Int
}