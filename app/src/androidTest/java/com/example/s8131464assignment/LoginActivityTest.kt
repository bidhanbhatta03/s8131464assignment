package com.example.s8131464assignment

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun loginActivity_displaysCorrectElements() {
        // Launch the activity
        ActivityScenario.launch(LoginActivity::class.java)

        // Check if login button is displayed
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))

        // Check if username field is displayed
        onView(withId(R.id.etUsername))
            .check(matches(isDisplayed()))

        // Check if password field is displayed
        onView(withId(R.id.etPassword))
            .check(matches(isDisplayed()))

        // Check if location spinner is displayed
        onView(withId(R.id.spLocation))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginActivity_canEnterCredentials() {
        // Launch the activity
        ActivityScenario.launch(LoginActivity::class.java)

        // Enter username
        onView(withId(R.id.etUsername))
            .perform(typeText("testuser"))

        // Enter password
        onView(withId(R.id.etPassword))
            .perform(typeText("testpass"))

        // Verify text was entered
        onView(withId(R.id.etUsername))
            .check(matches(withText("testuser")))

        onView(withId(R.id.etPassword))
            .check(matches(withText("testpass")))
    }

    @Test
    fun loginActivity_canClickLoginButton() {
        // Launch the activity
        ActivityScenario.launch(LoginActivity::class.java)

        // Click login button
        onView(withId(R.id.btnLogin))
            .perform(click())

        // Button should be clickable (no crash)
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))
    }
}
