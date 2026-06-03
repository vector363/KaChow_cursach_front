package com.example.kachow_cursach

import com.example.kachow_cursach.domain.validation.ValidationUtils
import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun isValidEmail_correctEmail_returnsTrue() {
        val result = ValidationUtils.isValidEmail("user@example.com")
        assertTrue(result)
    }

    @Test
    fun isValidEmail_incorrectEmail_returnsFalse() {
        val result = ValidationUtils.isValidEmail("user@")
        assertFalse(result)
    }

    @Test
    fun isValidEmail_emptyEmail_returnsFalse() {
        val result = ValidationUtils.isValidEmail("")
        assertFalse(result)
    }

    @Test
    fun isValidPassword_longerThan6_returnsTrue() {
        val result = ValidationUtils.isValidPassword("password123")
        assertTrue(result)
    }

    @Test
    fun isValidPassword_shorterThan6_returnsFalse() {
        val result = ValidationUtils.isValidPassword("123")
        assertFalse(result)
    }

    @Test
    fun doPasswordsMatch_samePassword_returnsTrue() {
        val result = ValidationUtils.doPasswordsMatch("123456", "123456")
        assertTrue(result)
    }

    @Test
    fun doPasswordsMatch_differentPasswords_returnsFalse() {
        val result = ValidationUtils.doPasswordsMatch("123456", "654321")
        assertFalse(result)
    }
}