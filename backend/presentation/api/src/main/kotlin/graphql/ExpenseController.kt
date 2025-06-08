package com.example.api.graphql

import com.expediagroup.graphql.server.operations.Query
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Size
import com.example.shared.validation.SafeString
import com.example.shared.validation.ReasonableAmount
import java.math.BigDecimal

@Component
@Validated
class ExpenseController : Query, Mutation {
    fun expense(id: String): Expense? {
        return Expense(id = id, amount = BigDecimal.ZERO, description = "")
    }
    
    fun createExpense(@Valid input: CreateExpenseInput): Expense {
        return Expense(
            id = input.id,
            amount = input.amount,
            description = input.description,
            category = input.category
        )
    }
}

data class Expense(
    val id: String,
    val amount: BigDecimal,
    val description: String,
    val category: String? = null
)

data class CreateExpenseInput(
    @field:NotBlank(message = "経費IDは必須です")
    @field:Size(min = 3, max = 50, message = "経費IDは3文字以上50文字以下で入力してください")
    @field:SafeString
    val id: String,
    
    @field:DecimalMin(value = "0.01", message = "金額は0.01以上で入力してください")
    @field:Digits(integer = 10, fraction = 2, message = "金額は整数部10桁、小数部2桁以下で入力してください")
    @field:ReasonableAmount
    val amount: BigDecimal,
    
    @field:NotBlank(message = "説明は必須です")
    @field:Size(max = 500, message = "説明は500文字以下で入力してください")
    @field:SafeString
    val description: String,
    
    @field:Size(max = 100, message = "カテゴリは100文字以下で入力してください")
    @field:SafeString
    val category: String? = null
)