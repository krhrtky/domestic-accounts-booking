package com.example.api.graphql

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.math.BigDecimal

class GraphQLValidationTest {
    
    private lateinit var validator: Validator
    
    @BeforeEach
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }
    
    @Test
    fun `CreateUserInput should validate successfully with valid data`() {
        val validInput = CreateUserInput(
            id = "user123",
            name = "Test User",
            email = "test@example.com"
        )
        
        val violations = validator.validate(validInput)
        assertTrue(violations.isEmpty(), "Valid input should not have validation errors")
    }
    
    @Test
    fun `CreateUserInput should reject invalid email format`() {
        val invalidInput = CreateUserInput(
            id = "user123",
            name = "Test User",
            email = "invalid-email"
        )
        
        val violations = validator.validate(invalidInput)
        assertTrue(violations.isNotEmpty(), "Invalid email format should be rejected")
        assertTrue(
            violations.any { it.message.contains("正しいメールアドレス形式") },
            "Should contain email format error message"
        )
    }
    
    @Test
    fun `CreateUserInput should reject blank required fields`() {
        val invalidInput = CreateUserInput(
            id = "",
            name = "",
            email = ""
        )
        
        val violations = validator.validate(invalidInput)
        assertTrue(violations.size >= 3, "Should have validation errors for all blank fields")
    }
    
    @Test
    fun `CreateUserInput should reject SQL injection attempts`() {
        val maliciousInput = CreateUserInput(
            id = "user'; DROP TABLE users; --",
            name = "admin' OR '1'='1",
            email = "test@example.com"
        )
        
        val violations = validator.validate(maliciousInput)
        assertTrue(violations.isNotEmpty(), "SQL injection attempts should be rejected")
        assertTrue(
            violations.any { it.message.contains("危険な文字列") },
            "Should contain dangerous string error message"
        )
    }
    
    @Test
    fun `CreateUserInput should reject XSS attempts`() {
        val xssInput = CreateUserInput(
            id = "user123",
            name = "<script>alert('XSS')</script>",
            email = "test@example.com"
        )
        
        val violations = validator.validate(xssInput)
        assertTrue(violations.isNotEmpty(), "XSS attempts should be rejected")
        assertTrue(
            violations.any { it.message.contains("危険な文字列") },
            "Should contain dangerous string error message"
        )
    }
    
    @Test
    fun `CreateExpenseInput should validate successfully with valid data`() {
        val validInput = CreateExpenseInput(
            id = "exp123",
            amount = BigDecimal("100.50"),
            description = "Valid expense description",
            category = "食費"
        )
        
        val violations = validator.validate(validInput)
        assertTrue(violations.isEmpty(), "Valid input should not have validation errors")
    }
    
    @Test
    fun `CreateExpenseInput should reject negative amounts`() {
        val invalidInput = CreateExpenseInput(
            id = "exp123",
            amount = BigDecimal("-100.00"),
            description = "Invalid negative amount",
            category = "食費"
        )
        
        val violations = validator.validate(invalidInput)
        assertTrue(violations.isNotEmpty(), "Negative amounts should be rejected")
    }
    
    @Test
    fun `CreateExpenseInput should reject unreasonably large amounts`() {
        val invalidInput = CreateExpenseInput(
            id = "exp123",
            amount = BigDecimal("50000000.00"), // 5000万円
            description = "Unreasonably large amount",
            category = "食費"
        )
        
        val violations = validator.validate(invalidInput)
        assertTrue(violations.isNotEmpty(), "Unreasonably large amounts should be rejected")
        assertTrue(
            violations.any { it.message.contains("妥当な範囲を超えています") },
            "Should contain reasonable amount error message"
        )
    }
    
    @Test
    fun `CreateExpenseInput should reject malicious description content`() {
        val maliciousInput = CreateExpenseInput(
            id = "exp123",
            amount = BigDecimal("100.00"),
            description = "'; DELETE FROM expenses; --",
            category = "食費"
        )
        
        val violations = validator.validate(maliciousInput)
        assertTrue(violations.isNotEmpty(), "Malicious content should be rejected")
        assertTrue(
            violations.any { it.message.contains("危険な文字列") },
            "Should contain dangerous string error message"
        )
    }
    
    @Test
    fun `CreateExpenseInput should accept maximum valid amount`() {
        val validInput = CreateExpenseInput(
            id = "exp123",
            amount = BigDecimal("10000000.00"), // 1000万円（上限）
            description = "Maximum valid amount",
            category = "大きな支出"
        )
        
        val violations = validator.validate(validInput)
        assertTrue(violations.isEmpty(), "Maximum valid amount should be accepted")
    }
    
    @Test
    fun `CreateExpenseInput should accept minimum valid amount`() {
        val validInput = CreateExpenseInput(
            id = "exp123",
            amount = BigDecimal("0.01"), // 1円（下限）
            description = "Minimum valid amount",
            category = "小額"
        )
        
        val violations = validator.validate(validInput)
        assertTrue(violations.isEmpty(), "Minimum valid amount should be accepted")
    }
}