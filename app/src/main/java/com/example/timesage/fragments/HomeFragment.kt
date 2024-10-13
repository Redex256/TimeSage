package com.example.timesage.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timesage.R
import com.example.timesage.databinding.FragmentHomeBinding
import com.example.timesage.databinding.FragmentSingUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import utils.ToDoAdapter
import utils.ToDoData



class HomeFragment : Fragment(), addToDoListFragment.DialogAddButtonClick,
    ToDoAdapter.ToDoAdapterClick {

    private lateinit var auth: FirebaseAuth
    private lateinit var databeseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private var todopop: addToDoListFragment? = null
    private lateinit var ToDoAdapter: ToDoAdapter
    private lateinit var mList:MutableList<ToDoData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFireBase()
        registerEvent()
    }

    private fun registerEvent(){
        binding.btnPomodorro.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_timerFragment)
        }
        binding.addButton.setOnClickListener{
            if (todopop != null){
                childFragmentManager.beginTransaction().remove(todopop!!).commit()
            }
            todopop = addToDoListFragment()
            todopop!!.setListener(this)
            todopop!!.show(
                childFragmentManager,
                addToDoListFragment.TAG
            )
        }
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databeseRef = FirebaseDatabase.getInstance().reference.child("Task")
            .child(auth.currentUser?.uid.toString())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        ToDoAdapter = ToDoAdapter(mList)
        ToDoAdapter.setListener(this)
        binding.recyclerView.adapter = ToDoAdapter
    }

    private fun getDataFromFireBase(){
        databeseRef.addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children){
                    val ToDoTask = taskSnapshot.key?.let {
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if (ToDoTask != null){
                        mList.add(ToDoTask)
                    }
                }
                ToDoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
    override fun onSaveTask(todo: String, todotext: EditText) {
        databeseRef.push().setValue(todo).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "todo save", Toast.LENGTH_SHORT).show()
                todotext.text = null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }

            todopop!!.dismiss()
        }
    }

    override fun onEditTask(ToDoData: ToDoData, todotext: EditText) {
        val map = HashMap<String, Any>()
        map[ToDoData.taskId] = ToDoData.task
        databeseRef.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "Edit successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todotext.text = null
            todopop!!.dismiss()
        }
    }

    override fun onDeleteTaskClick(ToDoData: ToDoData) {
        databeseRef.child(ToDoData.taskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "delete successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskClick(ToDoData: ToDoData) {
       if(todopop != null)
           childFragmentManager.beginTransaction().remove(todopop!!).commit()
        todopop = addToDoListFragment.newInstance(ToDoData.taskId, ToDoData.task)
        todopop!!.setListener(this)
        todopop!!.show(childFragmentManager, addToDoListFragment.TAG)
    }



}

