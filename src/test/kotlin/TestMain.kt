import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestMain : StringSpec({
    "should greet properly" {
        hello() shouldBe "Hello, world!"
    }
})