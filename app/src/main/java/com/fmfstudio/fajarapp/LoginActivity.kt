package com.fmfstudio.fajarapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity(){
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoogle: SignInButton
    private lateinit var btnRegister: Button

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)
        btnGoogle = findViewById(R.id.btn_google)

        btnRegister.setOnClickListener{
            startActivity(Intent(applicationContext,RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            if(editEmail.text.length > 0 && editPassword.text.length > 0){
                login(editEmail.text.toString(),editPassword.text.toString())
            }
            else{
                Toast.makeText(applicationContext,"Silakan isi semua data",Toast.LENGTH_SHORT).show()
            }
        }

        btnGoogle.setOnClickListener{
            signIn()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var accnt = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(accnt.idToken!!)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential =GoogleAuthProvider.getCredential(idToken, null);
        MainActivity.mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = MainActivity.mAuth.currentUser
                    reload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun signIn() {
        val signInIntent = MainActivity.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }


    private fun reload(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun login(email:String,password:String)
    {
        MainActivity.mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                if (task.result.user != null) {
                    reload()
                } else {
                    Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

}