package com.isabri.myapplication.ui

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




class BattleGroundViewModel: ViewModel() {

    val stateLiveData: MutableLiveData<HeroesListState> by lazy { MutableLiveData<HeroesListState>() }
    lateinit var heroesList: List<Hero>
    var fightingHeroes: MutableList<Hero> = arrayListOf()

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
                    Log.e(BattleGroundViewModel::javaClass.name, "Error fetching heroes: ${e.message}")
                    setValueOnMainThread(HeroesListState.Failure(e.message.toString()))
                }
                override fun onResponse(call: Call, response: Response) {
                    Log.d(BattleGroundViewModel::javaClass.name, "Success fetching heroes")
                    val responseBody = response.body?.string()
                    val responseHeroes: Array<Hero> = Gson().fromJson(responseBody, Array<Hero>::class.java)
                    val heroes: List<Hero> = responseHeroes.map { Hero(it.photo, it.id, it.name) }
                    setValueOnMainThread(HeroesListState.Success(heroes))
                    heroes.forEach { println(it) }
                    heroesList = heroes
                }
            }
        )
    }

    fun setFightingHeroes(selectedHero: Hero) {
        // Get random number other than selectedHero position

        heroesList.let {
            val heroesExcludingSelectedHero = it.filter { hero -> hero != selectedHero }
            val randomHero = heroesExcludingSelectedHero[(0..heroesExcludingSelectedHero.size).random()]
            fightingHeroes.add(0, randomHero)
            fightingHeroes.add(0, selectedHero)
        }
    }

    fun setValueOnMainThread(value: HeroesListState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    fun fight() {
        val hero1damage = (0..60).random()
        val hero2damage = (0..60).random()
        with(fightingHeroes[0]) {
            currentLive -= hero2damage
            if(currentLive <= 0) {
            //TODO to selection panel
            }
        }
        with(fightingHeroes[1]) {
            currentLive -= hero1damage
            if(currentLive <= 0) {
                // TODO select another random hero
            }
        }
        println(" hero 1 life is ${fightingHeroes[0].currentLive} and hero 2 life is ${fightingHeroes[1].currentLive}")
    }

    sealed class HeroesListState {
        data class Success(val heroes: List<Hero>): HeroesListState()
        data class Failure(val errorMessage: String): HeroesListState()
        object Loading: HeroesListState()

    }
}
