package com.example.timesage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.timesage.R
import com.example.timesage.databinding.FragmentSingInBinding
import com.example.timesage.databinding.FragmentSingUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SingUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSingUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSingUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }
    private fun init(view: View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents(){
        binding.authTextView.setOnClickListener {
            navControl.navigate(R.id.action_singUpFragment_to_singInFragment)
        }

        binding.RegisterButton.setOnClickListener{
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val pass = binding.editTextTextPassword.text.toString().trim()
            val verifypass = binding.verifyPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && verifypass.isNotEmpty()){
               if (pass == verifypass){
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(context, "Реєстрація успішна", Toast.LENGTH_SHORT).show()
                                navControl.navigate(R.id.action_singUpFragment_to_homeFragment)
                            }else{
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        })
               }
            }

        }

    }
}
