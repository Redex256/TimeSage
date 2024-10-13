package com.example.timesage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.timesage.databinding.FragmentAddToDoListBinding
import com.google.firebase.database.core.Tag

import utils.ToDoData

class addToDoListFragment : DialogFragment() {

    private lateinit var binding: FragmentAddToDoListBinding
    private lateinit var listener: DialogAddButtonClick
    private var ToDoData: ToDoData? = null

    fun setListener(listener: DialogAddButtonClick){
        this.listener = listener
    }

    companion object{
        const val TAG = "addToDoListFragment"
        @JvmStatic
        fun newInstance(taskId:String, task: String) = addToDoListFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null ){
            ToDoData = ToDoData(arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString())
            binding.todotext.setText(ToDoData?.task)
        }
        registerEvent()
    }
    private fun registerEvent(){
        binding.cancelButton.setOnClickListener{
            dismiss()
        }
        binding.addButton.setOnClickListener{
            val todoTask = binding.todotext.text.toString()
            if(todoTask.isNotEmpty()){
                if (ToDoData == null){
                    listener.onSaveTask(todoTask, binding.todotext)
                }else{
                    ToDoData?.task = todoTask
                    listener.onEditTask(ToDoData!!, binding.todotext)
                }
            }else{
                Toast.makeText(context, "Будь ласка додайне текст", Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface DialogAddButtonClick{
        fun onSaveTask(todo : String, todotext : EditText)
        fun onEditTask(ToDoData: ToDoData, todotext : EditText)

    }
}