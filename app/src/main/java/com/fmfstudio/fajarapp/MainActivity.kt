package com.fmfstudio.fajarapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fmfstudio.fajarapp.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var textEmail: TextView
    private lateinit var textName: TextView
    private lateinit var btnLogout: Button

    companion object{
        lateinit var mAuth: FirebaseAuth
        lateinit var googleSignInClient: GoogleSignInClient
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        textName = findViewById(R.id.name)
        textEmail = findViewById(R.id.email)
        btnLogout = findViewById(R.id.btn_logout)

        if(mAuth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        else{
            textEmail.setText(mAuth.currentUser!!.email)
            textName.setText(mAuth.currentUser!!.displayName)
        }

        btnLogout.setOnClickListener{
            mAuth.signOut()
            googleSignInClient.signOut()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}