package com.godfrey.codingexercise.util

import com.godfrey.codingexercise.R

enum class Shape(val id:String, val resID:Int) {

    Square("0", R.drawable.shape_square),
    Circle("1", R.drawable.shape_circle),
    Triangle("2", R.drawable.shape_triangle);

    fun nextShape() : Shape{
        val values = enumValues<Shape>()
        val nextValue = (this.ordinal + 1) % values.size
        return values[nextValue]
    }

    fun previousShape () : Shape{
        val values = enumValues<Shape>()
        var previousValue = (this.ordinal - 1)
        if( previousValue < 0){
            previousValue = values.size - 1
        }
        return  values[previousValue]
    }

}