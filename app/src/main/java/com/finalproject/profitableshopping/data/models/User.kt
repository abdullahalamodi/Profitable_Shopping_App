package com.finalproject.profitableshopping.data.models

data class User(
    val id: String,
    val email: String,
    val password: String,
    val isActive: Boolean
)