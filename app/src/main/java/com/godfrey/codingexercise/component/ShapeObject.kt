package com.godfrey.codingexercise.component

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import com.godfrey.codingexercise.util.Shape

class ShapeObject {

    companion object{
        const val SHAPE_OBJECT_SIZE = 100
    }

    var currentShapeID : Shape = Shape.Square
    lateinit var drawable : Drawable
    var context : Context
    var point  = Point(0,0)


    constructor(context: Context, shapID : Shape, pos:Point){
        this.context = context
        point = pos
        changeShape(shapID)
    }

    fun changeShape( shapeID : Shape){
        drawable = this.context.getDrawable(shapeID.resID)!!
        currentShapeID = shapeID
    }

    fun isTouchObject( touchPointX : Float, touchPointY : Float) : Boolean{
        if( touchPointX >= point.x &&
            touchPointX <= point.x + SHAPE_OBJECT_SIZE &&
            touchPointY >= point.y &&
            touchPointY <= point.y + SHAPE_OBJECT_SIZE){
            return true
        }
        return false
    }
}

