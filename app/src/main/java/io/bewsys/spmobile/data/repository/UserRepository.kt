package io.bewsys.spmobile.data.repository

import io.bewsys.spmobile.api.model.login.LoginResponse

interface UserRepository {
    fun login(email: String, password: String): LoginResponse
}