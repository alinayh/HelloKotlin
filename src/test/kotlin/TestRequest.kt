import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TestRequest : StringSpec({
    "should add properly" {
        CalculatorRequest("add", 4, 5).result.result shouldBe 9
    }
    "should multiply properly" {
        CalculatorRequest("multiply", 4, 5).result.result shouldBe 20
    }
})
