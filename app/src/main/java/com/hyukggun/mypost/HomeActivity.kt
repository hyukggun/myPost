package com.hyukggun.mypost

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.hyukggun.mypost.databinding.ActivityHomeBinding
import com.google.firebase.storage.ktx.storage
import coil.load

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)
        AppPreference.user?.let {
            binding.tvHomeWelcome.text = "Welcome back ${it.email.toString()}"
        }

        val storage = Firebase.storage

        val storageRef = storage.reference

        val imageRef = storageRef.child("grand-budapest-hotel.jpeg")

        imageRef.downloadUrl.addOnSuccessListener { uri ->
            binding.ivHomeUser.load(uri) {
                listener(
                    onError = { _,_ -> Toast.makeText(this@HomeActivity, "이미지 로딩 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()},
                    onSuccess = { _, _ -> }
                )
            }
        }
        bindAction()
    }

    private fun bindAction() {
        binding.ibPlus.setOnClickListener {
            val mapIntent = Intent(this@HomeActivity, MapActivity::class.java)
            startActivity(mapIntent)
        }
    }
}