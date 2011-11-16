package trading
import scala.annotation.tailrec

object CalcTradeRestriction {
	val marketVolume = 70000
	val volumeDone = 10000
	val pctAllowed = 25
	
	val volumeToTrade = marketVolume * pctAllowed / 100 - volumeDone: BigDecimal
	
	var volumesCalculated: Stream[BigDecimal] = _
	var maxPower = 500
	
	def calcByRecursion(marketVolume: BigDecimal, volumeDone: BigDecimal, pctAllowed: Double): BigDecimal = {
		def calc(volumeToTrade: BigDecimal, power: Int): BigDecimal =
			if (power > maxPower)
				0
			else
				volumeToTrade * Math.pow(pctAllowed/100, power) + calc(volumeToTrade, power + 1)
		
		return (volumeToTrade + calc(volumeToTrade, 1)).setScale(0, BigDecimal.RoundingMode.FLOOR) + volumeDone
		
	}
	
	def calcByIteration(marketVolume: BigDecimal, volumeDone: BigDecimal, pctAllowed: Double): BigDecimal = {
		var increaseInRestrictionFromOurTrade: BigDecimal = 0
		for ( i <- 1 to maxPower ) {
			increaseInRestrictionFromOurTrade += volumeToTrade * Math.pow(pctAllowed/100, i)
		}
		return (volumeToTrade + increaseInRestrictionFromOurTrade).setScale(0, BigDecimal.RoundingMode.FLOOR) + volumeDone
	}
	
	def calcByFold(marketVolume: BigDecimal, volumeDone: BigDecimal, pctAllowed: Double) = {
		val calcVol = (power: Int) => Math.pow(pctAllowed/100, power): BigDecimal
		val newVolume = (1 to maxPower).foldLeft(volumeToTrade) {(increaseInRestrictionFromOurTrade, power) =>
			increaseInRestrictionFromOurTrade + calcVol(power) * volumeToTrade
		}
		
		newVolume.setScale(0, BigDecimal.RoundingMode.FLOOR) + volumeDone
	}
	
	def calcByStream(marketVolume: BigDecimal, volumeDone: BigDecimal, pctAllowed: Double) = {
		import scala.Stream._
		
		val calcVol = (power: Int, volume: BigDecimal) => (Math.pow(pctAllowed/100, power): BigDecimal) * volume

		def calcIncrease(increaseInRestrictionFromOurTrade: BigDecimal, power: Int): Stream[BigDecimal] = 
			cons(increaseInRestrictionFromOurTrade, calcIncrease(calcVol(power, volumeToTrade), power + 1))
		
		volumesCalculated = calcIncrease(volumeToTrade + volumeDone, 1)
		
		(volumesCalculated take (maxPower+1) sum).setScale(0, BigDecimal.RoundingMode.FLOOR)
	}
	
	def main(args: Array[String]) {
		
		val byRecursion = () => calcByRecursion(marketVolume, volumeDone, pctAllowed) 
		val byIteration = () => calcByIteration(marketVolume, volumeDone, pctAllowed)
		val byFold = () => calcByFold(marketVolume, volumeDone, pctAllowed)
		val byStream = () => calcByStream(marketVolume, volumeDone, pctAllowed)
		val byIterationJava = () => CalcTrade.calcByIteration(new java.math.BigDecimal(marketVolume), new java.math.BigDecimal(volumeDone), pctAllowed): BigDecimal
		
		byRecursion()
		byIteration()
		byIterationJava()
		byFold()
		byStream()
		
		maxPower = 5
		
		def byStream2 = () => (volumesCalculated take (maxPower+1) sum).setScale(0, BigDecimal.RoundingMode.FLOOR)

		print("recursion: "); timeAndPrint(byRecursion)
		print("iteration: "); timeAndPrint(byIteration)
		print("iteration java: "); timeAndPrint(byIterationJava)
		print("fold: "); timeAndPrint(byFold)
		print("stream: "); timeAndPrint(byStream2)
		
	}
	
	def timeAndPrint(calculate: () => BigDecimal) {
		var result = 0 :BigDecimal
		for (i <- 1 to 10000)
			calculate()
		val start = System.currentTimeMillis
		for (i <- 1 to 10000)
			result = calculate()
		val finish = System.currentTimeMillis
		println(result + " " + (finish - start))
	}

}

