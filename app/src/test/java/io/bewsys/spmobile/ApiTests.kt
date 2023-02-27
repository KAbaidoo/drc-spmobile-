package io.bewsys.spmobile

import io.bewsys.spmobile.api.Api
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTests {

    private val apiMockEngine = ApiMockEngine()
    private val apiMock = Api(apiMockEngine.get())

    @Test
    fun `test login`() = runTest {

        val response = apiMock.getClient().get("/login")
        assertEquals(200, response.status.value)
    }

    @Test
    fun `test production api client `() = runTest {

        val api = Api(HttpClient(Android).engine).getClient()
        val response = api.get("https://ktor.io/")


        assertEquals(200, response.status.value)
    }
}