package com.godfrey.codingexercise

import androidx.test.espresso.Espresso

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.Espresso.*


import com.godfrey.codingexercise.statpage.view.StatActivity
import com.godfrey.codingexercise.util.Shape
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatActivityTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(StatActivity::class.java)

    @Before
    fun beforeTest(){
        val shapeMapInfo = hashMapOf<Shape, Int>( Shape.Square to 3, Shape.Triangle to 4, Shape.Circle to 5)
        activityRule.activity.setStatInformation(shapeMapInfo)
    }

    @Test
    fun testUpdateStat(){

        onView(withId(R.id.StatActivity_squareTxtView))
            .check( matches( withText (activityRule.activity.getString(R.string.state_title_square) + "3")))

        onView(withId(R.id.StatActivity_triangleTxtView))
            .check( matches( withText (activityRule.activity.getString(R.string.state_title_triangle) + "4")))


        onView(withId(R.id.StatActivity_circleTxtView))
            .check( matches( withText (activityRule.activity.getString(R.string.state_title_circle) + "5")))

    }
}