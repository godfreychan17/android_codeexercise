package com.godfrey.codingexercise.command

import com.godfrey.codingexercise.component.ShapeObject


data class Command (val action: Action, val shapeList : ArrayList<ShapeObject>?, val previousCommand: Command?) {

    companion object{

        fun addShape( addedShapeList : ArrayList<ShapeObject>) : Command{
            return Command(Action.Add, addedShapeList, null)
        }

        fun changeShape( updateShapeList : ArrayList<ShapeObject> ) : Command{
            return Command(Action.UpdateShape, updateShapeList, null )
        }

        fun deleteShape ( deleteShapeList : ArrayList<ShapeObject>) : Command{
            return Command(Action.Delete, deleteShapeList, null)
        }

        fun refreshView () : Command{
            return Command(Action.RefreshView, null, null)
        }

    }
}