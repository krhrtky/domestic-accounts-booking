package com.example.shared.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.math.BigDecimal
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ReasonableAmountValidator::class])
annotation class ReasonableAmount(
    val message: String = "金額が妥当な範囲を超えています（0.01円以上10,000,000円以下）",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class ReasonableAmountValidator : ConstraintValidator<ReasonableAmount, BigDecimal> {
    
    private val minAmount = BigDecimal("0.01")
    private val maxAmount = BigDecimal("10000000.00") // 1千万円
    
    override fun isValid(value: BigDecimal?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true // nullは他のバリデーションで処理
        }
        
        return value >= minAmount && value <= maxAmount
    }
}