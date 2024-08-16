package com.example.project13_allfirebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project13_allfirebase.databinding.ActivitySignEmailBinding
import com.google.firebase.auth.FirebaseAuth

class SignEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignEmailBinding
    private lateinit var dbAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbAuth = FirebaseAuth.getInstance()

        binding.edtSignE.setText("")
        binding.edtTypeSignPass.setText("")
        binding.edtRetypeSignPass.setText("")

        binding.btnSign.setOnClickListener {
            SignEmailData()
        }
    }

    private fun SignEmailData() {
        var email = binding.edtSignE.text.toString().trim()
        var password = binding.edtTypeSignPass.text.toString().trim()
        var retypePassWord = binding.edtRetypeSignPass.text.toString().trim()

        if(email.isNotEmpty() && password.isNotEmpty() && retypePassWord.isNotEmpty()){
            if(password == retypePassWord){
                dbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {it->
                    if(it.isSuccessful){
                        finish()
                    }else{
                        Toast.makeText(this, "Error Sign !!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            Toast.makeText(this, "Please Enter Infomation !!", Toast.LENGTH_SHORT).show()
        }
    }
}