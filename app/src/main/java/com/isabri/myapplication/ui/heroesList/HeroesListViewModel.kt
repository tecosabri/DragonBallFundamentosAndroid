package com.isabri.myapplication.ui.heroesList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.isabri.myapplication.domain.models.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException




class HeroesListViewModel: ViewModel() {

    val stateLiveData: MutableLiveData<HeroesListState> by lazy { MutableLiveData<HeroesListState>() }
    lateinit var heroesList: List<Hero>

    private val token: String = "eyJhbGciOiJIUzI1NiIsImtpZCI6InByaXZhdGUiLCJ0eXAiOiJKV1QifQ.eyJlbWFpbCI6ImJlamxAa2VlcGNvZGluZy5lcyIsImlkZW50aWZ5IjoiN0FCOEFDNEQtQUQ4Ri00QUNFLUFBNDUtMjFFODRBRThCQkU3IiwiZXhwaXJhdGlvbiI6NjQwOTIyMTEyMDB9.PHf8uuTCyM638Ehd--tt0B5M6sbp-XLAApoeMHc-yZw"

    fun getHeroes() {
        setValueOnMainThread(HeroesListState.Loading)
        val client = OkHttpClient()
        val url = "https://dragonball.keepcoding.education/api/heros/all"
        val requestBody = FormBody.Builder()
            .add("name", "")
            .build()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .post(requestBody)
            .build()
        val call = client.newCall(request)
        call.enqueue(
            object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(HeroesListViewModel::javaClass.name, "Error fetching heroes: ${e.message}")
                    setValueOnMainThread(HeroesListState.Failure(e.message.toString()))
                }
                override fun onResponse(call: Call, response: Response) {
                    Log.d(HeroesListViewModel::javaClass.name, "Success fetching heroes")
                    val responseBody = response.body?.string()
                    val responseHeroes: Array<Hero> = Gson().fromJson(responseBody, Array<Hero>::class.java)
                    val heroes: List<Hero> = responseHeroes.map { Hero(it.photo, it.id, it.name) }
                    Thread.sleep(2_000)

                    setValueOnMainThread(HeroesListState.Success(heroes))
                    heroes.forEach { println(it) }
                    heroesList = heroes
                }
            }
        )
    }

    fun setValueOnMainThread(value: HeroesListState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    sealed class HeroesListState {
        data class Success(val heroes: List<Hero>): HeroesListState()
        data class Failure(val errorMessage: String): HeroesListState()
        object Loading: HeroesListState()

    }
}
