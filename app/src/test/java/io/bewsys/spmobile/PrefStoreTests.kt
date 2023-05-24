package io.bewsys.spmobile

import android.app.Application
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class PrefStoreTests {

    @Mock
    lateinit var mockContext: Application
    lateinit var prefStore: PreferencesManager


    @Before
    fun `init mocks`() {
        mockContext = mock()
        prefStore = PreferencesManager(mockContext)
    }

    @Test
    fun `test prefstore`() = runTest {
        val TEST_NAME = "Test name";
        val TEST_STATUS = true;




        prefStore.setLoggedIn(TEST_STATUS)
//        prefStore.saveUser(TEST_NAME)

        prefStore.preferencesFlow.collect {
            assertEquals(true, it.isLoggedIn)
            assertEquals("Test name", it.name)
        }


    }
}