package com.example.api.graphql

import com.expediagroup.graphql.server.operations.Query
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import com.example.shared.validation.SafeString

@Component
@Validated
class UserController : Query, Mutation {
    fun user(id: String): User? {
        return User(id = id)
    }
    
    fun createUser(@Valid input: CreateUserInput): User {
        return User(
            id = input.id,
            name = input.name,
            email = input.email
        )
    }
}

data class User(
    val id: String,
    val name: String? = null,
    val email: String? = null
)

data class CreateUserInput(
    @field:NotBlank(message = "ユーザーIDは必須です")
    @field:Size(min = 3, max = 50, message = "ユーザーIDは3文字以上50文字以下で入力してください")
    @field:SafeString
    val id: String,
    
    @field:NotBlank(message = "ユーザー名は必須です")
    @field:Size(min = 1, max = 100, message = "ユーザー名は1文字以上100文字以下で入力してください")
    @field:SafeString
    val name: String,
    
    @field:Email(message = "正しいメールアドレス形式で入力してください")
    @field:NotBlank(message = "メールアドレスは必須です")
    val email: String
)