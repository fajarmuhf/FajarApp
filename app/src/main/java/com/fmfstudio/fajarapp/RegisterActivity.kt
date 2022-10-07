package com.fmfstudio.fajarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editPasswordConf: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editName = findViewById(R.id.name)
        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        editPasswordConf = findViewById(R.id.password_conf)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)

        mAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener{
            if(editName.text.length > 0 && editEmail.text.length > 0 && editPassword.text.length > 0 && editPasswordConf.text.length > 0){
                if(editPassword.text.toString().equals(editPasswordConf.text.toString())){
                    register(editName.text.toString(),editEmail.text.toString(),editPassword.text.toString())
                }
                else{
                    Toast.makeText(this,"Silakan masukan password yang sama",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Silakan isi semua data",Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener{
            finish()
        }


    }

    private fun reload(){
        startActivity(Intent(applicationContext,MainActivity::class.java))
    }

    private fun register(name:String,email:String,password:String)
    {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            task ->
            if (task.isSuccessful && task.result.user != null) {
                var firebaseUser: FirebaseUser? = task.result.user
                var request:UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(name).build()
                firebaseUser?.updateProfile(request)?.addOnCompleteListener { task ->
                    reload()
                }
            }else {
                Toast.makeText(this, task.exception?.localizedMessage ,Toast.LENGTH_SHORT).show()
            }
        }

    }
}