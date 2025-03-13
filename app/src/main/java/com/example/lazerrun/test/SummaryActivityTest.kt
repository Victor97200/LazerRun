package com.example.lazerrun.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.lazerrun.R
import com.example.lazerrun.SummaryActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SummaryActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SummaryActivity::class.java)

    @Test
    fun testSummaryDisplay() {
        // Vérifiez que les vues affichent les valeurs attendues
        onView(withId(R.id.total_time)).check(matches(withText("Total Time: 00:00:000")))
        onView(withId(R.id.run_time)).check(matches(withText("Run Time: 00:00:000")))
        onView(withId(R.id.chrono_tire)).check(matches(withText("Shooting Time: 00:00:000")))
        onView(withId(R.id.avg_speed)).check(matches(withText("Average Speed: 0.0 m/s")))
        onView(withId(R.id.min_shooting_time)).check(matches(withText("Min Shooting Time: 00:00:000")))
        onView(withId(R.id.avg_shooting_time)).check(matches(withText("Avg Shooting Time: 00:00:000")))
        onView(withId(R.id.max_shooting_time)).check(matches(withText("Max Shooting Time: 00:00:000")))
        onView(withId(R.id.missed_shots)).check(matches(withText("Missed Shots: 0")))
    }
}