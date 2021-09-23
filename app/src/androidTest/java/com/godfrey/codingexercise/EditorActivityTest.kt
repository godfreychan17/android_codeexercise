package com.godfrey.codingexercise

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.godfrey.codingexercise.editorpage.view.EditorActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import com.godfrey.codingexercise.command.TouchAction
import com.godfrey.codingexercise.util.Shape

import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class EditorActivityTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(EditorActivity::class.java)

    @Test
    fun testPlaceSquare(){
        onView(withId(R.id.EditorActivity_SquareBtn))
            .perform( click())

        assertEquals(1, activityRule.activity.getViewModel().getShapeList().count())
        assertEquals(Shape.Square, activityRule.activity.getViewModel().getShapeList()[0].currentShapeID)
    }

    @Test
    fun testPlaceCircle(){
        onView(withId(R.id.EditorActivity_CircleBtn))
            .perform( click())

        assertEquals(1, activityRule.activity.getViewModel().getShapeList().count())
        assertEquals(Shape.Circle, activityRule.activity.getViewModel().getShapeList()[0].currentShapeID)
    }

    @Test
    fun testPlaceTriangle(){
        onView(withId(R.id.EditorActivity_TriangleBtn))
            .perform( click())

        assertEquals(1, activityRule.activity.getViewModel().getShapeList().count())
        assertEquals(Shape.Triangle, activityRule.activity.getViewModel().getShapeList()[0].currentShapeID)
    }

    @Test
    fun testUndo(){

        onView(withId(R.id.EditorActivity_CircleBtn))
            .perform( click())
        val shapeObject = activityRule.activity.getViewModel().getShapeList()[0]

        //Test is undo change shape work
        activityRule.activity.getViewModel().didTouchShapeObject(shapeObject, TouchAction.Press)
        assertEquals(Shape.Triangle, shapeObject.currentShapeID)
        onView(withId(R.id.EditorActivity_UndoBtn))
            .perform( click())
        assertEquals(Shape.Circle, shapeObject.currentShapeID)

        //Test is undo change delete shape work
        activityRule.activity.getViewModel().didTouchShapeObject(shapeObject, TouchAction.LongPress)
        assertEquals(0, activityRule.activity.getViewModel().getShapeList().count())
        onView(withId(R.id.EditorActivity_UndoBtn))
            .perform( click())
        assertEquals(1, activityRule.activity.getViewModel().getShapeList().count())

        //Test undo add shape action
        onView(withId(R.id.EditorActivity_UndoBtn))
            .perform( click())
        assertEquals(0, activityRule.activity.getViewModel().getShapeList().count())
    }

    @Test
    fun testShapeObjectInterAction(){
        onView(withId(R.id.EditorActivity_CircleBtn))
            .perform( click())
        val shapeObject = activityRule.activity.getViewModel().getShapeList()[0]

        activityRule.activity.getViewModel().didTouchShapeObject(shapeObject, TouchAction.Press)
        assertEquals(Shape.Triangle, shapeObject.currentShapeID)

        activityRule.activity.getViewModel().didTouchShapeObject(shapeObject, TouchAction.Press)
        assertEquals(Shape.Square, shapeObject.currentShapeID)

        activityRule.activity.getViewModel().didTouchShapeObject(shapeObject, TouchAction.Press)
        assertEquals(Shape.Circle, shapeObject.currentShapeID)

        activityRule.activity.getViewModel().didTouchShapeObject(shapeObject, TouchAction.LongPress)
        assertEquals(0, activityRule.activity.getViewModel().getShapeList().count())
    }

    @Test
    fun testClearAllShapeAction(){
        //preform add shape
        for( i in 0 until 3 ){
            onView(withId(R.id.EditorActivity_SquareBtn))
                .perform( click())

            onView(withId(R.id.EditorActivity_CircleBtn))
                .perform( click())

            onView(withId(R.id.EditorActivity_TriangleBtn))
                .perform( click())
        }

        assertEquals(3, activityRule.activity.getViewModel().getShapeList().filter { it.currentShapeID == Shape.Circle }.count())
        assertEquals(3, activityRule.activity.getViewModel().getShapeList().filter { it.currentShapeID == Shape.Square }.count())
        assertEquals(3, activityRule.activity.getViewModel().getShapeList().filter { it.currentShapeID == Shape.Triangle }.count())


        activityRule.activity.getViewModel().receiveClearListAction(Shape.Circle)
        assertEquals(0,  activityRule.activity.getViewModel().getNumOfShapeMapTable()[Shape.Circle])

        activityRule.activity.getViewModel().receiveClearListAction(Shape.Square)
        assertEquals(0,  activityRule.activity.getViewModel().getNumOfShapeMapTable()[Shape.Square])

        activityRule.activity.getViewModel().receiveClearListAction(Shape.Triangle)
        assertEquals(0,  activityRule.activity.getViewModel().getNumOfShapeMapTable()[Shape.Triangle])

    }
}