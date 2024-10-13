package utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timesage.databinding.ToDoItemBinding

class ToDoAdapter(private val list:MutableList<ToDoData>) :
RecyclerView.Adapter<ToDoAdapter.ToDoView>(){

    private var listener:ToDoAdapterClick? = null
    fun setListener(listener:ToDoAdapterClick){
        this.listener = listener
    }

    inner class ToDoView(var binding: ToDoItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoView {
        val binding = ToDoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoView(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoView, position: Int) {
        with(holder){
            with(list[position]){
                binding.textView.text = this.task
                binding.imageView.setOnClickListener{
                    listener?.onDeleteTaskClick(this)
                }
                binding.imageView2.setOnClickListener{
                    listener?.onEditTaskClick(this)
                }
            }
        }
    }
    interface ToDoAdapterClick{
        fun onDeleteTaskClick(ToDoData: ToDoData)
        fun onEditTaskClick(ToDoData: ToDoData)
    }
}