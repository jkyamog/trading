package trading

import org.scalatest.Spec
import org.scalatest.GivenWhenThen

class OrderServiceSpec extends Spec with GivenWhenThen {
	
	describe("A OrderServiceJ") {
		it ("should process an order") {
			import java.math.BigDecimal

			given("a volume of 100")
			val order = new OrderJ(new BigDecimal(100), null)
			val osj = new OrderServiceJ
			
			when("its processed")
			val volumeToTrade = osj.processOrder(new BigDecimal(500), new BigDecimal(600), new BigDecimal(0), order)
			
			then("should return volume to trade 100")
			assert(volumeToTrade === new BigDecimal(100))
		}
	}
	
	
	describe("A OrderServiceS") {
		it ("should process an order") {

			given("a volume of 100")
			val order = OrderS(100, None)
			
			when("its processed")
			val volumeToTrade = OrderServiceS.processOrder(
					doneVolume = 500, 
					targetVolume = 600, 
					totalDoneVolume = 0,
					order)
			
			then("should return volume to trade 100")
			assert(volumeToTrade.get === 100)
		}
	}


}