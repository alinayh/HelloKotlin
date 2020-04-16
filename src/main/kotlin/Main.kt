import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun hello(): String {
    return "Hello, world!"
}

data class OperationResult(val operation: String, val first: Int, val second: Int, val result: Int)
data class CalculatorRequest(val operation: String, val first: Int, val second: Int) {
    val result: OperationResult
    init {
        val mathResult = when (operation) {
            "add" -> first + second
            "multiply" -> first * second
            else -> throw Exception("$operation is not supported")
        }
        result = OperationResult(operation, first, second, mathResult)
    }
}

fun Application.adder() {
    val counts: MutableMap<String, Int> = mutableMapOf()
    install(ContentNegotiation) {
        gson { }
    }
    routing {
        get("/") {
            call.respondText(hello())
        }
        get("/test") {
            call.respondText("Testing...")
        }
        get("/count/{first}") {
            val firstCount = counts.getOrDefault(call.parameters["first"], 0) + 1
            counts[call.parameters["first"].toString()] = firstCount
            call.respondText(firstCount.toString())
        }
        get("/{operation}/{first}/{second}") {
            try {
                val operation = call.parameters["operation"]!!
                val first = call.parameters["first"]!!.toInt()
                val second = call.parameters["second"]!!.toInt()
                val result = when (operation) {
                    "add" -> first + second
                    "subtract" -> first - second
                    "multiply" -> first * second
                    "divide" -> first / second
                    else -> throw Exception("$operation is not supported.")
                }
                val operationResult = OperationResult(operation, first, second, result)
                call.respond(operationResult)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, e)
            }
        }
        post("/calculate") {
            try {
                val request = call.receive<CalculatorRequest>()
                val result = when (request.operation) {
                    "add" -> request.first + request.second
                    "subtract" -> request.first - request.second
                    "multiply" -> request.first * request.second
                    "divide" -> request.first / request.second
                    else -> throw Exception("${request.operation} is not supported")
                }
                call.respond(OperationResult(request.operation, request.first, request.second, result))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::adder).start(wait = true)
}
