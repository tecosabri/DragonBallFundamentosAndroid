package com.isabri.myapplication.ui.battle

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.isabri.myapplication.databinding.FragmentBattleBinding
import com.isabri.myapplication.ui.BattleGroundActivity
import com.isabri.myapplication.ui.BattleGroundViewModel

class Battle : Fragment() {

    private val viewModel: BattleGroundViewModel by activityViewModels()
    private lateinit var binding: FragmentBattleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBattleBinding.inflate(inflater)

        binding.bFight.setOnClickListener {
            val returnToPreviousScene = viewModel.fight()
            reloadLifeViews()
            if(returnToPreviousScene) returnToPreviousScene()
        }

        val hero1 = viewModel.fightingHeroes[0]
        val hero2 = viewModel.fightingHeroes[1]

        binding.presentation.text = "${hero1.name} will fight ${hero2.name}"
        binding.hero1name.text = hero1.name
        binding.hero2name.text = hero2.name
        binding.hero1Life.progress = hero1.currentLive
        binding.hero2Life.progress = hero2.currentLive
        binding.ivHero1Photo.load(hero1.photo)
        binding.ivHero2Photo.load(hero2.photo)

        return binding.root
    }

    private fun reloadLifeViews() {
        binding.hero1Life.progress = viewModel.fightingHeroes[0].currentLive
        binding.hero2Life.progress = viewModel.fightingHeroes[1].currentLive
    }

    private fun returnToPreviousScene() {
        val activity = activity as BattleGroundActivity
        activity.supportFragmentManager
            .popBackStack()
    }
}