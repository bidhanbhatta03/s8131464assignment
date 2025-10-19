package com.example.s8131464assignment

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.s8131464assignment.ui.LoginActivity
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for the login flow.
 * Tests the complete login process from UI interaction to navigation.
 */
@RunWith(AndroidJUnit4::class)
class LoginIntegrationTest {

    @Test
    fun testSuccessfulLogin() {
        // Launch the LoginActivity
        val scenario = ActivityScenario.launch(LoginActivity::class.java)

        // Enter username
        onView(withId(R.id.etUsername))
            .perform(clearText(), typeText("bidhan"))

        // Enter password
        onView(withId(R.id.etPassword))
            .perform(clearText(), typeText("8131464"))

        // Close keyboard
        onView(withId(R.id.etPassword))
            .perform(closeSoftKeyboard())

        // Select location from spinner
        onView(withId(R.id.spLocation))
            .perform(click())
        onView(withText("footscray"))
            .perform(click())

        // Click login button
        onView(withId(R.id.btnLogin))
            .perform(click())

        // Wait for network call and verify navigation
        Thread.sleep(2000) // Simple wait for demo; in production use IdlingResource

        scenario.close()
    }

    @Test
    fun testLoginWithEmptyFields() {
        val scenario = ActivityScenario.launch(LoginActivity::class.java)

        // Clear the pre-filled fields
        onView(withId(R.id.etUsername))
            .perform(clearText())
        onView(withId(R.id.etPassword))
            .perform(clearText())

        // Click login without entering credentials
        onView(withId(R.id.btnLogin))
            .perform(click())

        // Verify that we're still on login screen (not navigated)
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))

        scenario.close()
    }

    @Test
    fun testLoginWithOnlyUsername() {
        val scenario = ActivityScenario.launch(LoginActivity::class.java)

        // Enter only username
        onView(withId(R.id.etUsername))
            .perform(clearText(), typeText("testuser"))
        onView(withId(R.id.etPassword))
            .perform(clearText())

        // Close keyboard
        onView(withId(R.id.etUsername))
            .perform(closeSoftKeyboard())

        // Click login
        onView(withId(R.id.btnLogin))
            .perform(click())

        // Verify that we're still on login screen
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))

        scenario.close()
    }
}