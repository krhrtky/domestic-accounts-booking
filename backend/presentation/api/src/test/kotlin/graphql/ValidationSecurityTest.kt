package com.example.api.graphql

import com.example.shared.validation.SafeStringValidator
import com.example.shared.validation.ReasonableAmountValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal

class ValidationSecurityTest {
    
    private val safeStringValidator = SafeStringValidator()
    private val reasonableAmountValidator = ReasonableAmountValidator()
    
    @Test
    fun `SafeStringValidator should reject SQL injection attempts`() {
        val maliciousInputs = listOf(
            "'; DROP TABLE users; --",
            "admin' OR '1'='1",
            "UNION SELECT * FROM users",
            "INSERT INTO users VALUES",
            "UPDATE users SET password",
            "DELETE FROM users WHERE",
            "CREATE TABLE malicious",
            "ALTER TABLE users",
            "EXEC xp_cmdshell"
        )
        
        maliciousInputs.forEach { input ->
            assertFalse(
                safeStringValidator.isValid(input, null),
                "SQLインジェクション攻撃パターンが検出されませんでした: $input"
            )
        }
    }
    
    @Test
    fun `SafeStringValidator should reject XSS attempts`() {
        val xssInputs = listOf(
            "<script>alert('XSS')</script>",
            "<script src='evil.js'></script>",
            "javascript:alert('XSS')",
            "onload=alert('XSS')",
            "onclick=alert('XSS')",
            "onerror=alert('XSS')"
        )
        
        xssInputs.forEach { input ->
            assertFalse(
                safeStringValidator.isValid(input, null),
                "XSS攻撃パターンが検出されませんでした: $input"
            )
        }
    }
    
    @Test
    fun `SafeStringValidator should reject command injection attempts`() {
        val commandInjectionInputs = listOf(
            "test | rm -rf /",
            "\$(whoami)",
            "test; cat /etc/passwd",
            "eval(maliciousCode)"
        )
        
        commandInjectionInputs.forEach { input ->
            assertFalse(
                safeStringValidator.isValid(input, null),
                "コマンドインジェクション攻撃パターンが検出されませんでした: $input"
            )
        }
    }
    
    @Test
    fun `SafeStringValidator should accept legitimate inputs`() {
        val legitimateInputs = listOf(
            "正常なユーザー名",
            "Normal User Name",
            "user@example.com",
            "食料品",
            "Grocery Shopping",
            "123456",
            "Test-Description_123"
        )
        
        legitimateInputs.forEach { input ->
            assertTrue(
                safeStringValidator.isValid(input, null),
                "正当な入力が拒否されました: $input"
            )
        }
    }
    
    @Test
    fun `ReasonableAmountValidator should reject unreasonable amounts`() {
        val unreasonableAmounts = listOf(
            BigDecimal("0.00"),          // 0円
            BigDecimal("-1.00"),         // 負の値
            BigDecimal("10000001.00"),   // 1千万円超
            BigDecimal("999999999.99")   // 極端に大きな値
        )
        
        unreasonableAmounts.forEach { amount ->
            assertFalse(
                reasonableAmountValidator.isValid(amount, null),
                "不正な金額が許可されました: $amount"
            )
        }
    }
    
    @Test
    fun `ReasonableAmountValidator should accept reasonable amounts`() {
        val reasonableAmounts = listOf(
            BigDecimal("0.01"),          // 最小値
            BigDecimal("100.00"),        // 通常の金額
            BigDecimal("10000.00"),      // 1万円
            BigDecimal("10000000.00")    // 最大値（1千万円）
        )
        
        reasonableAmounts.forEach { amount ->
            assertTrue(
                reasonableAmountValidator.isValid(amount, null),
                "正当な金額が拒否されました: $amount"
            )
        }
    }
    
    @Test
    fun `Validators should handle null values appropriately`() {
        // nullは他のバリデーションで処理されるため、カスタムバリデーターではtrueを返す
        assertTrue(safeStringValidator.isValid(null, null))
        assertTrue(reasonableAmountValidator.isValid(null, null))
    }
    
    @Test
    fun `SafeStringValidator should handle empty strings`() {
        // 空文字列は他のバリデーションで処理されるため、trueを返す
        assertTrue(safeStringValidator.isValid("", null))
        assertTrue(safeStringValidator.isValid("   ", null)) // 空白のみ
    }
}