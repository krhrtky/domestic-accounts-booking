package com.example.api.graphql

import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Component

@Component
class UserController : Query {
    fun user(id: String): User? {
        return User(id = id)
    }
}

data class User(
    val id: String
)