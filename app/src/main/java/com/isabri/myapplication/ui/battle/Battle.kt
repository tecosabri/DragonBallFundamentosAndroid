package com.isabri.myapplication.ui.battle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.isabri.myapplication.databinding.FragmentBattleBinding
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
            viewModel.fight()
            reloadLifeViews()
        }

        binding.presentation.text = "${viewModel.fightingHeroes[0].name} will fight ${viewModel.fightingHeroes[1].name}"
        binding.hero1name.text = viewModel.fightingHeroes[0].name
        binding.hero2name.text = viewModel.fightingHeroes[1].name
        binding.hero1Life.progress = viewModel.fightingHeroes[0].currentLive
        binding.hero2Life.progress = viewModel.fightingHeroes[1].currentLive

        return binding.root
    }

    private fun reloadLifeViews() {
        binding.hero1Life.progress = viewModel.fightingHeroes[0].currentLive
        binding.hero2Life.progress = viewModel.fightingHeroes[1].currentLive
    }
}