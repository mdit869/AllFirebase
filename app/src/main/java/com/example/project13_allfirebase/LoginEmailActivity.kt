package com.example.project13_allfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project13_allfirebase.databinding.ActivityLoginEmailBinding
import com.google.firebase.auth.FirebaseAuth

class LoginEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginEmailBinding
    private lateinit var dbAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtSign.setOnClickListener {
            val intent = Intent(this, SignEmailActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginEmail.setOnClickListener {
            dbAuth = FirebaseAuth.getInstance()
            var email = binding.edtTypeSignEmail.text.toString()
            var password = binding.edtTypeSignPass.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                dbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{it->
                    if(it.isSuccessful){
                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Login Successfull !!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Login Pass Fail !!", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Please Enter Infomation !!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}