package com.example.project13_allfirebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project13_allfirebase.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbAuth = FirebaseAuth.getInstance()

        val ago = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, ago)

        binding.btnGGIn.setOnClickListener{
            SignInGG()
        }

        binding.btnEmailIn.setOnClickListener{
            var inetnt =Intent(this,LoginEmailActivity::class.java)
            startActivity(inetnt)
        }
    }

    private fun SignInGG() {
        val signIntent = googleSignInClient.signInIntent
        launcher.launch(signIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            hasdlResult(task)
        }else{
            Toast.makeText(this, "Login GG khong thanh cong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasdlResult(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            Toast.makeText(this, "Login GG thanh cong", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this, "Login GG khong thanh cong", Toast.LENGTH_SHORT).show()
        }
    }
}