package com.godfrey.codingexercise.statpage.view

import android.content.Intent
import com.godfrey.codingexercise.util.Shape
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.godfrey.codingexercise.R
import com.godfrey.codingexercise.databinding.ActivityStatBinding


class StatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStatBinding

    private var clearActionList = ArrayList<Shape>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val shapeMapInfo : HashMap<Shape, Int>? = intent.getSerializableExtra("ShapeInfo") as HashMap<Shape, Int>?
        if( shapeMapInfo != null){
            setStatInformation(shapeMapInfo)
        }
    }

    fun setStatInformation(shapeMapInfo :HashMap<Shape, Int> ){
        runOnUiThread {
            binding.StatActivitySquareTxtView.text = getString(R.string.state_title_square) +  shapeMapInfo[Shape.Square]
            binding.StatActivityCircleTxtView.text = getString(R.string.state_title_circle) +  shapeMapInfo[Shape.Circle]
            binding.StatActivityTriangleTxtView.text = getString(R.string.state_title_triangle) +  shapeMapInfo[Shape.Triangle]
        }
    }

    fun didPressBackBtn( btn : View){
        val returnIntent = Intent()
        returnIntent.putExtra("action", clearActionList)
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    fun didPressClearAllBtn(btn : View){
        (btn as Button).text = getString(R.string.btn_progress)
        val values = enumValues<Shape>()
        val idx = Integer.parseInt(btn.tag.toString())
        clearActionList.add(0, values[idx])
    }
}