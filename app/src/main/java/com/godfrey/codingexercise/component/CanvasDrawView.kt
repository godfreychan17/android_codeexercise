package com.godfrey.codingexercise.component

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*
import android.os.Handler
import com.godfrey.codingexercise.command.TouchAction

class CanvasDrawView : View {

    private var shapeObjectLsit = ArrayList<ShapeObject>()
    private lateinit var touchListener : (ShapeObject, TouchAction) -> Unit

    //private var pressDownTime  = 0L
    private val COUNT_AS_LONG_PRESS = 500L

    private var isSingleTouch = false
    private var lastTouchPosX = 0f
    private var lastTouchPosY = 0f

    private val myHandler = Handler()
    private val longTouchRunnable = Runnable {
        val touchObj = getTouchObject(lastTouchPosX, lastTouchPosY)
        if( touchObj != null && touchListener != null){
            isSingleTouch = false
            touchListener(touchObj, TouchAction.LongPress)
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun registerTouchListener ( touchListener : ( touchObj : ShapeObject, TouchAction) -> Unit){
        this.touchListener = touchListener
    }

    fun updateDrawableList(objectList: ArrayList<ShapeObject>){
        shapeObjectLsit = objectList
        invalidate()
    }

    private fun getTouchObject ( posX : Float, posY : Float) : ShapeObject?{
        for( shapeObj in shapeObjectLsit){
            if( shapeObj.isTouchObject( posX, posY)){
                return shapeObj
            }
        }
        return null
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if( canvas != null){
            for( obj in shapeObjectLsit){
                val drawable = obj.drawable
                drawable.setBounds(obj.point.x, obj.point.y,
                    obj.point.x + ShapeObject.SHAPE_OBJECT_SIZE,
                    obj.point.y + ShapeObject.SHAPE_OBJECT_SIZE);//R.dimen.ShapeSize, R.dimen.ShapeSize)
                drawable.draw(canvas)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if( event != null){
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    myHandler.postDelayed(longTouchRunnable, COUNT_AS_LONG_PRESS)
                    isSingleTouch = true;
                    lastTouchPosX = event.x
                    lastTouchPosY = event.y
                }

                MotionEvent.ACTION_MOVE ->{
                    myHandler.removeCallbacks(longTouchRunnable)
                    myHandler.postDelayed(longTouchRunnable, COUNT_AS_LONG_PRESS)
                    isSingleTouch = true;
                    lastTouchPosX = event.x
                    lastTouchPosY = event.y
                }

                MotionEvent.ACTION_UP ->{
                    myHandler.removeCallbacks(longTouchRunnable)
                    if( isSingleTouch && touchListener != null) {
                        val touchObj = getTouchObject(event.x, event.y)
                        if( touchObj != null){
                            touchListener(touchObj, TouchAction.Press)
                        }
                    }
                }
            }
        }
        return true
    }
}