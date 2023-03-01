package io.bewsys.spmobile

import android.util.Log
import io.bewsys.spmobile.api.KtorHttpClient
import io.bewsys.spmobile.api.UserApi
import io.bewsys.spmobile.api.model.login.LoginRequest
import io.bewsys.spmobile.api.model.login.LoginResponse
import io.bewsys.spmobile.data.repository.UserRepositoryImpl
import io.bewsys.spmobile.prefsstore.PreferencesManager
import io.bewsys.spmobile.ui.login.LoginViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.kotlin.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTests {

    private val apiMockEngine = ApiMockEngine()
    private val apiMock = KtorHttpClient(apiMockEngine.get())


    @Test
    fun `test production api client `() = runTest {
        val api = KtorHttpClient(HttpClient(Android).engine).getClient()
        val response = api.get("https://ktor.io/")
        assertEquals(200, response.status.value)
    }

    @Test
    fun `test login`() = runTest {
//        val response = apiMock.getClient().get("login")
//        assertEquals(200, response.status.value)

        val userApi = UserApi(apiMock.getClient())
        val response = userApi.login("kobbykolmes@gmail.com", "pass")

        assertEquals(200, response.status.value)
        assertNotNull(response.body())
//        val body = response.body<LoginResponse>().toString()
//        System.out.println(body)

    }


    @Test
    fun `test user repository`() = runTest {


        val mockUserApi = UserApi(apiMock.getClient())
        val userRepo = UserRepositoryImpl(mockUserApi)

        val result = userRepo.login("kobbykolmes@gmail.com", "password").drop(1).first()

        assertNotNull(result)

        val body = result.toString()
        println("results: $body")

    }


}