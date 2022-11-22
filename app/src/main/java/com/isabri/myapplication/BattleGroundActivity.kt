package com.isabri.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isabri.myapplication.databinding.ActivityBattleGroundBinding
import com.isabri.myapplication.ui.heroeslist.HeroesListFragment

class BattleGroundActivity : AppCompatActivity() {

    lateinit var binding: ActivityBattleGroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBattleGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, HeroesListFragment.newInstance())
                .commitNow()
        }
    }
}