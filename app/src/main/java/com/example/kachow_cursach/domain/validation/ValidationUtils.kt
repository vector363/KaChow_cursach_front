package com.example.kachow_cursach.domain.validation

import java.util.regex.Pattern


object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        )
        return emailPattern.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun doPasswordsMatch(password: String, repeatPassword: String): Boolean {
        return password == repeatPassword
    }
}