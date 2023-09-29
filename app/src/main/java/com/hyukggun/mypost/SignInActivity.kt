package com.hyukggun.mypost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.hyukggun.mypost.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private lateinit var auth: FirebaseAuth

    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindActions()

        auth = Firebase.auth
    }

    private fun bindActions() {
        binding.btnCompleteSignIn.setOnClickListener {
            var email = binding.etSignInEmail.text?.toString() ?: ""
            var password = binding.etSignInPW.text?.toString() ?: ""
            val checkPassword = binding.etSignInCheckPW.text?.toString() ?: ""

            if (!Util.isEmailValid(email)) {
                showToast("올바르지 않은 이메일 입니다.")
            }
            if (!isValidPassword(password)) {
                showToast("올바르지 않은 비밀번호입니다.")
            }
            if (password != checkPassword) {
                showToast("비밀번호가 일치하지 않습니다.")
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@SignInActivity) { task ->
                    if (task.isSuccessful) {
                        Log.d("AUTH", "createUserWithEmail:success")
                        user = auth.currentUser
                    } else {
                        Log.e("AUTH", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "create User with Email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return (password.isNotEmpty() && password.length > 6)
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SignInActivity, message, Toast.LENGTH_SHORT).show()
    }

}