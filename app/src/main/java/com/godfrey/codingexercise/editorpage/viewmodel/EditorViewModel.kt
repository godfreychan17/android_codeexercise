package com.godfrey.codingexercise.editorpage.viewmodel

import android.content.Context
import android.graphics.Point
import android.util.Size
import androidx.lifecycle.MutableLiveData
import com.godfrey.codingexercise.component.ShapeObject
import com.godfrey.codingexercise.editorpage.model.EditorModel
import com.godfrey.codingexercise.command.Action
import com.godfrey.codingexercise.command.Command
import com.godfrey.codingexercise.command.TouchAction
import com.godfrey.codingexercise.util.Shape
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditorViewModel () {

    private val editorModel = EditorModel( hashMapOf( Shape.Square to 0, Shape.Circle to 0, Shape.Triangle to 0),
                                    arrayListOf(),
                                    Size(0,0),
                                    arrayListOf()
                                )

    private val shapeValues = enumValues<Shape>()
    private val sender = MutableLiveData<Command>()

    private fun getRandomPosition() : Point{
        val rand = Random()
        val x = rand.nextInt(editorModel.canvasSize.width - ShapeObject.SHAPE_OBJECT_SIZE)
        val y = rand.nextInt(editorModel.canvasSize.height  - ShapeObject.SHAPE_OBJECT_SIZE)
        return Point(x, y)
    }

    private fun recordPostAction (action : Command){
        editorModel.commandList.add(action)
        sender.postValue(action)
    }

    fun didTouchShapeObject ( shapeObj : ShapeObject, touchAction: TouchAction){

        when(touchAction){

            TouchAction.Press -> {
                val nextShapeID = shapeObj.currentShapeID.nextShape()

                editorModel.numOfEachShapeMap[shapeObj.currentShapeID] =  editorModel.numOfEachShapeMap[shapeObj.currentShapeID]!! - 1
                editorModel.numOfEachShapeMap[nextShapeID] = editorModel.numOfEachShapeMap[nextShapeID]!! + 1

                shapeObj.changeShape(nextShapeID)
                recordPostAction(Command.changeShape( arrayListOf(shapeObj)))

            }

            TouchAction.LongPress ->{
                //delete
                editorModel.numOfEachShapeMap[shapeObj.currentShapeID] = editorModel.numOfEachShapeMap[shapeObj.currentShapeID]!! - 1
                editorModel.shapeList.remove(shapeObj)
                recordPostAction(Command.deleteShape(arrayListOf(shapeObj)))
            }
        }

    }

    fun didPressGeneratedShape(tagID : Int, context: Context){
        val point = getRandomPosition()
        val shapeID = shapeValues[tagID]

        val shapeObj = ShapeObject(context, shapeID,point)

        editorModel.numOfEachShapeMap[shapeID] = editorModel.numOfEachShapeMap[shapeID]!! + 1
        editorModel.shapeList.add(shapeObj)
        recordPostAction(Command.addShape( arrayListOf(shapeObj)))
    }

    fun receiveClearListAction( shapeID: Shape){
        val removeShapeList = editorModel.shapeList.filter { it.currentShapeID == shapeID} as ArrayList<ShapeObject>
        editorModel.shapeList.removeAll(removeShapeList)
        editorModel.numOfEachShapeMap[shapeID] = 0
        recordPostAction(Command.deleteShape(removeShapeList))
    }

    fun didPressUndoAction(){
        if( editorModel.commandList.size >  0){
            val lastCommand = editorModel.commandList.last()

            when (lastCommand.action){
                Action.Add ->{
                    for( shapeObj in lastCommand.shapeList!!){
                        editorModel.numOfEachShapeMap[shapeObj.currentShapeID] = editorModel.numOfEachShapeMap[shapeObj.currentShapeID]!! - 1
                    }
                    editorModel.shapeList.removeAll(lastCommand.shapeList)
                    sender.postValue(Command.refreshView())
                }

                Action.UpdateShape ->{
                    for( shapeObj in lastCommand.shapeList!!){
                        editorModel.numOfEachShapeMap[shapeObj.currentShapeID] = editorModel.numOfEachShapeMap[shapeObj.currentShapeID]!! - 1
                        val rollBackToShape = shapeObj.currentShapeID.previousShape()

                        editorModel.numOfEachShapeMap[rollBackToShape] = editorModel.numOfEachShapeMap[rollBackToShape]!! + 1
                        shapeObj.changeShape(rollBackToShape)
                    }
                    sender.postValue(Command.changeShape(lastCommand.shapeList))
                }

                Action.Delete ->{
                    for( shapeObj in lastCommand.shapeList!!){
                        editorModel.numOfEachShapeMap[shapeObj.currentShapeID] = editorModel.numOfEachShapeMap[shapeObj.currentShapeID]!! + 1
                        editorModel.shapeList.add(shapeObj)
                    }
                    sender.postValue(Command.refreshView())
                }
            }

            editorModel.commandList.removeLast()
        }
    }

    fun setCanvasSize( size : Size){
        editorModel.canvasSize = size
    }


    fun getShapeList() : ArrayList<ShapeObject>{
        return editorModel.shapeList
    }

    fun getNumOfShapeMapTable () : HashMap<Shape, Int>{
        return editorModel.numOfEachShapeMap
    }

    fun getEditorModel() : EditorModel{
        return editorModel
    }


    fun getSender() : MutableLiveData<Command>{
        return sender
    }
}