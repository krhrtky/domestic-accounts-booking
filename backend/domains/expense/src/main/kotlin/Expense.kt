package com.example.expense

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class Expense(
    @field:NotBlank(message = "経費IDは必須です")
    @field:Size(min = 3, max = 50, message = "経費IDは3文字以上50文字以下で入力してください")
    val id: String,
    
    @field:DecimalMin(value = "0.01", message = "金額は0.01以上で入力してください")
    @field:Digits(integer = 10, fraction = 2, message = "金額は整数部10桁、小数部2桁以下で入力してください")
    val amount: BigDecimal,
    
    @field:NotBlank(message = "説明は必須です")
    @field:Size(max = 500, message = "説明は500文字以下で入力してください")
    val description: String,
    
    @field:Size(max = 100, message = "カテゴリは100文字以下で入力してください")
    val category: String? = null
)
