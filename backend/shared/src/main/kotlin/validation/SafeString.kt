package com.example.shared.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SafeStringValidator::class])
annotation class SafeString(
    val message: String = "危険な文字列が含まれています",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class SafeStringValidator : ConstraintValidator<SafeString, String> {
    
    private val dangerousPatterns = listOf(
        // SQLインジェクション対策
        Regex("(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute)\\s+", RegexOption.IGNORE_CASE),
        // XSS対策
        Regex("<script[^>]*>.*?</script>", RegexOption.DOT_MATCHES_ALL),
        Regex("javascript:", RegexOption.IGNORE_CASE),
        Regex("on\\w+\\s*=", RegexOption.IGNORE_CASE),
        // その他の危険なパターン
        Regex("\\|", RegexOption.IGNORE_CASE), // パイプ文字
        Regex("\\$\\(", RegexOption.IGNORE_CASE), // コマンド実行
        Regex("eval\\s*\\(", RegexOption.IGNORE_CASE)
    )
    
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrEmpty()) {
            return true // 空文字列は他のバリデーションで処理
        }
        
        return dangerousPatterns.none { pattern ->
            pattern.containsMatchIn(value)
        }
    }
}