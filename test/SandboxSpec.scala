package services

import org.scalatest._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

@DoNotDiscover
class SandBoxSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  "a test" must {
    "be true" in {
      true mustBe true
    }

    "use app" in {
      app mustBe an[play.api.Application]
      app must not be an[play.api.mvc.AbstractController]
    }
  }

  "a service" must {
    "get()" when {
      "unauthorized" must {
        "return None" in {
          true mustBe true
        }
      }
    }
  }

}
