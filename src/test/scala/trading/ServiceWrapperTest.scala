package trading

import org.junit.Test
import org.junit.Assert._
import scala.collection.JavaConversions._

class ServiceWrapperTest {
	
	@Test
	def testWithWorkingService {
		implicit val emptyTrade = (trades: Seq[Trade]) => if (trades.size == 0) true else false
		
		import ServiceWrapper.printException
		
		val tradeService = ServiceWrapper[Seq[Trade]] (() => { 
			val ts = new DummyTradeService()
			ts.getTrades
		})

		val results = tradeService.execute
		
		results match {
			case Some(trades) => assertTrue(true)
			case None => fail("expected to got some trades")
		}
	}
	
	@Test
	def testWithWorkingServiceUsingExplicitAsNone {
		val tradeService = ServiceWrapper[Seq[Trade]] (() => { 
			val ts = new DummyTradeService()
			ts.getTrades
		}) ((exception: java.lang.Throwable) => exception.printStackTrace(), (trades: Seq[Trade]) => if (trades.size == 0) true else false)

		val results = tradeService.execute
		
		results match {
			case Some(trades) => assertTrue(true)
			case None => fail("expected to got some trades")
		}
	}
	
	@Test
	def testWithNullServiceAndDefaultNullAsNone {
		val nullService = new TradeService() {
			def getTrades = null
		}
		
		import ServiceWrapper.nullAsNone
		import ServiceWrapper.printException
		
		val tradeService = ServiceWrapper[Seq[Trade]] (() => { 
			nullService.getTrades
		})

		val results = tradeService.execute
		
		results match {
			case Some(trades) => fail("expected to get none")
			case None => assertTrue(true)
		}
	}

	@Test
	def testWithExceptionService {
		val exceptionService = new TradeService() {
			def getTrades = throw new Exception("service that always throw exception")
		}

		implicit val emptyTrade = (trades: Seq[Trade]) => if (trades.size == 0) true else false
		import ServiceWrapper.printException
		
		val tradeService = ServiceWrapper[Seq[Trade]] (() => { 
			exceptionService.getTrades
		})

		try {
			tradeService.execute
		} catch {
			case e: Exception => assertTrue(true)
		}

		tradeService.executeWithTry match {
			case Right(Some(trades)) => fail("expected to get exception")
			case Right(None) => fail("expected to get exception")
			case Left(exception) => assertTrue(true)
		}
		
		tradeService.executeWithHandle match {
			case Some(trades) => fail("expected to get none")
			case None => assertTrue(true)					
		}
	}

	@Test
	def testFailingService {
		val exceptionService = new TradeService() {
			def getTrades = throw new Exception("service that always throw exception")
		}
		
		implicit val emptyTrade = (trades: Seq[Trade]) => if (trades.size == 0) true else false
		import ServiceWrapper.printException
		
		val tradeService = ServiceWrapper[Seq[Trade]] (() => { 
			exceptionService.getTrades
		})

		val (msg, gotTrades, trades) = tradeService.executeWithTry match {
			case Right(Some(trades)) => ("", true, trades)
			case Right(None) => ("", false, Nil)
			case Left(exception) => (exception.getMessage, false, Nil)
		}
		
		assertEquals("service that always throw exception", msg)
		assertFalse(gotTrades)
		assertEquals(0, trades.size);

	}
}