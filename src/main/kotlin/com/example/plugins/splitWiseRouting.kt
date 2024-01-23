package com.example.plugins

import com.example.entities.UserGroup
import com.example.models.dao.UserInterface
import entities.Transaction
import entities.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.splitWiseRouting(dao: UserInterface, txnDao: com.example.models.dao.Transaction){
    routing {
        get("/") {
            call.respond("Welcome to Splitwise")
        }

        //Do a particular transaction for current Time {from} to {to}
        post("/txn/{from}/{to}"){
            val user1=call.parameters["from"]
            val user2=call.parameters["to"]
            val txn=call.receive<Transaction>()
            println(Transaction)
            call.respond("Doing transfer from ${user1} to ${user2}")
        }

        post("/adduser") {
            try {
                val formParameters = call.receive<User>()
                val name = formParameters.name
                val balance = formParameters.balance
                val article = dao.insert(name, balance)
                call.respond("User added successfully")
            }catch (e: Throwable){
                application.log.error("Failed to register user", e)
                call.respond(HttpStatusCode.BadRequest, "Problems creating User")
            }
        }

        get("/getusers") {
            val listOfUsers = dao.getAllUsers()

                call.respond(listOfUsers.toString())
        }

        get("/getUser/{userId}") {
            val request = call.receive<GetUserRequest>()
            val user = dao.getUserbyId(request.userId)
            call.respond(user.toString())
        }

        post("/create_user_group"){
            val userGroup=call.receive<UserGroup>()
            val userList=userGroup.users
            val payment=userGroup.payment
            val sz=userList.size
            val split=payment/sz
            call.respond("According to default split everyone pays: ${split}")
        }

        post("/transaction/default_split"){
            val txn=call.receive<Transaction>()
            val userList=txn.users
            val usersString=userList.toString()
            val payee=txn.payee
            val amount=txn.amt
            val sz=userList.size
            val split=(amount.value)/sz
            val transaction=txnDao.insert(txn.txnId,userList,payee,amount,txn.description,txn.createdOn)
            for (user in userList) {
                if (user.userId != payee.userId) {
                    user.balance -= split
                    dao.updateUserBalance(user.userId, user.balance)
                }
            }
            call.respond("According to default split everyone pays: ${split}")
        }

        get("/getAllTxn"){
            val txns=txnDao.getAllTxn()
            call.respond(txns.toString())
        }

        get("/getTxnById") {
            try{
                val req=call.receive<GetTxnReq>()
                val txnId=req.txnId
                call.respond(txnDao.getTxnById(txnId).toString())
            }catch (e: NumberFormatException) {
                call.respondText("Invalid ID format. Please provide an integer ID.", status = HttpStatusCode.BadRequest)
            }
        }

        post("/splitInGroup/{groupId}"){
            val groupId=call.parameters["groupId"]

            val userinfo=call.receive<UserInfo >()
            println(userinfo)
            call.respondText("Everything Working")
        }

//        get("/getUser"){
//            val resObj=UserInfo("Yashwant","yash@gmail.com")
//            call.respond(resObj)
//        }
    }
}

@Serializable
data class GetUserRequest(val userId: Int)

@Serializable
data class GetTxnReq(val txnId: Int)