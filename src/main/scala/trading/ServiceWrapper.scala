package trading

/**
 * A wrapper for a java service code that returns a T.  This would wrap
 * T into an Option so we can perform scala idiomatic match on it.
 * Rather then testing for nulls explicitly, an implicit nullAsNone is given as
 * a sample.  Its possible to pass of have an implicity for the asNone
 * function
 */
class ServiceWrapper[T] (body: () => T, handleException: (java.lang.Throwable) => Unit, asNone: (T) => Boolean) {
	
	def execute: Option[T] = {
		val result = body()
		if (asNone(result))
			None
		else
			Some(result)
	}

	def executeWithTry: Either[java.lang.Throwable, Option[T]] = {
		try {
			Right(execute)
		} catch {
			case e: Exception => Left(e)
		}
	}
	
	def executeWithHandle: Option[T] = {
		try {
			execute
		} catch {
			case e: Exception => handleException(e); None
		}
	}
	
	
}

object ServiceWrapper {
	implicit def nullAsNone[T] = (value: T) => if (value == null) true else false
	implicit def printException = (exception: java.lang.Throwable) => println(exception.getMessage)

	def apply[T](body: () => T)(implicit handleException: (java.lang.Throwable) => Unit, asNone: (T) => Boolean) = {
	
		new ServiceWrapper(body, handleException, asNone)
	}

}

