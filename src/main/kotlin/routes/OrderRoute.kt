package routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import models.orderStorage

fun Route.listOrdersRoute() {
    get("/order") {
        if (orderStorage.isNotEmpty()) {
            call.respond(orderStorage)
        } else {
            call.respondText("No order Found", status = HttpStatusCode.NotFound)
        }
    }
}

fun Route.getOrderRoute() {
    get("/order/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val order = orderStorage.find { it.id == id } ?: return@get call.respondText(
            "No order with id",
            status = HttpStatusCode.NotFound
        )
        call.respond(order)
    }
}

fun Route.totalizeOrderRoute() {
    get("/order/{id}/total") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val order = orderStorage.find { it.id == id } ?: return@get call.respondText(
            "Not Found",
            status = HttpStatusCode.NotFound
        )
        //  val total = order.contents.map { it.price * it.amount }.sum() ||
        val total = order.contents.sumOf { it.price * it.amount }
        call.respond(total)
    }
}

fun Application.registerOrderRoutes() {
    routing {
        listOrdersRoute()
        getOrderRoute()
        totalizeOrderRoute()
    }
}
