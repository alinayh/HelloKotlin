import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication

class TestMain : StringSpec({
    "should greet properly" {
        hello() shouldBe "Hello, world!"
    }
    "should retrieve root path properly" {
        withTestApplication(Application::adder) {
            handleRequest(HttpMethod.Get, "/").apply {
                response.status() shouldBe HttpStatusCode.OK
                response.content shouldBe "Hello, world!"
            }
        }
    }
})
