package com.tugasbesarkotlin5.firechatgo2.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.tugasbesarkotlin5.firechatgo2.R
import com.tugasbesarkotlin5.firechatgo2.model.SignUpModel
import com.tugasbesarkotlin5.firechatgo2.utility.Utility
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*


class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private var mAuth: FirebaseAuth? = null
    private var progressBar: ProgressDialog? = null

    val MY_REQUEST_CODE: Int = 7117
    lateinit var providers : List<AuthUI.IdpConfig>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()
        btn_signUp!!.setOnClickListener(this)

        providers = Arrays.asList<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        //Event
        btn_google.setOnClickListener {
            showSignInOptions()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == MY_REQUEST_CODE) {

            if (requestCode == Activity.RESULT_OK)
            {
                val mDatabase = FirebaseDatabase.getInstance().getReference("User")
                val authUserEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()
                val idUserEmail = SignUpModel("","",authUserEmail,"")
                mDatabase.child(authUserEmail).setValue(idUserEmail).addOnSuccessListener {  }
                val target = Intent(this, LoginActivity::class.java)
                startActivity(target)
                finish()
            }
            else
            {
//                Toast.makeText(this, ""+response!!.error!!.message,Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.myTheme)
                .build(), MY_REQUEST_CODE)

    }

    override fun onClick(v: View?) {
        val name: String = et_nameSignUp.text.toString().trim()
        val number: String = et_numberSignUp.text.toString().trim()
        val email: String = et_emailSignUp!!.text.toString().trim()
        val pass: String = et_passSignUp!!.text.toString().trim()
        if (checkValidation()) {
            if (Utility.isOnline(this)) {
                progressBar = ProgressDialog(this@SignUpActivity, android.app.AlertDialog
                        .THEME_DEVICE_DEFAULT_LIGHT)
                progressBar!!.setMessage("Please wait......")
                progressBar!!.setCancelable(false)
                progressBar!!.show()
                val signUpModel = SignUpModel(name, number, email, pass)
                Log.e("test", "test")
                saveData(signUpModel)
            }
        }
    }

    private fun saveData(signUpModel: SignUpModel) {
        mAuth!!.createUserWithEmailAndPassword(signUpModel.getEmail()!!, signUpModel.getPassword()!!)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e("Success", "Sign up success")
                        saveDataOnFirebase(signUpModel)
                    } else {
                        progressBar!!.dismiss()
                        Log.e("Fail", "Sign up fail")
                    }
                }
    }

    private fun saveDataOnFirebase(signUpModel: SignUpModel) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        var email: String = signUpModel.getEmail()!!
        email = email.replace(".", "")
        myRef.child("User").child(email).setValue(signUpModel).addOnSuccessListener {
            Log.e("saveDataOnFirebase", "saveDataOnFirebase success")
            progressBar!!.dismiss()
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }.addOnFailureListener {
            progressBar!!.dismiss()
            Log.e("saveDataOnFirebase", "saveDataOnFirebase fail")
        }
    }

    private fun checkValidation(): Boolean {
        if (et_nameSignUp.text.toString().trim() == "") {
            Toast.makeText(this, "User name must be require", Toast.LENGTH_SHORT).show()
            return false
        } else if (et_numberSignUp.text.toString().trim() == "") {
            Toast.makeText(this, "Mobile number must be require", Toast.LENGTH_SHORT).show()
        } else if (et_numberSignUp.text.toString().trim().length < 10) {
            Toast.makeText(this, "Mobile number must be of 10 digits", Toast.LENGTH_SHORT).show()
            return false
        } else if (et_emailSignUp!!.text.toString().trim() == "") {
            Toast.makeText(this, "Email id must be require", Toast.LENGTH_SHORT).show()
            return false
        } else if (!Utility.isValidEmail(et_emailSignUp!!.text.toString().trim())) {
            Toast.makeText(this, "Please enter valid email id", Toast.LENGTH_SHORT).show()
            return false
        } else if (et_passSignUp!!.text.toString().trim() == "") {
            Toast.makeText(this, "Password must be require", Toast.LENGTH_SHORT).show()
        } else if (et_passSignUp!!.text.toString().trim().length < 5) {
            Toast.makeText(this, "Password must be 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
