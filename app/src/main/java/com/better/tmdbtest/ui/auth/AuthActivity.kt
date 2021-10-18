package com.better.tmdbtest.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.better.tmdbtest.databinding.ActivityAuthBinding
import com.better.tmdbtest.ui.main.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var callbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNoLogin.setOnClickListener {
            setResult(RESULT_CANCELED, Intent(this@AuthActivity, MainActivity::class.java))
            finish()
        }

        callbackManager = CallbackManager.Factory.create()

        //Update Facebook user data after login
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    setResult(RESULT_OK, Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                }

                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                }
            })
    }

    //Catch the result from login Facebook page
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {

    }
}