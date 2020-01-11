package com.tugasbesarkotlin5.firechatgo2.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.common.SignInButton
import com.rengwuxian.materialedittext.MaterialEditText
import com.tugasbesarkotlin5.firechatgo2.R
import com.tugasbesarkotlin5.firechatgo2.utility.Utility
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    internal lateinit var login: Button
    private var signUp: TextView? = null
    private lateinit var emailEt: MaterialEditText
    private var passwordEt: MaterialEditText? = null
    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth!!.currentUser
        if (currentUser != null) {
            Log.e("email ", "" + currentUser.email)
            val i = Intent(baseContext, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        Log.e("mAuth", "mAuth")
        this.findViewById()
        signUp!!.setOnClickListener(this)
        login.setOnClickListener(this)

        val signin = findViewById<View>(R.id.sign_in_button) as SignInButton

        signin.setOnClickListener {
            
        }



    }




    private fun findViewById() {
        login = findViewById(R.id.btn_signIn)
        signUp = findViewById(R.id.btn_signUpActivity)
        passwordEt = findViewById(R.id.et_passwordLogin)
        emailEt = findViewById(R.id.et_emailLogin)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_signIn -> {
                val email: String = emailEt.text.toString().trim()
                val password: String = passwordEt!!.text.toString().trim()
                if (checkValidation(email, password)) {
                    progressBar = ProgressDialog(this@LoginActivity, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    progressBar!!.setMessage("Please wait......")
                    progressBar!!.setCancelable(false)
                    progressBar!!.show()
                    userLogin(email, password)
                }
            }
            R.id.btn_signUpActivity -> {
                val i = Intent(baseContext, SignUpActivity::class.java)
                startActivity(i)
            }
        }
    }

    private fun checkValidation(email: String, password: String): Boolean {
        if (email == "") {
            Toast.makeText(this, "Email id must be require", Toast.LENGTH_SHORT).show()
            return false
        } else if (!Utility.isValidEmail(email)) {
            Toast.makeText(this, "Please enter valid email id", Toast.LENGTH_SHORT).show()
            return false
        } else if (password == "") {
            Toast.makeText(this, "Password must be require", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.length < 5) {
            Toast.makeText(this, "Password must be 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun userLogin(email: String, password: String) {
        auth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e("userLogin", "success")
                        progressBar!!.dismiss()
                        val i = Intent(baseContext, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        progressBar!!.dismiss()
                        Toast.makeText(this, "Please enter correct email and password", Toast.LENGTH_SHORT).show()
                        Log.e("userLogin", "fail ")
                    }
                }
    }
}
