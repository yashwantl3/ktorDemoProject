package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    routing {
        get("/") {
            println("URI: ${call.request.uri}")
            println("Header: ${call.request.headers.names()}")
            println("Query Paremeters  : ${call.request.queryParameters.names()}")
            println("Name  : ${call.request.queryParameters["name"]}")
            println("Email   : ${call.request.queryParameters["email"]}")
            call.respondText("Hello World!")
        }

        get("/iphones/{page}"){
            var pgNumber=call.parameters["page"]
            call.respond("You are on Page: ${pgNumber}")
            call.respondText("Something wennt wrong", status = HttpStatusCode.GatewayTimeout )
        }

        post("/login"){
            val userinfo=call.receive<UserInfo >()
            println(userinfo)
            call.respondText("Everything Working")
        }

        get("/getUser"){
            val resObj=UserInfo("Yashwant","yash@gmail.com")
            call.respond(resObj)
        }
    }
}

@Serializable
data class UserInfo(
    val name: String,
    val email: String
)