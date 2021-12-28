package com.example.firebaseexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.firebaseexample.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setLifecycleOwner {
            binding.executePendingBindings()
            lifecycle
        }
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        intent.extras?.apply {
            binding.textViewTest.text = keySet().joinToString("\n") { it + ": " + get(it) }
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(
                    TAG_FIREBASE_MESSAGE,
                    "Fetch FCM registration token failed",
                    task.exception
                )
            }
            val token = task.result
            Log.e(TAG_FIREBASE_MESSAGE, token.toString())
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.run {
            Log.e(
                TAG_FIREBASE_MESSAGE, "new Intent " +
                keySet().joinToString("\n") { it + ": " + get(it) } ?: "")
            binding.textViewTest.text = keySet().joinToString("\n") { it + ": " + get(it) }
        }
    }

    companion object {
        const val TAG_FIREBASE_MESSAGE = "FIREBASE MESSAGE"
    }
}