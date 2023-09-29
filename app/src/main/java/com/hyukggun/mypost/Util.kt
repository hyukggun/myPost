package com.hyukggun.mypost

object Util {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)$")
        return emailRegex.matches(email)
    }
}