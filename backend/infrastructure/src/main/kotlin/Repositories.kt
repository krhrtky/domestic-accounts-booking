package com.example.infrastructure

interface UserRepository {
    fun findAll(): List<String>
}
