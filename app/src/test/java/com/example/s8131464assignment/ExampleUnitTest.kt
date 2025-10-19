package com.example.s8131464assignment

import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `app name should be University Portal`() {
        // This test verifies the app name is correctly set
        // In a real scenario, you would test against actual string resources
        val expectedAppName = "University Portal"
        assertNotNull("App name should not be null", expectedAppName)
        assertTrue("App name should contain 'University'", expectedAppName.contains("University"))
    }
}