package com.godfrey.codingexercise.editorpage.view


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.godfrey.codingexercise.databinding.ActivityEditorBinding
import com.godfrey.codingexercise.editorpage.viewmodel.EditorViewModel
import com.godfrey.codingexercise.command.Action
import com.godfrey.codingexercise.statpage.view.StatActivity
import com.godfrey.codingexercise.util.Shape


class EditorActivity : AppCompatActivity() {

    private val viewModel = EditorViewModel()
    private lateinit var binding: ActivityEditorBinding
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vto: ViewTreeObserver = binding.EditorCanvasView.viewTreeObserver

        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    binding.EditorCanvasView.viewTreeObserver.removeOnGlobalLayoutListener {  }
                } else {
                    binding.EditorCanvasView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                viewModel.setCanvasSize(Size(binding.EditorCanvasView.measuredWidth, binding.EditorCanvasView.measuredHeight))
            }
        })

        binding.EditorCanvasView.registerTouchListener ( viewModel::didTouchShapeObject )

        setupObserver()
        registerResult()
    }

    private fun registerResult(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val extra = data?.extras?.get("action");
                if(extra != null ){
                    for( shapeID in (extra as ArrayList<Shape>) ){
                        viewModel.receiveClearListAction(shapeID)
                    }
                }
            }
        }
    }


    private fun setupObserver(){
        viewModel.getSender().observe(this, {
            when (it.action) {
                Action.Add -> {
                    binding.EditorCanvasView.updateDrawableList( viewModel.getShapeList())
                }

                Action.UpdateShape ->{
                    binding.EditorCanvasView.invalidate()
                }

                Action.RefreshView ->{
                    binding.EditorCanvasView.invalidate()
                }

                Action.Delete ->{
                    binding.EditorCanvasView.updateDrawableList(viewModel.getShapeList())
                }
            }
        })
    }

    fun getViewModel() : EditorViewModel{
        return viewModel
    }

    fun didPressGenerateShapeButton(btn: View){
        viewModel.didPressGeneratedShape(Integer.parseInt(btn.tag.toString()), this)
    }

    fun didPressUndoButton(btn : View){
        viewModel.didPressUndoAction()
    }

    fun didPressStatButton(btn : View){
        val intent = Intent(this, StatActivity::class.java)
        intent.putExtra("ShapeInfo", viewModel.getNumOfShapeMapTable())
        resultLauncher.launch(intent)
    }

}