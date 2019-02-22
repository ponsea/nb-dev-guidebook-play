import org.scalatest._

@DoNotDiscover
class SomeSpec extends WordSpec with OptionValues with Matchers {

  "A Set" when {
    "empty" should {
      "have size 0" in {
        assert(Set.empty.size == 0)
      }

      "produce NoSuchElementException when head is invoked" in {
        assertThrows[NoSuchElementException] {
          Set.empty.head
        }
      }
    }
  }

  "A test" must {
    "success" in {
      val opt: Option[Int] = None
      opt.value should be (1)
    }
  }
}
