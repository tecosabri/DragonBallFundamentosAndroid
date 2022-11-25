package com.isabri.myapplication.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.isabri.myapplication.R
import com.isabri.myapplication.domain.models.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException




class BattleGroundViewModel: ViewModel() {

    val stateLiveData: MutableLiveData<HeroesListState> by lazy { MutableLiveData<HeroesListState>() }
    lateinit var heroesList: List<Hero>
    var fightingHeroes: MutableList<Hero> = arrayListOf()
    var token: String = ""

    fun getHeroes() {
        if(token.isBlank()) return
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
                    Log.d("MyLog", "Error fetching heroes: ${e.message}")
                    setValueOnMainThread(HeroesListState.Failure(e.message.toString()))
                }
                override fun onResponse(call: Call, response: Response) {
                    Log.d("MyLog", "Success fetching heroes")
                    val responseBody = response.body?.string()
                    val responseHeroes: Array<Hero> = Gson().fromJson(responseBody, Array<Hero>::class.java)
                    val heroes: List<Hero> = responseHeroes.map { Hero(it.photo, it.id, it.name) }
                    setValueOnMainThread(HeroesListState.Success(heroes))
                    heroesList = heroes
                }
            }
        )
    }

    // Returns true if the selected hero is able to fight (is not dead)
    fun setFightingHeroes(selectedHero: Hero): Boolean {
        // Get random number other than selectedHero position
        if(selectedHero.currentLive == 0) return false // Avoids selecting a dead hero
        if(getWinner() != null) return false // The function only continues if more than 1 hero alive
        heroesList.let {
            val aliveHeroesExcludingSelectedHero = it.filter { hero -> hero != selectedHero && hero.currentLive > 0}
            val randomHero = aliveHeroesExcludingSelectedHero[(aliveHeroesExcludingSelectedHero.indices).random()]
            fightingHeroes.add(0, randomHero)
            fightingHeroes.add(0, selectedHero)
        }
        return true
    }

    fun setValueOnMainThread(value: HeroesListState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    // Returns true if any of the heroes has died
    fun fight(): Boolean {
        val hero1damage = (10..60).random()
        val hero2damage = (10..60).random()
        fightingHeroes[0].let { hero1 ->
            fightingHeroes[1].let { hero2 ->
                hero1.currentLive -= hero2damage
                hero2.currentLive -= hero1damage
                val hero1isDead = checkNonNegativeHeroLife(hero1)
                val hero2isDead = checkNonNegativeHeroLife(hero2)
                if(hero1isDead || hero2isDead) {
                    return true
                }
            }
        }
        return false
    }

    // Returns true if the hero has died and sets its life to 0
    private fun checkNonNegativeHeroLife(hero: Hero): Boolean {
        when(hero.currentLive) {
            in Int.MIN_VALUE..-1 -> {
                hero.currentLive = 0
                return true
            }
            0 -> return true

        }
        return false
    }

    // Returns the only hero alive, an empty name hero if all heroes are dead or null if there is still any hero alive
    private fun getWinner(): Hero? {
        val aliveHeroes = heroesList.filter { it.currentLive > 0 }
        if(aliveHeroes.count() == 1) return aliveHeroes[0]
        if(aliveHeroes.isEmpty()) return Hero("","","")
        return null
    }

    // Checks the winner and shows a toast with the result
    fun checkWinner(context: Context) {
        if(this::heroesList.isInitialized) {
            getWinner()?.let {
                if (it.name.isBlank()) Toast.makeText(
                    context,
                    context.resources.getString(R.string.gameEnded),
                    Toast.LENGTH_LONG
                ).show()
                if (!it.name.isBlank()) Toast.makeText(
                    context,
                    context.resources.getString(R.string.heroHasWon, it.name),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    sealed class HeroesListState {
        data class Success(val heroes: List<Hero>): HeroesListState()
        data class Failure(val errorMessage: String): HeroesListState()
        object Loading: HeroesListState()
    }
}
