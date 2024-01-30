package com.example.plugins

import com.example.di.SplitWiseDataComponent
import com.example.models.dao.Debts
import com.example.models.dao.UserGroup
import com.example.models.dao.UserInterface
import com.example.repo.DebtsImp
import entities.Transaction
import entities.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import java.util.UUID
import javax.inject.Inject
class Router @Inject constructor(private val dao: UserInterface,private val txnDao: com.example.models.dao.Transaction,private val grpDao: UserGroup,private val debtsDao: Debts) {
    fun Application.splitWiseRouter() {

        routing {
            get("/") {
                call.respond("Welcome to Splitwise")
            }

            //Do a particular transaction for current Time {from} to {to}
            post("/txn/{from}/{to}") {
                val user1 = call.parameters["from"]
                val user2 = call.parameters["to"]
                val txn = call.receive<Transaction>()
                println(Transaction)
                call.respond("Doing transfer from $user1 to $user2")
            }

            post("/adduser") {
                try {
                    val formParameters = call.receive<User>()
                    val name = formParameters.name
                    val balance = formParameters.balance
                    val article = dao.insert(name, balance)
                    call.respond("User added successfully")
                } catch (e: Throwable) {
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

            post("/create_user_group") {
                val userGroup = call.receive<List<Int>>()
                val group = grpDao.insert(userGroup)
                call.respondText { "User Group created" }
            }

            post("/transaction/default_split") {
                val txn = call.receive<Transaction>()
                val userList = txn.users
                val payee = txn.payee
                val amount = txn.amt
                val sz = userList.size
                val split = (amount.value) / sz
                val transaction =
                    txnDao.insert(UUID.randomUUID().toString(), userList, payee, amount, txn.description, txn.createdOn)
                for (user in userList) {
                    if (user.userId != payee.userId) {
                        if (dao.getUserbyId(user.userId) == null) {
                            dao.insert(user.name, user.balance)
                        }
                        user.balance -= split
                        dao.updateUserBalance(user.userId, user.balance)
                    }
                }
                call.respond("According to default split everyone pays: ${split}")
            }

            post("/group_transaction") {
                try {
                    val group = call.receive<GroupTxn>()
                    val users = grpDao.getAllUsersInGrp(group.grpId)
                    val payee = group.payee
                    val sz = users.size
                    println("Getting data successfully -->  ${users.size}")
                    val split = (group.amt.value) / sz

                    if (users.isNotEmpty()) {
                        for (user in users) {
                            user?.balance = user?.balance?.minus(split) ?: 0.0
                            dao.updateUserBalance(user?.userId ?: 1, user?.balance ?: 0.0)

                            if (user != null) {
                                val checkExistingDebt = DebtsImp().check(user.userId, payee)
                                println("Result after checking: $checkExistingDebt")
                                if (!checkExistingDebt) {
                                    debtsDao.insert(
                                        debtId = UUID.randomUUID().toString(),
                                        userId = user.userId,
                                        amount = if (user.userId == payee) 0.0 else -split,
                                        owedToId = payee,
                                        groupId = group.grpId
                                    )
                                } else {
                                    debtsDao.update(user.userId, payee, if (user.userId == payee) 0.0 else -split)
                                }
                            }
                        }
                        grpDao.updateTransaction(group.grpId, split)
//                    txnDao.insert(UUID.randomUUID().toString(),users,payee,group.amt.value,"Test","now")

                    }

//                if (users.isNotEmpty()) {
//                    for (user in users) {
//                        user?.balance = user?.balance?.minus(split) ?: 0.0
//                        dao.updateUserBalance(user?.userId ?: 1, user?.balance ?: 0.0)
//
//                        if (user != null) {
//                            if (user.userId == payee) {
//                                debts = debts.plus(payee to (debts.getOrDefault(payee, 0.0) + split)) // Add positive amount for payee
//                            } else {
//                                debts = debts.plus(user.userId to (debts.getOrDefault(user.userId, 0.0) - split)) // Add negative amount for other members
//                            }
//                        }
//                     }
//                    grpDao.updateTransaction(group.grpId, split)
//                }
//                println(debts)
                    call.respond(HttpStatusCode.OK, users)
                } catch (e: Throwable) {
                    application.log.error("Failed Group Transaction", e)
                    call.respond(HttpStatusCode.BadRequest, "Problems making transactions")
                }
            }

            get("/getAllTxn") {
                val txns = txnDao.getAllTxn()
                call.respond(txns.toString())
            }

            get("/getTxnById") {
                try {
                    val req = call.receive<GetTxnReq>()
                    val txnId = req.txnId
                    call.respond(txnDao.getTxnById(txnId).toString())
                } catch (e: NumberFormatException) {
                    call.respondText(
                        "Invalid ID format. Please provide an integer ID.",
                        status = HttpStatusCode.BadRequest
                    )
                }
            }

            post("/splitInGroup/{groupId}") {
                val groupId = call.parameters["groupId"]

                val userinfo = call.receive<UserInfo>()
                println(userinfo)
                call.respondText("Everything Working")
            }
        }
    }
}

