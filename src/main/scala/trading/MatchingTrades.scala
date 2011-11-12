package trading

import java.util.Date
import TradingVolume._
import scala.collection.JavaConversions._
import org.scala_tools.time.Imports._


object MatchingTrades {
	val telTrade = new Trade("TEL", 10, new Date());
	
	def main(args: Array[String]) {
		println((new ForCompMatcher).doMatch(telTrade))

		println((new MatchMatcher).doMatch(telTrade))
	}
}

trait TradeMatcher {
	val trades: Seq[Trade] = (new DummyTradeService()).getTrades
	
	def doMatch(trade: Trade): String
}

class ForCompMatcher extends TradeMatcher {
	def doMatch(trade: Trade): String = {
		val matchedTrades = for {
			t <- trades
			if (trade.symbol == t.symbol)
		} yield t
		
		if (matchedTrades.size == 1)
			"matches"
		else if (matchedTrades.size > 1)
			"2 or more values are not expected"
		else
			"unexpected result"
	}
}

class MatchMatcher extends TradeMatcher {
	def doMatch(trade: Trade): String = {
		val matchedTrades = trades.filter(trade.symbol == _.symbol)
		
		// matchedTrades match { incorrect as matchedTrades is a seq which would be an arraybuffer 
		// because of the above conversion will convert java list to array buffer
		matchedTrades.toList match {
			case trade :: Nil => "matches"
			case trade :: tail => "2 or more values are not expected"
			case _ => "unexpected result"
		}
	}
}