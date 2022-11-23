package com.isabri.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.isabri.myapplication.databinding.ActivityBattleGroundBinding
import com.isabri.myapplication.ui.heroesList.HeroesList

class BattleGroundActivity : AppCompatActivity() {

    lateinit var binding: ActivityBattleGroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityBattleGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.container.id, HeroesList())
                .commit()
        }
    }
}