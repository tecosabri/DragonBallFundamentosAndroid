package com.isabri.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.isabri.myapplication.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeFetchingState()
        setListeners()
    }


    private fun setListeners() {
        with(binding) {
            bLogin.setOnClickListener {
                val user: String = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                viewModel.getToken(user, password)
            }
        }
    }

    private fun observeFetchingState() {
        viewModel.stateLiveData.observe(this) {
            when(it) {
                is LoginViewModel.LoginState.Success -> {
                    binding.pbLogin.visibility = View.INVISIBLE
                    Log.d("MyLog", "Success getting token: ${viewModel.token}")
                    val intent = Intent(this@LoginActivity, BattleGroundActivity::class.java)
                    intent.putExtra("token", viewModel.token)
                    startActivity(intent)
                }
                is LoginViewModel.LoginState.Failure -> {
                    Log.d("MyLog", "${it.errorMessage}")
                    binding.pbLogin.visibility = View.INVISIBLE
                    Toast.makeText(this, "Error while login: ${it.errorMessage}", Toast.LENGTH_LONG).show()
                }
                is LoginViewModel.LoginState.Loading -> {
                    binding.pbLogin.visibility = View.VISIBLE
                }
            }
        }
    }


}