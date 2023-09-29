package com.hyukggun.mypost

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.hyukggun.mypost.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.Manifest
import android.content.pm.PackageManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var auth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        bindActions()

        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Log.d("MainActivity", "granted")
            } else {
                Log.d("MainActivity", "not granted")
            }
        }

        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {

            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {

            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun bindActions() {
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this@MainActivity, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }

            if (password.isEmpty()) {
                Toast.makeText(this@MainActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth?.let { auth ->
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                AppPreference.user = auth.currentUser
                                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@MainActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun requestPermission() {
    }
}