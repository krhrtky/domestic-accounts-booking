package com.example.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class User(
    @field:NotBlank(message = "ユーザーIDは必須です")
    @field:Size(min = 3, max = 50, message = "ユーザーIDは3文字以上50文字以下で入力してください")
    val id: String,
    
    @field:NotBlank(message = "ユーザー名は必須です")
    @field:Size(min = 1, max = 100, message = "ユーザー名は1文字以上100文字以下で入力してください")
    val name: String,
    
    @field:Email(message = "正しいメールアドレス形式で入力してください")
    @field:NotBlank(message = "メールアドレスは必須です")
    val email: String? = null
)
