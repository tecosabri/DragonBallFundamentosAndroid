package com.isabri.myapplication.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.Base64

class LoginViewModel: ViewModel() {

    val stateLiveData: MutableLiveData<LoginState> by lazy { MutableLiveData<LoginState>() }
    var token: String = ""


    fun getToken(user: String, password: String) {
        setValueOnMainThread(LoginState.Loading)
        val client = OkHttpClient()
        val url = "https://dragonball.keepcoding.education/api/auth/login"
        val authorization = "$user:$password"
        val base64Authorization = Base64.getEncoder().encodeToString(authorization.toByteArray())
        val requestBody = "".toRequestBody()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Basic $base64Authorization")
            .post(requestBody)
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Error", "Error getting token: ${e.message}")
                    setValueOnMainThread(LoginState.Failure(e.message.toString()))
                }
                override fun onResponse(call: Call, response: Response) {
                    if(response.code != 200) {
                        setValueOnMainThread(LoginState.Failure("${response.code}"))
                        return
                    }

                    Log.d("Network call", "Success getting token")
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        setValueOnMainThread(LoginState.Success(it))
                        token = it
                    }
                }
            }
        )
    }

    fun setValueOnMainThread(value: LoginState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    sealed class LoginState {
        data class Success(val token: String): LoginState()
        data class Failure(val errorMessage: String): LoginState()
        object Loading: LoginState()
    }

}