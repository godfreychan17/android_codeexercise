package com.godfrey.codingexercise.editorpage.model


import android.util.Size
import com.godfrey.codingexercise.component.ShapeObject
import com.godfrey.codingexercise.command.Command
import com.godfrey.codingexercise.util.Shape
import java.util.*
import kotlin.collections.ArrayList

data class EditorModel (
    var numOfEachShapeMap : HashMap<Shape, Int>,
    var shapeList : ArrayList<ShapeObject>,
    var canvasSize : Size,
    var commandList : ArrayList<Command>
)

